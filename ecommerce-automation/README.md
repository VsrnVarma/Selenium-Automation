# E-Commerce Test Automation Framework
A comprehensive Selenium WebDriver automation framework for e-commerce web applications, built with Java and TestNG, following industry best practices including Page Object Model (POM), data-driven testing, and automated reporting.

## Proect Overview
This framework automates end-to-end testing of the Sauce Demo e-commerce application — a demo site designed specifically for automation practice. The project demonstrates real-world automation skills including framework design, test architecture, and CI/CD readiness.

## Features
- Page Object Model (POM) - Clean separation of page elements and test logic
- Data-Driven Testing - Multiple test scenarios using TestNG DataProviders and Excel
- TestNG Framework - Parallel execution, grouping, and prioritization
- Extent Reports - Rich HTML test reports with charts and logs
- Maven - Easy dependency management and test execution
- Cross-Browser Support - Chrome, Firefox and Edge
- Configurable - Environment-specific configurations
- Logging - Detailed execution logs for debugging
- Screenshot on Failure - Automatic screenshot capture

## Tech Stack
- Java 11+
- Selenium WebDriver 4.35.0
- TestNG 7.11.0
- Maven 3.9.11
- Apache POI (Excel handling)
- Extent Reports 5.1.1
- Log4j 2.22.0
- WebDriverManager 5.6.2

## Test Scenarios
**Login Module (LoginTest.java)**
| Test Case | Description | Expected Result |
|-----------|-------------|-----------------|
| testValidLogin | Login with correct credentials | Navigates to Products page |
| testInvalidUsername | Login with wrong username | Error message displayed |
| testInvalidPassword | Login with wrong password | Error message displayed |
| testEmptyUsername | Login without entering username | "Username is required" error |
| testLoginWithMultipleUsers | Data-driven test for multiple user types | Pass/Fail per user type |    

**Cart Module (LoginTest.java)**
| Test Case | Description | Expected Result |
|-----------|-------------|-----------------|
| testAddSingleProductToCart | Add one product to cart | Cart shows count as 1 |
| testAddMultipleProductsToCart | Add 3 products to cart | Cart shows count as 3 |
| testRemoveProductFromCart | Remove item from cart | Cart count decreases |
| testCartPersistence | Navigate away and return to cart | Cart count remains same |
| testEmptyCart | Open cart without adding products | Cart is empty |

**Checkout Module (LoginTest.java)**
| Test Case | Description | Expected Result |
|-----------|-------------|-----------------|
| testCompleteCheckoutProcess | End-to-end checkout flow | Order confirmation displayed |
| testCheckoutWithMissingFirstName | Submit without first name | "First Name is required" error |
| testCheckoutWithMissingLastName | Submit without last name | "Last Name is required" error |
| testCheckoutWithMissingPostalCode | Submit without postal code | "Postal Code is required" error |

## Running Tests  
**Run all tests**  
mvn clean test  

**Run specific test class**  
mvn clean test -Dtest=LoginTest  
mvn clean test -Dtest=CartTest  
mvn clean test -Dtest=CheckoutTest

**Run specific test method**  
mvn clean test -Dtest=LoginTest#testValidLogin

**Run in different browser**  
mvn clean test -Dbrowser=chrome  
mvn clean test -Dbrowser=firefox  
mvn clean test -Dbrowser=edge

## Test Reports  
**Extent Reports:** reports/ExtentReport_<timestamp>.html  
- Open in any browser
- Shows pass/fail summary, charts, and logs
- Includes screenshots for failed tests

**Application logs:** logs/application.log

**Failure Screenshot:** screenshots/<testName>_<timestamp>.png

## Known Issues
**Chrome Password Manager Pop-up:**  
Issue- Google Password Manager popup appears during tests  
Fix- Disabled via ChromeOptions preferences in BaseTest.java by using browser incognito mode

**CDP Version Warning**  
Issue: WARNING: Unable to find CDP implementation matching 144  
Impact: No impact on test execution, cosmetic warning only  
Fix: Update Selenium version when compatible version is released