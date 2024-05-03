

package demo;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utilities {

public static void scrape(WebDriver driver, String year) {
    WebElement yearLink = driver.findElement(By.id(year));
    String yearText = yearLink.getText();
    SeleniumWrapper.clickAction(yearLink, driver);

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='table']")));

    ArrayList<HashMap<String, String>> movieList = new ArrayList<>();
    long epochTime = System.currentTimeMillis() / 1000;

    List<WebElement> rows = driver.findElements(By.xpath("//tr[@class='film']"));
    int count = 0;
    for (WebElement row : rows) {
        String movieTitle = row.findElement(By.xpath("./td[@class='film-title']")).getText();
        int nominations = Integer.parseInt(row.findElement(By.xpath("./td[@class='film-nominations']")).getText());
        int awards = Integer.parseInt(row.findElement(By.xpath("./td[@class='film-awards']")).getText());

        boolean isWinner = count == 0;
        String isWinnerText = String.valueOf(isWinner);

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("epoch Time", String.valueOf(epochTime));
        dataMap.put("Year", yearText);
        dataMap.put("Title", movieTitle);
        dataMap.put("Nominations", String.valueOf(nominations));
        dataMap.put("Awards", String.valueOf(awards));
        dataMap.put("isWinner", isWinnerText);

        movieList.add(dataMap);
        count++;

        if (count >= 5) {
            break;
        }
    }

    for (HashMap<String, String> data : movieList) {
        System.out.println("Epoch Time of Scrape: " + data.get("epoch Time") +
                ", Year: " + data.get("Year") +
                ", Title: " + data.get("Title") +
                ", Nominations: " + data.get("Nominations") +
                ", Awards: " + data.get("Awards") +
                ", isWinner: " + data.get("isWinner"));
    }

    ObjectMapper objectMapper = new ObjectMapper();
    try {
        String filePath = "D:/Automation Crio projects/Crio-Code-A-Thon Assignments/WebScraper/selenium-starter-2/src/test/resources/movieList" + year + "-data.json";
        File jsonFile = new File(filePath);
        objectMapper.writeValue(jsonFile, movieList);
        System.out.println("JSON data written to: " + filePath);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}

