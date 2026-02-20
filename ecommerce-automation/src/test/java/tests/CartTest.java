package tests;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import base.BaseTest;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductsPage;
import utils.ExtentManager;
import utils.ScreenshotUtil;

/**
 * LoginTest contains test cases for login functionality.
 * Tests both positive and negative scenarios.
 */
public class CartTest extends BaseTest {
	
	private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private ExtentTest test;
    
    @BeforeMethod
    public void setUpTest() {
        loginPage = new LoginPage(driver);
        productsPage = loginPage.login("standard_user", "secret_sauce");
    }
    
    @Test(priority = 1, description = "Verify adding single product to cart")
    public void testAddSingleProductToCart() {
        test = extent.createTest("Add Single Product to Cart", 
                "Verify user can add a product to cart");
        test.log(Status.INFO, "Starting add single product test");
        
        productsPage.addProductToCart("sauce-labs-backpack");
        test.log(Status.INFO, "Added Sauce Labs Backpack to cart");
        
        String cartCount = productsPage.getCartItemCount();
        Assert.assertEquals(cartCount, "1", "Cart should contain 1 item");
        test.log(Status.PASS, "Cart badge shows correct count: " + cartCount);
        
        cartPage = productsPage.clickShoppingCart();
        
        Assert.assertTrue(cartPage.isProductInCart("Sauce Labs Backpack"), 
                "Sauce Labs Backpack should be in cart");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should have 1 item");
        test.log(Status.PASS, "Product successfully added to cart");
        
        logger.info("Add single product test passed");
    }
    
    @Test(priority = 2, description = "Verify adding multiple products to cart")
    public void testAddMultipleProductsToCart() {
        test = extent.createTest("Add Multiple Products to Cart", 
                "Verify user can add multiple products to cart");
        test.log(Status.INFO, "Starting add multiple products test");
        
        productsPage.addProductToCart("sauce-labs-backpack");
        productsPage.addProductToCart("sauce-labs-bike-light");
        productsPage.addProductToCart("sauce-labs-bolt-t-shirt");
        test.log(Status.INFO, "Added 3 products to cart");
        
        String cartCount = productsPage.getCartItemCount();
        Assert.assertEquals(cartCount, "3", "Cart should contain 3 items");
        test.log(Status.PASS, "Cart badge shows: " + cartCount);
        
        cartPage = productsPage.clickShoppingCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 3, "Cart should have 3 items");
        test.log(Status.PASS, "All 3 products added successfully");
        
        logger.info("Add multiple products test passed");
    }
    
    @Test(priority = 3, description = "Verify removing product from cart")
    public void testRemoveProductFromCart() {
        test = extent.createTest("Remove Product from Cart", 
                "Verify user can remove products from cart");
        test.log(Status.INFO, "Starting remove product test");
        
        productsPage.addProductToCart("sauce-labs-backpack");
        productsPage.addProductToCart("sauce-labs-bike-light");
        test.log(Status.INFO, "Added 2 products to cart");
        
        cartPage = productsPage.clickShoppingCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should have 2 items initially");
        
        cartPage.removeItemByIndex(0);
        test.log(Status.INFO, "Removed first item from cart");
        
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should have 1 item after removal");
        test.log(Status.PASS, "Product removed successfully");
        
        logger.info("Remove product test passed");
    }
    
    @Test(priority = 4, description = "Verify cart persists when navigating between pages")
    public void testCartPersistence() {
        test = extent.createTest("Cart Persistence Test", 
                "Verify cart items persist across page navigation");
        test.log(Status.INFO, "Starting cart persistence test");
        
        productsPage.addProductToCart("sauce-labs-backpack");
        String initialCartCount = productsPage.getCartItemCount();
        test.log(Status.INFO, "Added product, cart count: " + initialCartCount);
        
        cartPage = productsPage.clickShoppingCart();
        productsPage = cartPage.continueShopping();
        test.log(Status.INFO, "Navigated to cart and back to products");
        
        String finalCartCount = productsPage.getCartItemCount();
        Assert.assertEquals(finalCartCount, initialCartCount, 
                "Cart count should remain same after navigation");
        test.log(Status.PASS, "Cart persists correctly: " + finalCartCount);
        
        logger.info("Cart persistence test passed");
    }
    
    @Test(priority = 5, description = "Verify empty cart displays correctly")
    public void testEmptyCart() {
        test = extent.createTest("Empty Cart Test", "Verify empty cart state");
        test.log(Status.INFO, "Starting empty cart test");
        
        cartPage = productsPage.clickShoppingCart();
        
        Assert.assertTrue(cartPage.isCartEmpty(), "Cart should be empty");
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Cart count should be 0");
        test.log(Status.PASS, "Empty cart displayed correctly");
        
        logger.info("Empty cart test passed");
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
