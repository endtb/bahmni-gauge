package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Gauge;
import com.thoughtworks.gauge.Step;

import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.home.HomePage;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

public class HomeSpec {
    private final WebDriver driver;
    private HomePage homePage;

    public HomeSpec() {
        this.driver = DriverFactory.getDriver();
    }

    @BeforeClassSteps
    public void waitForAppReady() {
        BahmniPage.waitForSpinner(driver);
    }

    @Step("Click on registration app")
    public void goToRegistrationPage() {
        homePage = PageFactory.getHomePage();
        homePage.clickRegistrationApp();
        BahmniPage.waitForSpinner(driver);
    }

    @Step("Click on programs app")
    public void goToProgramsPage() {
        homePage = PageFactory.getHomePage();
        homePage.clickProgramsApp();
    }

    @Step("Click on clinical app")
    public void goToClinicalPage() {
        homePage = PageFactory.getHomePage();
        homePage.clickClinicalApp();
    }

    @Step("Click on inpatient app")
    public void goToInpatientPage() {
        homePage = PageFactory.getHomePage();
        homePage.clickInpatientApp();
    }

    @Step("Click on admin app")
    public void goToAdminPage() {
        homePage = PageFactory.getHomePage();
        homePage.clickAdminApp();
    }

    @Step("Click on orders app")
    public void goToOrdersPage() {
        homePage = PageFactory.getHomePage();
        homePage.clickOrdersApp();
    }

    @Step("Click on <apps> app")
    public void goToApp(String app) {
        homePage = PageFactory.getHomePage();
        switch (app.toLowerCase()) {
            case "registration":
                homePage.clickRegistrationApp();
            case "programs":
                homePage.clickProgramsApp();
            case "clinical":
                homePage.clickClinicalApp();
            case "inpatient":
                homePage.clickInpatientApp();
            case "radiology upload":
                homePage.clickRadiologyUploadApp();
            case "patient documents":
                Gauge.writeMessage(app + " not implemented");
            case "admin":
                homePage.clickAdminApp();
            case "reports":
                Gauge.writeMessage(app + " not implemented");
            case "orders":
                homePage.clickOrdersApp();
            case "implementor interface":
                Gauge.writeMessage(app + " not implemented");
        }
        homePage.clickOrdersApp();
    }

    @Step("Click on radiology upload app")
    public void goToRadiologyUploadPage() {
        homePage = PageFactory.getHomePage();
        homePage.clickRadiologyUploadApp();
    }

    @Step("Logout the user")
    public void logout() {
        //new BahmniPage().waitForElementOnPage(driver, ".i.icon-bahmni-org.bahmi.gauge.possible.registration");
        homePage = PageFactory.getHomePage();
        homePage.logout();
    }

    @Step("Verify Login Page")
    public void validateLogin() {
        homePage = PageFactory.getHomePage();
        Assert.assertTrue(homePage.registration.isDisplayed());
        Assert.assertTrue(homePage.admin.isDisplayed());
        Assert.assertTrue(homePage.exports.isDisplayed());
    }

}
