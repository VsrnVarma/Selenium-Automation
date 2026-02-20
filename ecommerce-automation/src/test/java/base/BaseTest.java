package base;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;
import utils.ExtentManager;

/**
 * BaseTest class handles browser initialization, configuration, and cleanup.
 * All test classes should extend this class.
 */
public class BaseTest {
	
	protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected static ExtentReports extent = ExtentManager.getInstance();
    protected static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
	
	/**
     * Setup method runs before each test method
     * Initializes browser and navigates to base URL
     */
    @BeforeMethod
    public void setUp() {
        logger.info("===== Starting Test Execution =====");
        
        String browser = System.getProperty("browser");
        if (browser == null || browser.isEmpty()) {
            browser = "chrome"; // Default browser
        }
        
        logger.info("Initializing browser: " + browser);
        
        driver = initializeDriver(browser);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        String baseUrl = ConfigReader.getProperty("base.url");
        driver.get(baseUrl);
        logger.info("Navigated to: " + baseUrl);
    }
    
    // Initialize WebDriver based on browser parameter
    private WebDriver initializeDriver(String browser) {
        WebDriver driver = null;
        String headless = System.getProperty("headless", "false");
        
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless.equalsIgnoreCase("true")) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("incognito");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
                
            default:
                logger.error("Invalid browser specified: " + browser);
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        
        return driver;
    }
    
    /**
     * Teardown method runs after each test method
     * Closes browser and cleans up resources
     */
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            logger.info("Closing browser");
            driver.quit();
        }
        logger.info("===== Test Execution Completed =====\n");
    }
    
    // Get current WebDriver instance
    public WebDriver getDriver() {
        return driver;
    }
}
