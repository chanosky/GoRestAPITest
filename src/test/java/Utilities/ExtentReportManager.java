package Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ExtentReportManager {
    private static ExtentReports extent;

    // Initialize Extent Reports
    public static void initReport() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportFilePath = "test-output/GoRestAPITests/ExtentReport_" + timestamp + ".html";
            
            // Use ExtentSparkReporter as a newer alternative
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);
            
            // Apply default settings
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setReportName("GoRest API Test Report");
            sparkReporter.config().setDocumentTitle("GoRest API Test Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

        }
    }

    // ThreadLocal to ensure that each thread has its own ExtentTest instance
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // Create test entry
    public static ExtentTest createTest(String testName, String description) {
        if (extent == null) {
            initReport();  // Ensure report is initialized
        }
        ExtentTest extentTest = extent.createTest(testName, description);     
        test.set(extentTest);
        return extentTest;
    }

    // Get test instance
    public static ExtentTest getTest() {
        return test.get();
    }

    // Flush report data
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
