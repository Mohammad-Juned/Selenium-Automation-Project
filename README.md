# Order Management System - Selenium UI Automation

A small Selenium 4 + Java 17 + TestNG automation suite for the local Spring Boot Order Management System.

## What it covers
- Invalid login validation
- Admin login and role-based admin form visibility
- Admin creates a product
- Customer registration and login
- Product listing and add-to-cart
- Cart quantity update
- Place order and verify order history

## Prerequisites
- Java 17
- Maven
- Google Chrome
- Your Order Management System running locally at `http://localhost:8080`
- MySQL configured and running for the application

The application seeds this admin account on first startup (from `DataInitializer`):
- Email: `admin@example.com`
- Password: `admin123`

## Run
Open a terminal in this automation project folder:

```bash
mvn clean test
```

Headless run:
```bash
mvn clean test -Dheadless=true
```

If the app uses another port:
```bash
mvn clean test -DbaseUrl=http://localhost:9090
```

WebDriverManager downloads/sets up a compatible ChromeDriver automatically. The first run requires internet access.

## Framework structure
- `base/BaseTest.java` - browser setup and teardown
- `pages/` - Page Object Model classes
- `tests/OrderFlowTest.java` - test scenarios
- `testng.xml` - TestNG suite

## Notes
- This is UI automation: Selenium interacts with the browser like a user; it does not call your REST endpoints directly.
- The end-to-end test creates a uniquely named product and customer each run, so previous test data does not cause name/email conflicts.
- Run against a test database. The test creates records and places an actual order in the configured local database.
- If the product UI changes, update the corresponding locators in the Page Object classes.
- TestNG results are generated under `target/surefire-reports/`.
