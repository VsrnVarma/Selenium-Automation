package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import base.BasePage;

public class LoginPage extends BasePage {
	
	// Page Elements using @FindBy annotation
    @FindBy(id = "user-name")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(css = "h3[data-test='error']")
    private WebElement errorMessage;
    
    @FindBy(className = "login_logo")
    private WebElement loginLogo;
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    public void enterUsername(String username) {
        sendKeys(usernameField, username);
        logger.info("Entered username: " + username);
    }
    
    public void enterPassword(String password) {
        sendKeys(passwordField, password);
        logger.info("Entered password");
    }
    
    /**
     * Click login button
     */
    public void clickLoginButton() {
        click(loginButton);
        logger.info("Clicked login button");
    }
    
    /**
     * Complete login with username and password
     * @param username Username
     * @param password Password
     * @return ProductsPage object
     */
    public ProductsPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        logger.info("Login performed with username: " + username);
        return new ProductsPage(driver);
    }
    
    public String getErrorMessage() {
        String error = getText(errorMessage);
        logger.info("Error message displayed: " + error);
        return error;
    }
    
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }
    
    public boolean isLoginLogoDisplayed() {
        return isElementDisplayed(loginLogo);
    }
    
    public String getLoginPageTitle() {
        return getPageTitle();
    }
}
