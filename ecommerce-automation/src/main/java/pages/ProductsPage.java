package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;

public class ProductsPage extends BasePage {
	//private final WebDriver pageDriver;
    
    @FindBy(className = "title")
    private WebElement pageTitle;
    
    @FindBy(className = "shopping_cart_link")
    private WebElement shoppingCartIcon;
    
    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;
    
    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;
    
    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;
    
    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;
    
    @FindBy(className = "inventory_item")
    private List<WebElement> productItems;
    
    @FindBy(css = ".inventory_item_name")
    private List<WebElement> productNames;
    
    @FindBy(css = ".inventory_item_price")
    private List<WebElement> productPrices;
    
    public ProductsPage(WebDriver driver) {
        super(driver);
        //this.pageDriver = driver;  // Store local copy
        //logger.info("ProductsPage created with local driver copy");
    }
    
    public String getPageTitle() {
        String title = getText(pageTitle);
        logger.info("Products page title: " + title);
        return title;
    }
    
    public boolean isProductsPageDisplayed() {
        return isElementDisplayed(pageTitle);
    }
    
    public int getProductCount() {
        int count = productItems.size();
        logger.info("Number of products: " + count);
        return count;
    }
    
    public void addProductToCart(String productName) {
        try {
            String buttonId = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
            logger.info("Adding product: " + buttonId);
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement addButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id(buttonId))
            );
            
            addButton.click();
            Thread.sleep(300);
            
            logger.info("Product added successfully");
            
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            throw new RuntimeException("Could not add product: " + productName, e);
        }
    }
    
    public String getCartItemCount() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOf(cartBadge));
            return getText(cartBadge);
        } catch (Exception e) {
            return "0";
        }
    }
    
    public CartPage clickShoppingCart() {
        click(shoppingCartIcon);
        logger.info("Clicked shopping cart");
        return new CartPage(driver);
    }
    
    public void sortProducts(String sortOption) {
        selectDropdown(sortDropdown, sortOption);
    }
    
    public List<String> getProductNames() {
        return productNames.stream().map(WebElement::getText).toList();
    }
    
    public List<String> getProductPrices() {
        return productPrices.stream().map(WebElement::getText).toList();
    }
    
    public void clickProductByName(String productName) {
        WebElement product = productNames.stream()
                .filter(p -> p.getText().equals(productName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found: " + productName));
        click(product);
    }
    
    public void logout() {
        click(menuButton);
        click(logoutLink);
    }
}
