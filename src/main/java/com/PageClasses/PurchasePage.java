package com.PageClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.PageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;

public class PurchasePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(PurchasePage.class);

    // Locators
    private By pageTitleLocator = By.tagName("h3");
    private By flightRowsLocator = By.cssSelector("table tbody tr");
    private By chooseFlightButtonLocator = By.cssSelector("input[type='submit']");
    private By purchasePageTitleLocator = By.tagName("h2");
    private By totalCostFieldLocator = By.xpath("//em");
    private By purchaseFlightButtonLocator = By.cssSelector("input[type='submit']");

    // Constructor
    public PurchasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Verify Page Title
    public boolean isPageTitleDisplayed(String departureCity, String destinationCity) {
        try {
            WebElement pageTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitleLocator));
            String expectedTitle = "Flights from " + departureCity + " to " + destinationCity + ":";
            boolean isDisplayed = pageTitleElement.getText().equals(expectedTitle);
            logger.info("Page title is displayed: " + isDisplayed);
            return isDisplayed;
        } catch (NoSuchElementException | TimeoutException e) {
            logger.error("Page title is not displayed", e);
            return false;
        }
    }

    // Select Flight with Lowest Price
    public void selectFlightWithLowestPrice() {
        try {
            List<WebElement> flightRows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(flightRowsLocator));
            WebElement lowestPriceRow = null;
            double lowestPrice = Double.MAX_VALUE;

            for (WebElement row : flightRows) {
                String priceText = row.findElement(By.cssSelector("td:nth-child(7)")).getText().replace("$", "");
                double price = Double.parseDouble(priceText);
                if (price < lowestPrice) {
                    lowestPrice = price;
                    lowestPriceRow = row;
                }
            }

            if (lowestPriceRow != null) {
                WebElement chooseFlightButton = lowestPriceRow.findElement(chooseFlightButtonLocator);
                chooseFlightButton.click();
                logger.info("Selected flight with lowest price: $" + lowestPrice);
            } else {
                logger.warn("No flights found to select");
            }

        } catch (NoSuchElementException | TimeoutException e) {
            logger.error("Failed to select flight with lowest price", e);
        }
    }

    // Verify Navigation to Purchase Page
    public boolean isPurchasePageDisplayed() {
        try {
            WebElement purchasePageTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(purchasePageTitleLocator));
            boolean isDisplayed = purchasePageTitleElement.getText().equals("Your flight from TLV to SFO has been reserved.");
            logger.info("Purchase page is displayed: " + isDisplayed);
            return isDisplayed;
        } catch (NoSuchElementException | TimeoutException e) {
            logger.error("Purchase page is not displayed", e);
            return false;
        }
    }

    // Verify Total Cost Field
    public boolean isTotalCostFieldDisplayed() {
        try {
            WebElement totalCostField = wait.until(ExpectedConditions.visibilityOfElementLocated(totalCostFieldLocator));
            boolean isDisplayed = totalCostField.getText().matches("\\d+\\.\\d{2}");
            logger.info("Total Cost field is displayed with correct format: " + isDisplayed);
            return isDisplayed;
        } catch (NoSuchElementException | TimeoutException e) {
            logger.error("Total Cost field is not displayed", e);
            return false;
        }
    }

    // Click on Purchase Flight Button
    public void clickPurchaseFlightButton() {
        try {
            WebElement purchaseFlightButton = wait.until(ExpectedConditions.elementToBeClickable(purchaseFlightButtonLocator));
            purchaseFlightButton.click();
            logger.info("Clicked on 'Purchase flight' button");
        } catch (NoSuchElementException | TimeoutException e) {
            logger.error("Failed to click on 'Purchase flight' button", e);
        }
    }
}