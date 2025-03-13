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

public class PurchaseConfirmationPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(PurchaseConfirmationPage.class);

    // Locators
    private By pageTitleLocator = By.tagName("h1");
    private By idTableLocator = By.cssSelector("table tbody tr td:nth-child(2)");

    // Constructor
    public PurchaseConfirmationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Verify Page Title
    public boolean isPageTitleDisplayed() {
        try {
            WebElement pageTitleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitleLocator));
            boolean isDisplayed = pageTitleElement.getText().equals("Thank you for your purchase today!");
            logger.info("Page title is displayed: " + isDisplayed);
            return isDisplayed;
        } catch (NoSuchElementException | TimeoutException e) {
            logger.error("Page title is not displayed", e);
            return false;
        }
    }

    // Store and Print ID
    public void storeAndPrintId() {
        try {
            WebElement idElement = wait.until(ExpectedConditions.visibilityOfElementLocated(idTableLocator));
            String idValue = idElement.getText();
            logger.info("ID value: " + idValue);
            System.out.println("ID: " + idValue);
        } catch (NoSuchElementException | TimeoutException e) {
            logger.error("Failed to retrieve ID value", e);
        }
    }
}