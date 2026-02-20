package tests;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import base.BaseTest;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ExcelUtil;
import utils.ExtentManager;
import utils.ScreenshotUtil;

public class LoginTest extends BaseTest {
	
	private LoginPage loginPage;
    private ExtentTest test;
    
    @BeforeMethod
    public void setUpTest() {
        loginPage = new LoginPage(driver);
    }
    
    @Test(priority = 1, description = "Verify login with valid credentials")
    public void testValidLogin() {
        test = extent.createTest("Valid Login Test", "Verify user can login with valid credentials");
        test.log(Status.INFO, "Starting valid login test");
        
        Assert.assertTrue(loginPage.isLoginLogoDisplayed(), "Login page should be displayed");
        test.log(Status.PASS, "Login page displayed successfully");
        
        ProductsPage productsPage = loginPage.login("standard_user", "secret_sauce");
        test.log(Status.INFO, "Entered credentials and clicked login");
        
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "User should be on products page");
        Assert.assertEquals(productsPage.getPageTitle(), "Products", "Page title should be 'Products'");
        test.log(Status.PASS, "Login successful - User navigated to products page");
        
        logger.info("Valid login test passed");
    }
    
    @Test(priority = 2, description = "Verify login fails with invalid username")
    public void testInvalidUsername() {
        test = extent.createTest("Invalid Username Test", "Verify login fails with invalid username");
        test.log(Status.INFO, "Starting invalid username test");
        
        loginPage.enterUsername("invalid_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLoginButton();
        test.log(Status.INFO, "Entered invalid credentials");
        
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Username and password do not match"), 
                "Error message should contain 'Username and password do not match'");
        test.log(Status.PASS, "Error message displayed: " + errorMsg);
        
        logger.info("Invalid username test passed");
    }
    
    @Test(priority = 3, description = "Verify login fails with invalid password")
    public void testInvalidPassword() {
        test = extent.createTest("Invalid Password Test", "Verify login fails with invalid password");
        test.log(Status.INFO, "Starting invalid password test");
        
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("wrong_password");
        loginPage.clickLoginButton();
        test.log(Status.INFO, "Entered username with wrong password");
        
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Username and password do not match"), 
                "Error message should indicate credentials mismatch");
        test.log(Status.PASS, "Error message displayed correctly");
        
        logger.info("Invalid password test passed");
    }
    
    @Test(priority = 4, description = "Verify login fails with empty username")
    public void testEmptyUsername() {
        test = extent.createTest("Empty Username Test", "Verify login fails when username is empty");
        test.log(Status.INFO, "Starting empty username test");
        
        loginPage.enterUsername("");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLoginButton();
        test.log(Status.INFO, "Clicked login with empty username");
        
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Username is required"), 
                "Error message should indicate username is required");
        test.log(Status.PASS, "Appropriate error message displayed");
        
        logger.info("Empty username test passed");
    }
    
    @Test(priority = 5, dataProvider = "loginDataFromExcel", description = "Data-driven login test")
    public void testLoginWithMultipleUsers(String username, String password, String expectedResult) {
        test = extent.createTest("Login Test for: " + username, 
                "Testing login with different user types");
        test.log(Status.INFO, "Testing login with username: " + username);
        
        ProductsPage productsPage = loginPage.login(username, password);
        
        if (expectedResult.equals("success")) {
            Assert.assertTrue(productsPage.isProductsPageDisplayed(), 
                    "Login should be successful for " + username);
            test.log(Status.PASS, "Login successful for " + username);
        } else {
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
                    "Login should fail for " + username);
            test.log(Status.PASS, "Login correctly failed for " + username);
        }
        
        logger.info("Data-driven login test completed for: " + username);
    }
    
    /**
     * DATAPROVIDER READING FROM EXCEL
     * Reads test data from TestData.xlsx file, LoginData sheet
     */
    @DataProvider(name = "loginDataFromExcel")
    public Object[][] getLoginDataFromExcel() {
        String excelPath = "src/test/resources/testdata/TestData.xlsx";
        String sheetName = "LoginData";
        
        logger.info("Reading login test data from Excel: " + excelPath);
        
        Object[][] data = ExcelUtil.getExcelData(excelPath, sheetName);
        
        logger.info("Loaded " + data.length + " rows of test data from Excel");
        
        return data;
    }
    
    /* 
     * Alternative: Used for executing data based test through hard-code
     * */
//    @DataProvider(name = "loginData")
//    public Object[][] getLoginData() {
//        return new Object[][] {
//            {"standard_user", "secret_sauce", "success"},
//            {"locked_out_user", "secret_sauce", "failure"},
//            {"problem_user", "secret_sauce", "success"},
//            {"performance_glitch_user", "secret_sauce", "success"}
//        };
//    }
    
    @Test(priority = 6, description = "Verify login with invalid credentials")
    public void testInvalidLogin_ShouldCaptureScreenshot() {
    	test = extent.createTest("Valid Login Test", "Verify user can login with valid credentials");
        test.log(Status.INFO, "Starting valid login test");
        
        ProductsPage productsPage = loginPage.login("invalid_user", "secret_sauce");
        
        // This assertion will fail intentionally
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"), "Usershould not be redirected to inventory page");
    }
    
    @AfterMethod
    public void captureScreenshotOnFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(driver, result.getName());
            test.log(Status.FAIL, "Test Failed: " + result.getThrowable());
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath);
            }
            logger.error("Test failed: " + result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, "Test Passed");
        }
        ExtentManager.flushReport();
    }
}
