package Utility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.logging.*;

public class BaseDriver {

    public static WebDriver driver;

    public static WebDriverWait wait;


    @BeforeClass(groups = {"Smoke Test","Login Test",
            "Logout Test","Regression Test","PatientManagement Test"})
    public void startUpOperations() {

        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.SEVERE);

        driver = new ChromeDriver();

        driver.manage().window().maximize();  // Ekranı maximum yapar
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(25));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(25));

        wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        LoginProcesses();

    }

    public void LoginProcesses() {

        SoftAssert _softAssert = new SoftAssert();
        LCE lce = new LCE();

        driver.get("https://openmrs.org/");

        lce.demoButton.click();

        wait.until(ExpectedConditions.elementToBeClickable(lce.selectLanguage));

        if (!(lce.flagImg.getAttribute("alt").equals("en"))) {
            System.out.println("Language : " + lce.flagImg.getAttribute("alt"));

            lce.selectLanguage.click();

            wait.until(ExpectedConditions.elementToBeClickable(lce.choiceLanguage));

            lce.choiceLanguage.click();

            System.out.println("Language : " + lce.flagImg.getAttribute("alt"));
        }

        wait.until(ExpectedConditions.visibilityOf(lce.exploreMRS2Button));

        Tools.JavaScriptClick(lce.exploreMRS2Button);

        wait.until(ExpectedConditions.elementToBeClickable(lce.demoMRS2Button));

        Tools.JavaScriptClick(lce.demoMRS2Button);

        wait.until(ExpectedConditions.urlContains("login"));

        lce.userName.sendKeys("admin");

        lce.password.sendKeys("Admin123");

        int random = Tools.randomGenerator(lce.sessionLocation.size());
        lce.sessionLocation.get(random).click();

        lce.loginButton.click();

        wait.until(ExpectedConditions.visibilityOf(lce.loggedMsg));

        _softAssert.assertTrue(lce.loggedMsg.getText().contains("Logged"), "Unable to Login");
        _softAssert.assertAll();
    }


    @AfterClass(groups = {"Smoke Test","Login Test",
            "Logout Test","Regression Test","PatientManagement Test"})
    public void closingOperations() {

        Tools.Wait(3);
        driver.quit();

    }
}
