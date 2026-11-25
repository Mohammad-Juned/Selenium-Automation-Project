package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OrdersPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    public OrdersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public void openOrders() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//nav//button[normalize-space()='My Orders']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orderList")));
    }
    public boolean hasOrderContaining(String productName) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orderList")))
                .findElements(By.xpath(".//div[contains(@class,'order')]//td[normalize-space()='" + productName + "']")).size() > 0;
    }
}
