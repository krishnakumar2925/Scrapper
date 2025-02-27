package demo.wrappers;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     * 
     */
    WebDriver driver;

    public Wrappers(WebDriver driver){
        this.driver=driver;
        PageFactory.initElements(driver, this);
    }
    @FindBy(xpath="//td[contains(@class,'pct')]")
    List<WebElement> winPercentages;

    
    @FindBy(xpath="//a[@class='year-link']")
    List<WebElement> yearText;

    @FindBy(xpath="//tr[@class='film'][position()<=5]")
    List<WebElement> topMovies;

    By teamsName=By.xpath("./parent::tr//td[contains(@class,'name')]");
    By years=By.xpath("./parent::tr//td[contains(@class,'year')]");
    By table=By.xpath("//table[@class='table']");

    public void launchApplication(String Url){
        driver.get(Url);
    }
    public void navigateTo(String text){
        driver.findElement(By.linkText(text)).click();
        wait(text);

    }
    public void wait(String text){
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(3));
        wait.until(ExpectedConditions.titleContains(text));
    }

    public void storeWinPercentage() throws InterruptedException{
        ArrayList<HashMap<String, Object>> teamData=new ArrayList<>();
        long epochTime = System.currentTimeMillis();
        for(int i=0;i<3;i++){
            for(WebElement winPercentage:winPercentages){
                String winText = winPercentage.getText().trim();
                if (!winText.isEmpty()) {
                    try {
                        double winPercent = Double.parseDouble(winText);
                        if (winPercent < 0.40) {
                            HashMap<String, Object> teamInfo = new HashMap<>();
                            teamInfo.put("Epoch Time of Scrape", epochTime);
                            String teamName=winPercentage.findElement(teamsName).getText();
                            String year=winPercentage.findElement(years).getText();
                            teamInfo.put("Team Name",teamName.trim());
                            teamInfo.put("Year",year.trim());
                            teamInfo.put("Win", winPercent);
                            teamData.add(teamInfo);

                        }
                    }catch(Exception e){
                        System.out.println("Skipping invalid percentage: " + winText);
                    }
            }
        }
        driver.findElement(By.linkText(String.valueOf(i+2))).click();
        Thread.sleep(3000);
    }   
    saveToJson(teamData,"hockey-team-data.json");
}

public void oscarMovies() throws InterruptedException{
    ArrayList<HashMap<String, Object>> movieData=new ArrayList<>();
        long epochTime = System.currentTimeMillis();
    for(WebElement value:yearText){
        String currentYear=value.getText();
        value.click();
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(table));
        Thread.sleep(1000);
        for(WebElement movie:topMovies){
            HashMap<String, Object> movieInfo = new HashMap<>();
        
                String title = movie.findElement(By.xpath("./td[1]")).getText().trim();
                String nomination = movie.findElement(By.xpath("./td[2]")).getText().trim();
                String awards = movie.findElement(By.xpath("./td[3]")).getText().trim();
                Boolean isWinner = !movie.findElements(By.xpath("./td[4]/i")).isEmpty(); // Checks if the best picture icon is present

                movieInfo.put("Epoch Time of Scrape", epochTime);
                movieInfo.put("Year",currentYear.trim());
                movieInfo.put("Title",title.trim());
                movieInfo.put("Nomination",nomination.trim());
                movieInfo.put("Awards",awards.trim());
                movieInfo.put("isWinner",isWinner);
                movieData.add(movieInfo);           
            

        }

        
    }
    saveToJson(movieData,"oscar-winner-data.json");
}
//hi
public static void saveToJson(ArrayList<HashMap<String, Object>> data,String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            if(path.contains("oscar")){
                File OutputFile = new File("output/"+path);
                OutputFile.getParentFile().mkdirs();
                objectMapper.writeValue(OutputFile, data);
                System.out.println("Data saved to output/"+path);
            }else{
                objectMapper.writeValue(new File(path), data);
                System.out.println("Data saved to "+path);
            }
            
        } catch (IOException e) {
            System.out.println("Error writing JSON file: " + e.getMessage());
        }
    }
}

