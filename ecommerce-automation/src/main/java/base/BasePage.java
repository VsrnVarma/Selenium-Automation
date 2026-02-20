package base;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


 /*BasePage contains common methods that can be used across all page objects.
 * This eliminates code duplication and provides reusable components.
 */
public class BasePage {
	protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    
    protected void waitForElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        logger.debug("Element is visible: " + element);
    }
    
    protected void waitForElementClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        logger.debug("Element is clickable: " + element);
    }
    
    // Click on element with wait
    protected void click(WebElement element) {
        waitForElementClickable(element);
        element.click();
        logger.info("Clicked on element: " + element);
    }
    
    protected void sendKeys(WebElement element, String text) {
        waitForElementVisible(element);
        element.clear();
        element.sendKeys(text);
        logger.info("Entered text '" + text + "' in element: " + element);
    }
    
    /**
     * Get text from element
     */
    protected String getText(WebElement element) {
        waitForElementVisible(element);
        String text = element.getText();
        logger.debug("Retrieved text: " + text);
        return text;
    }
    
    protected void selectDropdown(WebElement element, String text) {
        waitForElementVisible(element);
        Select select = new Select(element);
        select.selectByVisibleText(text);
        logger.info("Selected dropdown option: " + text);
    }
    
    protected boolean isElementDisplayed(WebElement element) {
        try {
            waitForElementVisible(element);
            return element.isDisplayed();
        } catch (Exception e) {
            logger.warn("Element not displayed: " + element);
            return false;
        }
    }
    
    protected String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.debug("Current URL: " + url);
        return url;
    }
    
    protected String getPageTitle() {
        String title = driver.getTitle();
        logger.debug("Page title: " + title);
        return title;
    }
    
    protected void waitForUrlContains(String urlPart) {
        wait.until(ExpectedConditions.urlContains(urlPart));
        logger.info("URL contains: " + urlPart);
    }
    
    // Get attribute value of element
    protected String getAttribute(WebElement element, String attribute) {
        waitForElementVisible(element);
        String value = element.getAttribute(attribute);
        logger.debug("Attribute '" + attribute + "' value: " + value);
        return value;
    }
}
