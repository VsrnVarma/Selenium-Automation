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
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ExcelUtil;
import utils.ExtentManager;
import utils.ScreenshotUtil;

public class CheckoutTest extends BaseTest {

	private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private ExtentTest test;
    
    @BeforeMethod
    public void setUpTest() {
        loginPage = new LoginPage(driver);
        productsPage = loginPage.login("standard_user", "secret_sauce");
    }
    
    @Test(priority = 1, description = "Verify complete checkout process end-to-end")
    public void testCompleteCheckoutProcess() {
        test = extent.createTest("Complete Checkout Test", 
                "Verify user can complete entire checkout process");
        test.log(Status.INFO, "Starting complete checkout test");
        
        productsPage.addProductToCart("sauce-labs-backpack");
        productsPage.addProductToCart("sauce-labs-bike-light");
        test.log(Status.INFO, "Added 2 products to cart");
        
        cartPage = productsPage.clickShoppingCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should have 2 items");
        test.log(Status.PASS, "Cart contains correct items");
        
        checkoutPage = cartPage.proceedToCheckout();
        test.log(Status.INFO, "Proceeded to checkout");
        
        checkoutPage.fillCheckoutInformation("John", "Doe", "12345");
        checkoutPage.clickContinue();
        test.log(Status.INFO, "Entered checkout information");
        
        Assert.assertEquals(checkoutPage.getPageTitle(), "Checkout: Overview", 
                "Should be on checkout overview page");
        test.log(Status.PASS, "Checkout overview page displayed");
        
        String subtotal = checkoutPage.getSubtotal();
        String tax = checkoutPage.getTax();
        String total = checkoutPage.getTotal();
        Assert.assertFalse(subtotal.isEmpty(), "Subtotal should be displayed");
        Assert.assertFalse(tax.isEmpty(), "Tax should be displayed");
        Assert.assertFalse(total.isEmpty(), "Total should be displayed");
        test.log(Status.INFO, "Price details - Subtotal: " + subtotal + ", Tax: " + tax + ", Total: " + total);
        
        checkoutPage.clickFinish();
        test.log(Status.INFO, "Clicked finish button");
        
        Assert.assertTrue(checkoutPage.isOrderCompleted(), "Order should be completed");
        String completeHeader = checkoutPage.getCompleteHeader();
        Assert.assertEquals(completeHeader, "Thank you for your order!", 
                "Order completion message should be displayed");
        test.log(Status.PASS, "Order completed successfully: " + completeHeader);
        
        logger.info("Complete checkout test passed");
    }
    
    @Test(priority = 2, dataProvider="checkoutDataFromExcel", description = "Test checkout with different customer data from Excel")
    public void testCheckoutWithExcelData(String firstName, String lastName, String postalCode) {
        test = extent.createTest("Checkout Test: " + firstName + " " + lastName, "Testing checkout with data from Excel");
        test.log(Status.INFO, "Customer: " + firstName + " " + lastName + ", ZIP: " + postalCode);
        
        productsPage.addProductToCart("sauce-labs-bike-light");
        test.log(Status.INFO, "Added product to cart");
        
        cartPage = productsPage.clickShoppingCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should have 1 item");
        test.log(Status.PASS, "Cart contains correct items");
        
        checkoutPage = cartPage.proceedToCheckout();
        test.log(Status.INFO, "Navigated to checkout Page");
        
        checkoutPage.fillCheckoutInformation(firstName, lastName, postalCode);
        checkoutPage.clickContinue();
        test.log(Status.INFO, "Filled checkout form with Excel data");
        
        Assert.assertEquals(checkoutPage.getPageTitle(), "Checkout: Overview", 
                "Should be on checkout overview page");
        test.log(Status.PASS, "Checkout successfull with:" + firstName + " " + lastName + "page displayed");
        
        logger.info("Checkout test passed for:" + firstName + " " + lastName + "page displayed");
    }
    
    // DATAPROVIDER FOR EXCEL CHECKOUT DATA
    @DataProvider(name = "checkoutDataFromExcel") 
    public Object[][] getCheckoutDataFromExcel() { 
    	String excelPath = "src/test/resources/testdata/TestData.xlsx"; 
    	String sheetName = "CheckoutData"; 
    	logger.info("Reading checkout test data from Excel: " + excelPath); 
    	Object[][] data = ExcelUtil.getExcelData(excelPath, sheetName); 
    	logger.info("Loaded " + data.length + " rows of test data from Excel");
    	return data;
    }
    
    @Test(priority = 3, description = "Verify checkout fails with missing first name")
    public void testCheckoutWithMissingFirstName() {
        test = extent.createTest("Checkout Missing First Name Test", 
                "Verify error when first name is not provided");
        test.log(Status.INFO, "Starting missing first name test");
        
        productsPage.addProductToCart("sauce-labs-backpack");
        cartPage = productsPage.clickShoppingCart();
        checkoutPage = cartPage.proceedToCheckout();
        
        checkoutPage.enterLastName("Doe");
        checkoutPage.enterPostalCode("12345");
        checkoutPage.clickContinue();
        test.log(Status.INFO, "Attempted checkout without first name");
        
        String errorMsg = checkoutPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("First Name is required"), 
                "Error message should indicate first name is required");
        test.log(Status.PASS, "Appropriate error displayed: " + errorMsg);
        
        logger.info("Missing first name test passed");
    }
    
    @Test(priority = 4, description = "Verify checkout fails with missing last name")
    public void testCheckoutWithMissingLastName() {
        test = extent.createTest("Checkout Missing Last Name Test", 
                "Verify error when last name is not provided");
        test.log(Status.INFO, "Starting missing last name test");
        
        productsPage.addProductToCart("sauce-labs-backpack");
        cartPage = productsPage.clickShoppingCart();
        checkoutPage = cartPage.proceedToCheckout();
        
        checkoutPage.enterFirstName("John");
        checkoutPage.enterPostalCode("12345");
        checkoutPage.clickContinue();
        test.log(Status.INFO, "Attempted checkout without last name");
        
        String errorMsg = checkoutPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Last Name is required"), 
                "Error message should indicate last name is required");
        test.log(Status.PASS, "Appropriate error displayed: " + errorMsg);
        
        logger.info("Missing last name test passed");
    }
    
    @Test(priority = 5, description = "Verify checkout fails with missing postal code")
    public void testCheckoutWithMissingPostalCode() {
        test = extent.createTest("Checkout Missing Postal Code Test", 
                "Verify error when postal code is not provided");
        test.log(Status.INFO, "Starting missing postal code test");
        
        productsPage.addProductToCart("sauce-labs-backpack");
        cartPage = productsPage.clickShoppingCart();
        checkoutPage = cartPage.proceedToCheckout();
        
        checkoutPage.enterFirstName("John");
        checkoutPage.enterLastName("Doe");
        checkoutPage.clickContinue();
        test.log(Status.INFO, "Attempted checkout without postal code");
        
        String errorMsg = checkoutPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Postal Code is required"), 
                "Error message should indicate postal code is required");
        test.log(Status.PASS, "Appropriate error displayed: " + errorMsg);
        
        logger.info("Missing postal code test passed");
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
