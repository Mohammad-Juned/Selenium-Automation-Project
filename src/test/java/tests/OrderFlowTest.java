package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import java.util.UUID;

public class OrderFlowTest extends BaseTest {

    @Test
    public void adminCreatesProduct_userRegisters_addsToCart_andPlacesOrder()
            throws InterruptedException {

        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String productName = "Selenium Test Product " + suffix;
        String userEmail = "selenium." + suffix + "@example.com";
        String password = "Test@123";

        LoginPage auth = new LoginPage(driver);
        ProductsPage products = new ProductsPage(driver);
        CartPage cart = new CartPage(driver);
        OrdersPage orders = new OrdersPage(driver);

        // =====================================
        // 1. ADMIN LOGIN
        // =====================================

        System.out.println("\n========== ADMIN LOGIN ==========");

        auth.login("admin@example.com", "admin123");

        Thread.sleep(2000);

        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page title: " + driver.getTitle());

        // Check browser localStorage
        JavascriptExecutor js = (JavascriptExecutor) driver;

        System.out.println("Stored role: " +
                js.executeScript(
                        "return localStorage.getItem('role')"));

        System.out.println("Stored email: " +
                js.executeScript(
                        "return localStorage.getItem('email')"));

        // Check notification
        System.out.println("Notification: " +
                driver.findElement(By.id("msg")).getText());

        // Check admin form
        System.out.println("Admin form exists: " +
                driver.findElements(By.id("adminForm")).size());

        System.out.println("Admin form visible: " +
                (!driver.findElements(By.id("adminForm")).isEmpty()
                        && driver.findElement(By.id("adminForm")).isDisplayed()));

        System.out.println("Admin form hidden attribute: " +
                driver.findElement(By.id("adminForm"))
                        .getAttribute("hidden"));

        System.out.println("========== END DEBUG ==========\n");

        // =====================================
        // 2. VERIFY ADMIN FORM
        // =====================================

        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("adminForm"))).isDisplayed(),
                "Admin should see the Add Product form"
        );

        // =====================================
        // 3. CREATE PRODUCT
        // =====================================

        System.out.println("Creating product: " + productName);

        products.addProductAsAdmin(productName, "199.00", "20");

        // =====================================
        // 4. ADMIN LOGOUT
        // =====================================

        System.out.println("Logging out admin...");

        auth.logout();

        // =====================================
        // 5. CUSTOMER REGISTRATION
        // =====================================

        System.out.println("Registering customer: " + userEmail);

        auth.register("Selenium Test User", userEmail, password);

        Assert.assertTrue(
                auth.message().contains("Registered successfully"),
                "Customer registration should be successful"
        );

        // =====================================
        // 6. CUSTOMER LOGIN
        // =====================================

        System.out.println("Customer login...");

        auth.login(userEmail, password);

        // =====================================
        // 7. VERIFY PRODUCT LISTING
        // =====================================

        Assert.assertTrue(
                products.productIsVisible(productName),
                "New product should be listed for customer"
        );

        // =====================================
        // 8. ADD PRODUCT TO CART
        // =====================================

        products.addToCart(productName, 2);

        cart.openCart();

        Assert.assertTrue(
                cart.containsProduct(productName),
                "Cart should contain the selected product"
        );

        // =====================================
        // 9. UPDATE CART QUANTITY
        // =====================================

        cart.updateFirstItemQuantity(3);

        // =====================================
        // 10. PLACE ORDER
        // =====================================

        cart.placeOrder();

        // =====================================
        // 11. VERIFY ORDER HISTORY
        // =====================================

        orders.openOrders();

        Assert.assertTrue(
                orders.hasOrderContaining(productName),
                "Order history should display the purchased product"
        );

        System.out.println(
                "\n========== END-TO-END ORDER TEST PASSED ==========\n"
        );
    }

    // =====================================
    // INVALID LOGIN TEST
    // =====================================

    @Test
    public void invalidLogin_showsErrorMessage() {

        LoginPage auth = new LoginPage(driver);

        auth.login("wrong.user@example.com", "wrong-password");

        // Wait for the error notification
        String errorMessage = wait.until(driver -> {

            String message = driver.findElement(By.id("msg"))
                    .getText().trim();

            return message.isEmpty() ? null : message;
        });

        System.out.println("Invalid login response: " + errorMessage);

        Assert.assertTrue(
                errorMessage.length() > 0,
                "An error notification should be displayed"
        );

        Assert.assertTrue(
                auth.isAuthVisible(),
                "User should remain on the login/register screen"
        );

        System.out.println("Invalid login test passed!");
    }
}