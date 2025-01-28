package Listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import Utilities.ExtentReportManager;
import com.aventstack.extentreports.ExtentTest;

import java.util.Arrays;

public class TestNGListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.initReport(); // Initialize before tests run
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Create a test entry in the report
        ExtentTest test = ExtentReportManager.createTest(result.getMethod().getMethodName(), 
                                "Executing: " + result.getMethod().getDescription());

        // Assign TestNG groups as categories
        String[] groups = result.getMethod().getGroups();
        if (groups.length > 0) {
            Arrays.stream(groups).forEach(test::assignCategory);
        }

        test.info("Test Started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.pass("Test Passed ✅");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.fail("Test Failed ❌: " + result.getThrowable().getMessage());
            test.fail(result.getThrowable()); // Logs the complete stack trace
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.skip("Test Skipped ⚠️: " + result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReport(); // Flush after all tests complete
    }
}
