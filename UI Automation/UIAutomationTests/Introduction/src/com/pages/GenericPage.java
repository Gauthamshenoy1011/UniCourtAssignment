package com.pages;

import java.util.List;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.utils.ObjectRepositoryUtils;
import com.utils.seleniumUtils;

public class GenericPage extends seleniumUtils {

	public GenericPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Perform search using keyword provided. [Wait for Auto Suggestion & Wait for Search Results to appear is internally handled]
	 * 
	 * @param=String to be searched to be sent
	 * @author Gautham
	 */
	public void googleSearch(String textToSearched)
	{
		
		sendKeys(ObjectRepositoryUtils.getLocator("searchField"), textToSearched);
		System.out.println("Enter Search field in Google search box "+textToSearched);
		
		// Wait for Google Auto suggestions to appear
		WaitUntilElementIsVisible(ObjectRepositoryUtils.getLocator("searchFieldDropDown"));
		
		//Click on Enter to get Google search results
		sendKeysEnter(ObjectRepositoryUtils.getLocator("searchField"), Keys.ENTER);
		System.out.println("Search for text in Google Search: "+textToSearched);
		
		//Wait for Google auto search results to appear
		WaitUntilElementIsVisible(ObjectRepositoryUtils.getLocator("searchResult"));
		System.out.println("Search Results found for text in Google Search: "+textToSearched);

	}
	
	/**
	 * Click on More Results button to appear all results for assertion
	 * 
	 * @param=No Parameters
	 * @author Gautham
	 */
	public void clickMore()
	{
			while (isDisplayed(ObjectRepositoryUtils.getLocator("checkForMoreResults")) && !(isDisplayed(ObjectRepositoryUtils.getLocator("checkSearchResultsInEnd")))) 
			{
				try
				{
				 scrollIntoView(ObjectRepositoryUtils.getLocator("checkForMoreResults"));
				 wait(5);
				 click(ObjectRepositoryUtils.getLocator("checkForMoreResults"));
			
				}
				catch(Exception e)
				{
					fail("Unable to click More Results button" + e.getMessage());
					
				}
			}
		
	}
	
	/**
	 * Validate each search result shown in search results
	 * 
	 * @param=String to be asserted to be sent
	 * @author Gautham
	 * @return= Assert searched text is present in each search result.
	 */
	public boolean validateSearchText(String textToSearched)
	{	
	boolean flag=true;
	List<WebElement> searchTexts=findElements(ObjectRepositoryUtils.getLocator("searchResultTextLink"));
	
	for (WebElement eachSearchResult : searchTexts) {
		if(eachSearchResult.getText().contains(textToSearched))
		{
		//This line will be printed  when exact search result is  matching
		System.out.println("Exact searched text is present in Search Results . Search Result: " + eachSearchResult.getText() + " is shown" );
		}
		
		else
		{
			flag=false;
			//This line will be printed when exact search result is not matching
			System.out.println("Exact Searched text is not present. Search Result " + eachSearchResult.getText() + " is shown");
		}
	}
	return flag;
	
	}
}
