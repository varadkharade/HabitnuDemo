package com.PageClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.PageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Set;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(HomePage.class);

    // Locators
    private By titleLocator = By.tagName("h1");
    private By destinationLinkLocator = By.linkText("destination of the week! The Beach!");
    private By departureCityDropdownLocator = By.name("fromPort");
    private By destinationCityDropdownLocator = By.name("toPort");
    private By findFlightsButtonLocator = By.xpath("//input[@type='submit']");

    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Navigate to Home Page
    public void navigateToHomePage(String url) {
        try {
            driver.get(url);
            logger.info("Navigated to Home Page: " + url);
        } catch (Exception e) {
            logger.error("Failed to navigate to Home Page: " + url, e);
        }
    }

    // Verify Home Page Title
    public boolean isHomePageTitleDisplayed() {
        try {
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(titleLocator));
            boolean isDisplayed = titleElement.getText().equals("Welcome to the Simple Travel Agency!");
            logger.info("Home Page title is displayed: " + isDisplayed);
            return isDisplayed;
        } catch (NoSuchElementException | TimeoutException e) {
            logger.error("Home Page title is not displayed", e);
            return false;
        }
    }

    // Click on Destination of the Week Link
    public void clickDestinationOfTheWeekLink() {
        try {
            WebElement destinationLink = wait.until(ExpectedConditions.elementToBeClickable(destinationLinkLocator));
            String originalWindow = driver.getWindowHandle();
            Set<String> originalWindows = driver.getWindowHandles();
            destinationLink.click();
            logger.info("Clicked on 'destination of the week! The Beach!' link");

            // Wait for new tab or window
            // wait.until(ExpectedConditions.numberOfWindowsToBe(originalWindows.size() + 1));
            Set<String> allWindows = driver.getWindowHandles();

            if (allWindows.size() > originalWindows.size()) {
                // New tab opened
                for (String window : allWindows) {
                    if (!originalWindows.contains(window)) {
                        driver.switchTo().window(window);
                        logger.info("Switched to new tab/window");
                        break;
                    }
                }

                // Verify URL contains 'vacation'
                if (driver.getCurrentUrl().contains("vacation")) {
                    logger.info("New tab URL contains 'vacation'");
                } else {
                    logger.warn("New tab URL does not contain 'vacation'");
                }

                // Close new tab and switch back to original window
                driver.close();
                driver.switchTo().window(originalWindow);
                logger.info("Closed new tab and switched back to Home Page tab");

            } else {
                // URL opened in the same tab
                wait.until(ExpectedConditions.urlContains("vacation"));
                if (driver.getCurrentUrl().contains("vacation")) {
                    logger.info("URL contains 'vacation'");
                } else {
                    logger.warn("URL does not contain 'vacation'");
                }

                // Navigate back to Home Page
                driver.navigate().back();
                logger.info("Navigated back to Home Page");
            }

        } catch (NoSuchElementException | TimeoutException e) {
            logger.error("Failed to click on 'destination of the week! The Beach!' link", e);
        }
    }

    // Find Flight
    public void findFlight(String departureCity, String destinationCity) {
        try {
            // Select departure city
            WebElement departureCityDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(departureCityDropdownLocator));
            Select departureSelect = new Select(departureCityDropdown);
            departureSelect.selectByVisibleText(departureCity);
            logger.info("Selected departure city: " + departureCity);

            // Select destination city
            WebElement destinationCityDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(destinationCityDropdownLocator));
            Select destinationSelect = new Select(destinationCityDropdown);
            destinationSelect.selectByVisibleText(destinationCity);
            logger.info("Selected destination city: " + destinationCity);

            // Click Find Flights button
            WebElement findFlightsButton = wait.until(ExpectedConditions.elementToBeClickable(findFlightsButtonLocator));
            findFlightsButton.click();
            logger.info("Clicked on 'Find Flights' button");
        } catch (NoSuchElementException | TimeoutException e) {
            logger.error("Failed to find flight", e);
        } 
    }
}