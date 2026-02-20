package utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {
	
	private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = "screenshots/";
    
    /**
     * Capture screenshot and save to file
     * @param driver WebDriver instance
     * @param testName Name of the test
     * @return Path to saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = SCREENSHOT_DIR + fileName;
        
        try {
            // Create screenshots directory if it doesn't exist
            File directory = new File(SCREENSHOT_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Capture screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            
            // Copy screenshot to destination
            FileUtils.copyFile(srcFile, destFile);
            logger.info("Screenshot captured: " + filePath);
            
            return destFile.getAbsolutePath();
            
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
