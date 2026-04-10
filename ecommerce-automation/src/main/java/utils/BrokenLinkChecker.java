package utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BrokenLinkChecker {
	private static final Logger logger = LogManager.getLogger(BrokenLinkChecker.class);
	private WebDriver driver;
	private Set<String> visitedLinks;	
	
	// Link categorization
    private List<LinkStatus> brokenLinks = new ArrayList<>();
    private List<LinkStatus> workingLinks = new ArrayList<>();
    private List<String> skippedLinks = new ArrayList<>();
    
    public BrokenLinkChecker(WebDriver driver) {
        this.driver = driver;
        this.visitedLinks = new HashSet<>();
    }
    
    /**
     * Main method to validate all links on current page
     */
    public void validateAllLinks() {
        String pageTitle = driver.getTitle();
        String pageURL = driver.getCurrentUrl();
        
        logger.info("═══════════════════════════════════════════════════════");
        logger.info("Page Title: " + pageTitle);
        logger.info("Page URL: " + pageURL);
        logger.info("═══════════════════════════════════════════════════════");
        
        System.out.println("\nVALIDATING LINKS ON: " + pageTitle);
        System.out.println("URL: " + pageURL + "\n");
        
        // Extract all links
        List<WebElement> allLinks = driver.findElements(By.tagName("a"));
        logger.info("Total anchor tags found: " + allLinks.size());
        
        // Extract unique URLs
        List<String> urlsToCheck = extractUniqueURLs(allLinks);
        
        System.out.println("Total Links to Validate: " + urlsToCheck.size());
        System.out.println("Starting validation...\n");
        
        // Validate links (can be parallelized for speed)
        validateLinksSequentially(urlsToCheck);
        
        // Print Report
        printDetailedReport(pageTitle);
    }
    
    /**
     * Extract unique URLs from WebElements
     */
    private List<String> extractUniqueURLs(List<WebElement> links) {
        List<String> urls = new ArrayList<>();
        
        for (WebElement link : links) {
            String url = link.getAttribute("href");
            
            // Skip invalid URLs
            if (url == null || url.isEmpty()) {
                skippedLinks.add("Empty URL");
                continue;
            }
            
            if (url.startsWith("javascript:") || url.startsWith("mailto:") || url.startsWith("#")) {
                skippedLinks.add(url + " (Non-HTTP)");
                continue;
            }
            
            // Add only if not already checked
            if (!visitedLinks.contains(url)) {
                visitedLinks.add(url);
                urls.add(url);
            }
        }
        return urls;
    }
    
    /**
     * Validate links sequentially
     */
    private void validateLinksSequentially(List<String> urls) {
        int count = 0;
        
        for (String url : urls) {
            count++;
            System.out.print("[" + count + "/" + urls.size() + "] Checking: " + url + " ... ");
            
            LinkStatus status = checkLink(url);
            
            if (status.isBroken()) {
                brokenLinks.add(status);
                System.out.println("BROKEN (" + status.getStatusCode() + ")");
            } else {
                workingLinks.add(status);
                System.out.println("OK (" + status.getStatusCode() + ")");
            }
        }
    }
    
    /**
     * Validate links in parallel (faster for many links)
     */
    private void validateLinksParallel(List<String> urls) {
        ExecutorService executor = Executors.newFixedThreadPool(10); // 10 parallel threads
        List<Future<LinkStatus>> futures = new ArrayList<>();
        
        for (String url : urls) {
            Future<LinkStatus> future = executor.submit(() -> checkLink(url));
            futures.add(future);
        }
        
        // Collect results
        for (int i = 0; i < futures.size(); i++) {
            try {
                LinkStatus status = futures.get(i).get();
                
                if (status.isBroken()) {
                    brokenLinks.add(status);
                } else {
                    workingLinks.add(status);
                }
                
                System.out.println("[" + (i + 1) + "/" + urls.size() + "] " + status.getUrl() + 
                                 " → " + (status.isBroken() ? "❌" : "✅") + " (" + status.getStatusCode() + ")");
                
            } catch (Exception e) {
                logger.error("Error processing URL: " + e.getMessage());
            }
        }
        
        executor.shutdown();
    }
    
    /**
     * Check individual link status
     */
    private LinkStatus checkLink(String urlString) {
        HttpURLConnection connection = null;
        
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            connection.connect();
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();
            
            return new LinkStatus(urlString, responseCode, responseMessage);
            
        } catch (Exception e) {
            logger.error("Error checking URL: " + urlString + " - " + e.getMessage());
            return new LinkStatus(urlString, -1, "Connection Error: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    /**
     * Print status code breakdown
     */
    private void printStatusCodeBreakdown() {
        Map<Integer, Integer> statusCodeCount = new HashMap<>();
        
        // Count broken links by status code
        for (LinkStatus link : brokenLinks) {
            int code = link.getStatusCode();
            statusCodeCount.put(code, statusCodeCount.getOrDefault(code, 0) + 1);
        }
        
        if (!statusCodeCount.isEmpty()) {
            System.out.println("\n📈 STATUS CODE BREAKDOWN:");
            System.out.println("─────────────────────────────────────────────────────");
            
            statusCodeCount.forEach((code, count) -> {
                String meaning = getStatusCodeMeaning(code);
                System.out.println(code + " (" + meaning + "): " + count + " link(s)");
            });
        }
    }
    
    /**
     * Get human-readable status code meaning
     */
    private String getStatusCodeMeaning(int code) {
        switch (code) {
            case 200: return "OK";
            case 301: return "Moved Permanently";
            case 302: return "Found (Redirect)";
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 500: return "Internal Server Error";
            case 503: return "Service Unavailable";
            case -1: return "Connection Error";
            default: return "Unknown";
        }
    }
    
    /**
     * Inner class to store link status information
     */
    private static class LinkStatus {
        private String url;
        private int statusCode;
        private String responseMessage;
        
        public LinkStatus(String url, int statusCode, String responseMessage) {
            this.url = url;
            this.statusCode = statusCode;
            this.responseMessage = responseMessage;
        }
        
        public boolean isBroken() {
            return statusCode >= 400 || statusCode == -1;
        }
        
        public String getUrl() { return url; }
        public int getStatusCode() { return statusCode; }
        public String getResponseMessage() { return responseMessage; }
    }
    
    /**
     * Print detailed report
     */
    private void printDetailedReport(String pageTitle) {
        System.out.println("\n\n═══════════════════════════════════════════════════════");
        System.out.println("      BROKEN LINKS VALIDATION REPORT");
        System.out.println("      Page: " + pageTitle);
        System.out.println("═══════════════════════════════════════════════════════");
        
        int total = workingLinks.size() + brokenLinks.size();
        
        System.out.println("\nSUMMARY:");
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println("Total Links Checked:    " + total);
        System.out.println("Working Links:       " + workingLinks.size() + 
                         " (" + String.format("%.1f", (workingLinks.size() * 100.0 / total)) + "%)");
        System.out.println("Broken Links:        " + brokenLinks.size() + 
                         " (" + String.format("%.1f", (brokenLinks.size() * 100.0 / total)) + "%)");
        System.out.println("Skipped Links:      " + skippedLinks.size());
        
        // Broken Links Details
        if (!brokenLinks.isEmpty()) {
            System.out.println("\nBROKEN LINKS DETAILS:");
            System.out.println("─────────────────────────────────────────────────────");
            
            for (int i = 0; i < brokenLinks.size(); i++) {
                LinkStatus link = brokenLinks.get(i);
                System.out.println((i + 1) + ". URL: " + link.getUrl());
                System.out.println("   Status Code: " + link.getStatusCode());
                System.out.println("   Message: " + link.getResponseMessage());
                System.out.println();
            }
        }
        
        // Status Code Breakdown
        printStatusCodeBreakdown();
        
        // Final Result
        System.out.println("\n═══════════════════════════════════════════════════════");
        if (brokenLinks.isEmpty()) {
            System.out.println("RESULT: ALL LINKS ARE WORKING!");
        } else {
            System.out.println("RESULT: " + brokenLinks.size() + " BROKEN LINK(S) FOUND!");
        }
        System.out.println("═══════════════════════════════════════════════════════\n");
    }
}
