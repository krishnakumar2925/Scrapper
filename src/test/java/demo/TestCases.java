package demo;

import java.io.File;
import java.util.logging.Level;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import demo.wrappers.Wrappers;


public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */

     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */

     @Test(enabled=true)
     public void testCase01() throws InterruptedException{
        Wrappers wrap=new Wrappers(driver);
        System.out.println("Testcase1: started");
        //navigate to application
        wrap.launchApplication("https://www.scrapethissite.com/pages/");
        //navigate to the page
        wrap.navigateTo("Hockey Teams: Forms, Searching and Pagination");
        //store to json
        wrap.storeWinPercentage();
        System.out.println("Testcase1: ended");
     }

     @Test
     public void testCase02() throws InterruptedException{
        Wrappers wrap=new Wrappers(driver);
        System.out.println("Testcase2: started");
        //navigate to application
        wrap.launchApplication("https://www.scrapethissite.com/pages/");
        //navigate to the page
        wrap.navigateTo("Oscar Winning Films: AJAX and Javascript");
        //store to json
        wrap.oscarMovies();
        File jsonFile=new File("output/oscar-winner-data.json");
        Assert.assertTrue(jsonFile.exists(),"JSON file does not exist!");
        Assert.assertTrue(jsonFile.length() > 0, "JSON file is empty!");
        System.out.println("Testcase2: ended");


     }
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }
}