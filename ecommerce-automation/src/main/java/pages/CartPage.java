package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * CartPage contains all elements and actions for the shopping cart page.
 */
public class CartPage extends BasePage {
    
    // Page Elements
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;
    
    @FindBy(css = ".inventory_item_name")
    private List<WebElement> cartItemNames;
    
    @FindBy(css = ".inventory_item_price")
    private List<WebElement> cartItemPrices;
    
    @FindBy(css = ".cart_quantity")
    private List<WebElement> cartItemQuantities;
    
    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;
    
    @FindBy(id = "checkout")
    private WebElement checkoutButton;
    
    @FindBy(css = ".cart_button")
    private List<WebElement> removeButtons;
    
    // Constructor
    public CartPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Get cart page title
     * @return Page title
     */
    public String getPageTitle() {
        String title = getText(pageTitle);
        logger.info("Cart page title: " + title);
        return title;
    }
    
    /**
     * Check if cart page is displayed
     * @return true if page is visible
     */
    public boolean isCartPageDisplayed() {
        return isElementDisplayed(pageTitle);
    }
    
    /**
     * Get number of items in cart
     * @return Item count
     */
    public int getCartItemCount() {
        int count = cartItems.size();
        logger.info("Number of items in cart: " + count);
        return count;
    }
    
    /**
     * Get list of product names in cart
     * @return List of product names
     */
    public List<String> getCartItemNames() {
        return cartItemNames.stream()
                .map(WebElement::getText)
                .toList();
    }
    
    /**
     * Get list of product prices in cart
     * @return List of prices
     */
    public List<String> getCartItemPrices() {
        return cartItemPrices.stream()
                .map(WebElement::getText)
                .toList();
    }
    
    /**
     * Check if product is in cart
     * @param productName Name of the product
     * @return true if product is in cart
     */
    public boolean isProductInCart(String productName) {
        return getCartItemNames().contains(productName);
    }
    
    /**
     * Remove item from cart by index
     * @param index Index of the item (0-based)
     */
    public void removeItemByIndex(int index) {
        click(removeButtons.get(index));
        logger.info("Removed item at index: " + index);
    }
    
    /**
     * Click continue shopping button
     * @return ProductsPage object
     */
    public ProductsPage continueShopping() {
        click(continueShoppingButton);
        logger.info("Clicked continue shopping");
        return new ProductsPage(driver);
    }
    
    /**
     * Click checkout button
     * @return CheckoutPage object
     */
    public CheckoutPage proceedToCheckout() {
        click(checkoutButton);
        logger.info("Proceeded to checkout");
        return new CheckoutPage(driver);
    }
    
    /**
     * Check if cart is empty
     * @return true if cart has no items
     */
    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }
    
    /**
     * Get total number of remove buttons
     * @return Number of remove buttons
     */
    public int getRemoveButtonCount() {
        return removeButtons.size();
    }
}
