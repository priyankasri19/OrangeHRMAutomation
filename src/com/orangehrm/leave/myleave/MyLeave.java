package com.orangehrm.leave.myleave;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.orangehrm.leave.contants.OrangeHRMConstants;
import com.orangehrm.leave.utility.OrangeHRMUtility;

public class MyLeave {
	
	WebDriver driver;
	
	@BeforeTest
	public void login() throws InterruptedException{
		System.setProperty("webdriver.gecko.driver", "lib\\geckodriver.exe");
		driver =new FirefoxDriver();
		driver.get(OrangeHRMConstants.loginURL);
		driver.manage().window().maximize();
		Thread.sleep(3000);
		List<String> loginData = OrangeHRMUtility.userCredentails();
		driver.findElement(By.name("username")).sendKeys(loginData.get(0));
		driver.findElement(By.name("password")).sendKeys(loginData.get(1));
		driver.findElement(By.cssSelector("[type=submit]")).click();
		//Find Leave Menu from Left Navigation Panel and click it
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		OrangeHRMUtility.navigateToLeaveMenu(driver, OrangeHRMConstants.leaveMenuXpath);
		
	}
	
	@Test
	public void myLeave() throws InterruptedException {
		//Going to click on Tab Myleave
		String mainMenuXpath="//*[@id=\"app\"]/div[1]/div[1]/header/div[2]/nav/ul/li[2]/a";
		OrangeHRMUtility.navigateToMainMenu(driver, mainMenuXpath);
		//Going to click on Search
		driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[1]/div[2]/form/div[2]/button[2]")).click();
		
		boolean searchResultFound = driver.findElement(By.className("oxd-table-card")).isDisplayed();
		assertTrue(searchResultFound, "My Leave List is  Displayed");
	}
	
	@AfterTest
	public void close(){
		driver.close();
	}

}
