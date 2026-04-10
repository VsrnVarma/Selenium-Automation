package tests;

import base.BaseTest;
import pages.LoginPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import utils.BrokenLinkChecker;

/**
 * Test class to validate broken links
 */
public class BrokenLinkTest extends BaseTest {
	private LoginPage loginPage;
    //private ExtentTest test;
    
    @BeforeMethod
    public void setUpTest() {
        loginPage = new LoginPage(driver);
    }
    
    @Test(priority = 1, description = "Validate links on products page")
    public void testProductsPageLinks() {
        // Login and navigate to products page
        loginPage.login("standard_user", "secret_sauce");
        
        // Validate links
        BrokenLinkChecker linkChecker = new BrokenLinkChecker(driver);
        linkChecker.validateAllLinks();
        
        logger.info("Products page link validation completed successfully");
    }
}