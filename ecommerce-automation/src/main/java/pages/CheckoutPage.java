package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * CheckoutPage handles the checkout process including shipping information.
 */
public class CheckoutPage extends BasePage {
    
    // Step 1: Checkout Information Elements
    @FindBy(id = "first-name")
    private WebElement firstNameField;
    
    @FindBy(id = "last-name")
    private WebElement lastNameField;
    
    @FindBy(id = "postal-code")
    private WebElement postalCodeField;
    
    @FindBy(id = "continue")
    private WebElement continueButton;
    
    @FindBy(id = "cancel")
    private WebElement cancelButton;
    
    @FindBy(css = "h3[data-test='error']")
    private WebElement errorMessage;
    
    // Step 2: Checkout Overview Elements
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;
    
    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;
    
    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;
    
    @FindBy(id = "finish")
    private WebElement finishButton;
    
    // Step 3: Checkout Complete Elements
    @FindBy(className = "complete-header")
    private WebElement completeHeader;
    
    @FindBy(className = "complete-text")
    private WebElement completeText;
    
    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;
    
    // Constructor
    public CheckoutPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Enter first name
     * @param firstName First name
     */
    public void enterFirstName(String firstName) {
        sendKeys(firstNameField, firstName);
        logger.info("Entered first name: " + firstName);
    }
    
    /**
     * Enter last name
     * @param lastName Last name
     */
    public void enterLastName(String lastName) {
        sendKeys(lastNameField, lastName);
        logger.info("Entered last name: " + lastName);
    }
    
    /**
     * Enter postal code
     * @param postalCode Postal code
     */
    public void enterPostalCode(String postalCode) {
        sendKeys(postalCodeField, postalCode);
        logger.info("Entered postal code: " + postalCode);
    }
    
    /**
     * Fill checkout information
     * @param firstName First name
     * @param lastName Last name
     * @param postalCode Postal code
     */
    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        logger.info("Filled checkout information");
    }
    
    /**
     * Click continue button
     */
    public void clickContinue() {
        click(continueButton);
        logger.info("Clicked continue button");
    }
    
    /**
     * Click cancel button
     */
    public void clickCancel() {
        click(cancelButton);
        logger.info("Clicked cancel button");
    }
    
    /**
     * Get error message
     * @return Error message text
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }
    
    /**
     * Get page title
     * @return Page title
     */
    public String getPageTitle() {
        return getText(pageTitle);
    }
    
    /**
     * Get subtotal amount
     * @return Subtotal as string
     */
    public String getSubtotal() {
        String subtotal = getText(subtotalLabel);
        logger.info("Subtotal: " + subtotal);
        return subtotal;
    }
    
    /**
     * Get tax amount
     * @return Tax as string
     */
    public String getTax() {
        String tax = getText(taxLabel);
        logger.info("Tax: " + tax);
        return tax;
    }
    
    /**
     * Get total amount
     * @return Total as string
     */
    public String getTotal() {
        String total = getText(totalLabel);
        logger.info("Total: " + total);
        return total;
    }
    
    /**
     * Click finish button to complete order
     */
    public void clickFinish() {
        click(finishButton);
        logger.info("Clicked finish button");
    }
    
    /**
     * Get order completion header
     * @return Completion header text
     */
    public String getCompleteHeader() {
        String header = getText(completeHeader);
        logger.info("Order complete header: " + header);
        return header;
    }
    
    /**
     * Get order completion message
     * @return Completion message
     */
    public String getCompleteText() {
        return getText(completeText);
    }
    
    /**
     * Check if order is completed
     * @return true if order complete page is displayed
     */
    public boolean isOrderCompleted() {
        return isElementDisplayed(completeHeader);
    }
    
    /**
     * Click back to products button
     * @return ProductsPage object
     */
    public ProductsPage backToProducts() {
        click(backToProductsButton);
        logger.info("Navigated back to products");
        return new ProductsPage(driver);
    }
}