package com.testClass;


import org.testng.annotations.*;

import com.pages.GenericPage;
import com.utils.ConfigUtils;
import com.utils.DriverInitialisation;

//Extend Driver Initialize will invoke browser
public class GoogleSearchAndAssertingSearchResultsIncludingPagination extends DriverInitialisation {
	
	//Generic Page is created which has all methods created for validation
	public GenericPage genericPage;
	
	@BeforeClass
	public void InitializeDriver() {
		
		//Initiating WebDriver through DriverInitialization Extended Class
		genericPage = new GenericPage(driver);
		
		// Open Google.com - Default search Page
		driver.get("https://www.google.com");
		
	}

	@Test
	public void validateSearch()
	{
		//Perform Google search using search text configured
		genericPage.googleSearch(ConfigUtils.getConfigData("searchText"));
	
		// Pagination to check all search results provided are matching
		genericPage.clickMore();
		
		//Validate each result shown in Google search results
		if(genericPage.validateSearchText(ConfigUtils.getConfigData("searchText")))
		{
			//This line will be printed  when exact search result is  matching
			System.out.println(ConfigUtils.getConfigData("searchText")+ " is displayed in all links");
		}
	
		else
		{
			//Testcase will fail when there is no match result
			genericPage.fail("Exact Searched text is not present in all search Result");
		}
		
	}
	
	@AfterClass
	public void driverClose()
	{
		//Closing browser using in-built method
		closeBrowser();
	
	}
}