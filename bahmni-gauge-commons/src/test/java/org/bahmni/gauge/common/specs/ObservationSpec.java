package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.clinical.ConsultationPage;
import org.bahmni.gauge.common.clinical.DashboardPage;
import org.bahmni.gauge.common.clinical.ObservationsPage;
import org.bahmni.gauge.common.clinical.domain.DrugOrder;
import org.bahmni.gauge.common.clinical.domain.ObservationForm;
import org.bahmni.gauge.common.program.domain.PatientProgram;
import org.bahmni.gauge.common.registration.domain.Patient;
import org.bahmni.gauge.rest.BahmniRestClient;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObservationSpec extends BahmniPage {

    public ObservationSpec() {
        driver = DriverFactory.getDriver();
    }

    @BeforeClassSteps
    public void waitForAppReady() {
        BahmniPage.waitForSpinner(DriverFactory.getDriver());
    }


    @Step("Select the template <template> from on the observation page")
    public void clickOnTreatmentEnrollment(String template) throws InterruptedException {
        ObservationsPage observationsPage = PageFactory.get(ObservationsPage.class);
        observationsPage.selectTemplate(template.replace(" ", "_"));
        waitForAppReady();
    }

    @Step("Enter values in <template> template <table>")
    public void enterTemplateValues(String template, Table table) throws InterruptedException {
        ObservationForm observationForm = null;
        if(template.toLowerCase().contains("obstetrics"))
            observationForm = new ObservationForm(driver.findElement(By.cssSelector("#concept-set-4")));
        observationForm.fillUp(table);
        waitForAppReady();
    }

    @Step("Create a <formName> form with following data <table>")
    public void createForm(String formName, Table table) {
        Patient patient = new BahmniPage().getPatientFromSpecStore();
        PatientProgram patientProgram = new BahmniPage().getPatientProgramFromSpecStore();

        Map<String, Object> formAttributes = new HashMap<>();
        formAttributes.put("patientUuid", patient.getUuid());
        formAttributes.put("patientProgramUuid", patientProgram.getPatientProgramUuid());
        formAttributes.putAll(transformTableToMap(table));

        BahmniRestClient.get().createForm(formName + ".ftl", formAttributes);

    }

    private Map<String, String> transformTableToMap(Table table) {

        Map<String, String> formVariables = new HashMap<>();
        List<String> columnNames = table.getColumnNames();

        for (String columnName : columnNames) {
            formVariables.put(columnName, table.getTableRows().get(0).getCell(columnName));
        }

        return formVariables;
    }

    @Step("Verify observations recorded under <formName>")
    public void verifyObservationsOnDashboard(String formName) {
        DashboardPage dashboardPage = PageFactory.get(DashboardPage.class);
        dashboardPage.validateObservationDisplayControl(formName);
    }

    @Step("Verify prescribed drugs on the dashboard page")
    public void verifyDrugsOnDashboard() {
        List<DrugOrder> drugOrder = (List<DrugOrder>) new BahmniPage().getDrugOrderFromSpecStore();
        DashboardPage dashboardPage = PageFactory.get(DashboardPage.class);
        for (DrugOrder drug : drugOrder)
            dashboardPage.validateDrugOrderDisplayControl(drug, "All active TB Drugs");
    }

    @Step("Verify display control <displayControlName> on dashboard, has the following details <table>")
    public void verifyDisplayControlContent(String displayControlName,Table table) {
        DashboardPage dashboardPage = PageFactory.get(DashboardPage.class);
        String displayControlText = dashboardPage.getDisplayControlText(displayControlName);
        for (String drugOrder : table.getColumnValues("details")) {
            Assert.assertTrue(stringDoesNotExist(drugOrder),displayControlText.contains(drugOrder));
        }
    }


    @Step("Verify the <template> concept set is <displayType>")
    public void verifyObservationFormContent(String displayType,String template) {
            DashboardPage dashboardPage = PageFactory.get(DashboardPage.class);
            if(displayType.toLowerCase().equals("not displayed"))
                Assert.assertFalse("Element "+template+"is displayed", dashboardPage.isDisplayed("#concept-set-4"));
            else
                Assert.assertTrue("Element "+template+"is not displayed", dashboardPage.isDisplayed("#concept-set-4"));
    }


    @Step("Close the app")
    public void closeApplication() {
        new BahmniPage().closeApp(driver);
    }

    private static String stringDoesNotExist(String content){
        return "String `"+content+"` does not exist";
    }

    @Step("Fill <Vitals> template with following observation details <table>")
    public void enterObservations(String template,Table table){
        ObservationsPage observationsPage=PageFactory.get(ObservationsPage.class);
        observationsPage.enterObservations(template,table);
//        storeObservationFormInSpecStore(observationForm);
    }

    @Step("Navigate back to program dashboard")
    public void navigateBackToDashboard(){
        ObservationsPage observationsPage=PageFactory.get(ObservationsPage.class);
        observationsPage.navigateToDashboard();
        waitForAppReady();
    }
}