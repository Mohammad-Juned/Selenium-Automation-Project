package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Login locators
    private final By loginEmail = By.id("loginEmail");
    private final By loginPassword = By.id("loginPassword");
    private final By loginButton =
            By.cssSelector("#loginForm button[type='submit']");

    // Registration locators
    private final By registerLink =
            By.cssSelector("#loginForm a");

    private final By regName = By.id("regName");
    private final By regEmail = By.id("regEmail");
    private final By regPassword = By.id("regPassword");

    private final By registerButton =
            By.cssSelector("#registerForm button[type='submit']");

    // Other locators
    private final By logoutButton =
            By.cssSelector("#nav button.danger");

    private final By messageBox = By.id("msg");
    private final By authView = By.id("auth-view");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // =========================
    // LOGIN
    // =========================

    public void login(String email, String password) {

        WebElement emailField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(loginEmail));

        // Clear previous email before entering new email
        emailField.clear();
        emailField.sendKeys(email);

        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(loginPassword));

        passwordField.clear();
        passwordField.sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(loginButton))
                .click();
    }

    // =========================
    // LOGOUT
    // =========================

    public void logout() {

        wait.until(ExpectedConditions.elementToBeClickable(logoutButton))
                .click();

        // After logout, login form should appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginEmail));

        System.out.println("Logout successful.");
    }

    // =========================
    // REGISTER
    // =========================

    public void register(String name, String email, String password) {

        // Switch from Login form to Registration form
        wait.until(ExpectedConditions.elementToBeClickable(registerLink))
                .click();

        WebElement nameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(regName));

        nameField.clear();
        nameField.sendKeys(name);

        WebElement emailField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(regEmail));

        emailField.clear();
        emailField.sendKeys(email);

        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(regPassword));

        passwordField.clear();
        passwordField.sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(registerButton))
                .click();

        // Registration JavaScript returns the application to login form
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginEmail));

        System.out.println("Registration form submitted for: " + email);
    }

    // =========================
    // READ NOTIFICATION MESSAGE
    // =========================

    public String message() {

        /*
         * Notification disappears after 3.5 seconds.
         * Read its text directly instead of waiting for visibility.
         */

        return (String) ((JavascriptExecutor) driver).executeScript(
                "var element = document.getElementById('msg');" +
                        "return element ? element.textContent.trim() : '';"
        );
    }

    // =========================
    // CHECK AUTH SCREEN
    // =========================

    public boolean isAuthVisible() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(authView)
        ).isDisplayed();
    }
}