package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Open Products
    public void openProducts() {

        wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//nav//button[normalize-space()='Products']")))
                .click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("productList")));
    }

    // Admin: Add Product
    public void addProductAsAdmin(String name, String price, String quantity) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("adminForm")));

        WebElement nameField = driver.findElement(By.id("pName"));
        WebElement priceField = driver.findElement(By.id("pPrice"));
        WebElement quantityField = driver.findElement(By.id("pQty"));

        nameField.clear();
        nameField.sendKeys(name);

        priceField.clear();
        priceField.sendKeys(price);

        quantityField.clear();
        quantityField.sendKeys(quantity);

        driver.findElement(
                By.cssSelector("#adminForm button[type='submit']")
        ).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("msg"), "Product added"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                productCard(name)));

        System.out.println("Product created: " + name);
    }

    // Verify Product
    public boolean productIsVisible(String name) {

        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            productCard(name))
            ).isDisplayed();

        } catch (TimeoutException e) {

            System.out.println("Product not found: " + name);

            System.out.println("Product list HTML: " +
                    driver.findElement(By.id("productList"))
                            .getAttribute("innerHTML"));

            return false;
        }
    }

    // Add Product to Cart
    public void addToCart(String productName, int quantity) {

        WebElement card = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        productCard(productName)));

        WebElement quantityField = card.findElement(
                By.cssSelector("input[type='number']"));

        quantityField.clear();
        quantityField.sendKeys(String.valueOf(quantity));

        WebElement addButton = card.findElement(
                By.xpath(".//button[normalize-space()='Add to Cart']"));

        wait.until(ExpectedConditions.elementToBeClickable(addButton));

        addButton.click();

        System.out.println("Clicked Add to Cart for: " + productName);

        // Do not depend on the temporary notification.
        // CartPage will verify whether the product was actually added.
    }

    // Product locator
    private By productCard(String name) {

        return By.xpath(
                "//div[@id='productList']//div[contains(@class,'card')]" +
                        "[.//h3[normalize-space()='" + name + "']]"
        );
    }
}