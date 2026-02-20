package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
	
	private static ExtentReports extent;
	
	public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

	 // Create and configure ExtentReports instance
    private static void createInstance() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportPath = "reports/ExtentReport_" + timestamp + ".html";
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        
        // Configure report theme and settings
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("E-Commerce Automation Report");
        sparkReporter.config().setReportName("Test Execution Report");
        sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // Set system information
        extent.setSystemInfo("Application", "E-Commerce Website");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
    }
    
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
