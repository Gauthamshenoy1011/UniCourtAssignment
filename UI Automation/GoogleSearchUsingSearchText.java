
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Constants.ChromeBrowserOptions;

public class GoogleSearchAndAssertingSearchResultsIncludingPagination {

	public static void main(String[] args) throws InterruptedException {

		// Supress Chrome options
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized"); 
		options.addArguments("disable-infobars"); // disabling infobars
		options.addArguments("--disable-extensions"); // disabling extensions
		options.addArguments("--disable-gpu"); // applicable to windows os only
		options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
		options.addArguments("--no-sandbox"); // Bypass OS security model
		options.addArguments("--disable-blink-features=AutomationControlled");
		
		//Initiating WebDriver
		WebDriver driver = new ChromeDriver();
		// Open Google.com - Default search Page
		driver.get("https://www.google.com");

		// Setting below Browser parameters to change ViewPort to overcome Google Search - ReCaptcha authentication
		int width = 1920;
		int height = 1080;
		Dimension dimension = new Dimension(width, height);
		driver.manage().window().setSize(dimension);
		driver.manage().window().maximize();

		// Sending Gautham Shenoy Text for Google Search
		String wordToBeSearched = "Gautham Shenoy";
		driver.findElement(By.name("q")).sendKeys(wordToBeSearched);

		// Wait for Google Auto suggestions to appear
		WebDriverWait waitForAutoSuggestionToAppear = new WebDriverWait(driver, Duration.ofSeconds(5));
		waitForAutoSuggestionToAppear.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul")));

		// Click to Enter to Get Google Search Results once auto suggestion appears
		driver.findElement(By.name("q")).sendKeys(Keys.ENTER);

		// Wait for Google search result to appear
		WebDriverWait waitForSearchResultToAppear = new WebDriverWait(driver, Duration.ofSeconds(5));
		waitForSearchResultToAppear.until(ExpectedConditions.presenceOfElementLocated(By.id("rso")));

		// Pagination to check all search results provided are matching
		boolean isNextButtonDisplayed = driver.findElement(By.xpath("//*[text()='Next']")).isDisplayed();
		do {
			// Wait for Next Page SearchResult to Appear
			waitForSearchResultToAppear.until(ExpectedConditions.presenceOfElementLocated(By.id("rso")));
						
			// Assert whether Searched text is present in search results
			List<WebElement> searchResults = driver.findElement(By.id("rso")).findElements(By.xpath("//h3"));
			for (WebElement eachSearchResult : searchResults) {
				WebElement currentPageNumberWebElement= driver.findElement(By.xpath("//td[@class='YyVfkd']"));
				if(eachSearchResult.getText().contains(wordToBeSearched))
				{
				//This line will be printed  when exact search result is  matching
				System.out.println("Exact searched text is present in Search Results . Search Result: " + eachSearchResult.getText() + " is shown in Page No " + currentPageNumberWebElement.getText());
				}
				
				else
				{
					//This line will be printed when exact search result is not matching
					System.out.println("Exact Searched text is not present. Search Result " + eachSearchResult.getText() + " is shown in Page No " + currentPageNumberWebElement.getText()+" is not proper");
				}
			}
			
			
			try
			{
			//Click on Next button till nth page in Google Search Results using Do While loop
			driver.findElement(By.xpath("//*[text()='Next']")).click();
			isNextButtonDisplayed = driver.findElement(By.xpath("//*[text()='Next']")).isDisplayed();
			
			}
			catch(Exception e)
			{
				//If there is no Next button, come out of DoWhile loop
				isNextButtonDisplayed = false;
			}
			
		} while (isNextButtonDisplayed);

		driver.close();
	}
}
