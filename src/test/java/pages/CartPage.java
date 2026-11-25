package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Reusable pause for observing browser actions
    private void pause() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    // Open Cart
    public void openCart() {

        wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//nav//button[normalize-space()='Cart']")))
                .click();

        pause();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("cart-view")));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("cartBox")));

        System.out.println("Cart page opened successfully.");
    }

    // Verify product exists in Cart
    public boolean containsProduct(String name) {

        By productLocator = By.xpath(
                "//div[@id='cartBox']//tr/td[normalize-space()='" + name + "']"
        );

        try {
            wait.until(driver ->
                    !driver.findElements(productLocator).isEmpty()
            );

            System.out.println("Product in cart: true");
            pause();

            return true;

        } catch (TimeoutException e) {

            System.out.println("Product not found in cart: " + name);

            System.out.println("Cart HTML: " +
                    driver.findElement(By.id("cartBox"))
                            .getAttribute("innerHTML"));

            return false;
        }
    }

    // Update Cart Quantity
    public void updateFirstItemQuantity(int quantity) {

        By quantityInput = By.cssSelector(
                "#cartBox table input[type='number']"
        );

        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(quantityInput)
        );

        input.clear();
        input.sendKeys(String.valueOf(quantity));

        pause();

        // Click Update
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(
                        "(//div[@id='cartBox']//button[normalize-space()='Update'])[1]"
                )
        )).click();

        pause();

        // Verify quantity
        wait.until(driver -> {
            try {
                WebElement updatedInput = driver.findElement(quantityInput);

                return String.valueOf(quantity).equals(
                        updatedInput.getAttribute("value")
                );

            } catch (StaleElementReferenceException e) {
                return false;
            }
        });

        System.out.println("Cart quantity updated successfully: " + quantity);
    }

    // Place Order
    public void placeOrder() {

        pause();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(
                        "//div[@id='cartBox']//button[normalize-space()='Place Order']"
                )
        )).click();

        // Wait for Orders view
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("orders-view")
        ));

        pause();

        System.out.println("Order placed successfully.");
    }
}