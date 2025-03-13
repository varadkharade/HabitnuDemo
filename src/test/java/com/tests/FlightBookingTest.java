package com.tests;

import com.PageClasses.HomePage;
import com.PageClasses.PurchasePage;
import com.PageClasses.PurchaseConfirmationPage;
import com.utils.ExcelUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightBookingTest {
    private WebDriver driver;
    private HomePage homePage;
    private PurchasePage purchasePage;
    private PurchaseConfirmationPage purchaseConfirmationPage;
    private static final Logger logger = LogManager.getLogger(FlightBookingTest.class);

    @BeforeClass
    public void setUp() {
        // Set up WebDriver (assuming ChromeDriver is in the system path)
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        homePage = new HomePage(driver);
        purchasePage = new PurchasePage(driver);
        purchaseConfirmationPage = new PurchaseConfirmationPage(driver);
        logger.info("Setup completed");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Teardown completed");
        }
    }

    @Test(dataProvider = "flightData")
    public void testFlightBooking(String url, String departureCity, String destinationCity) {
        try {
            // Step 1: Navigate to Home Page
            homePage.navigateToHomePage(url);
            Assert.assertTrue(homePage.isHomePageTitleDisplayed(), "Home Page title is not displayed");

            // Step 2: Click on Destination of the Week Link
            homePage.clickDestinationOfTheWeekLink();

            // Step 3: Find Flight
            homePage.findFlight(departureCity, destinationCity);

            // Step 4: Verify Purchase Page Title
            Assert.assertTrue(purchasePage.isPageTitleDisplayed(departureCity, destinationCity), "Purchase Page title is not displayed");

            // Step 5: Select Flight with Lowest Price
            purchasePage.selectFlightWithLowestPrice();

            // Step 6: Verify Navigation to Purchase Page
            Assert.assertTrue(purchasePage.isPurchasePageDisplayed(), "Purchase Page is not displayed");

            // Step 7: Verify Total Cost Field
            Assert.assertTrue(purchasePage.isTotalCostFieldDisplayed(), "Total Cost field is not displayed");

            // Step 8: Click on Purchase Flight Button
            purchasePage.clickPurchaseFlightButton();

            // Step 9: Verify Purchase Confirmation Page Title
            Assert.assertTrue(purchaseConfirmationPage.isPageTitleDisplayed(), "Purchase Confirmation Page title is not displayed");

            // Step 10: Store and Print ID
            purchaseConfirmationPage.storeAndPrintId();

        } catch (Exception e) {
            logger.error("Test failed", e);
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @DataProvider(name = "flightData")
    public Object[][] flightData() throws IOException {
        String excelFilePath = "C:\\Users\\varad\\OneDrive\\Desktop\\Habitnu\\demo\\src\\main\\resources\\flightData.xlsx";
        String sheetName = "Sheet1";
        ExcelUtils excelUtils = new ExcelUtils(excelFilePath, sheetName);
        Object[][] data = excelUtils.getTestData();
        excelUtils.close();
        return data;
    }

    public void captureScreenshot(String testName) {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String screenshotName = testName + "_" + timestamp + ".png";
        File screenshotsDir = new File("screenshots");
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs();
        }
        File destinationFile = new File(screenshotsDir, screenshotName);
        try {
            FileHandler.copy(screenshotFile, destinationFile);
            logger.info("Screenshot captured: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to capture screenshot", e);
        }
    }
}