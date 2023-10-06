package com.orangehrm.leave.accessrights;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrm.leave.contants.OrangeHRMConstants;
import com.orangehrm.leave.utility.OrangeHRMUtility;

public class AccessPrivelages {

WebDriver driver;
	
	@BeforeMethod
	public void initilaize() throws InterruptedException{
		System.setProperty("webdriver.gecko.driver", "lib\\geckodriver.exe");
		driver = new FirefoxDriver();
		driver.get(OrangeHRMConstants.loginURL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}
	
	@Test(dataProvider = "loginData")
	public void leave(String userName, String password) throws InterruptedException {
		driver.findElement(By.name("username")).sendKeys(userName);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.cssSelector("[type=submit]")).click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		//Find Leave Menu from Left Navigation Panel and click it
		OrangeHRMUtility.navigateToLeaveMenu(driver, OrangeHRMConstants.leaveMenuXpath);
		//Find All Main menus on the top of the page
		List<WebElement> allMenus = driver.findElements(By.className("oxd-topbar-body-nav-tab"));	    
	    
	    if("Admin".equals(userName) 
	    		&& (allMenus.contains(OrangeHRMConstants.configure)
	    		||  allMenus.contains(OrangeHRMConstants.leaveList)
	    		||  allMenus.contains(OrangeHRMConstants.assignLeave))) {
	    	
	    	Assert.assertTrue(true, "Admin User has access to Configure, Leave List and Assign Leave features");
	    }
	    
	    if((!"Admin".equals(userName)) 
	    		&& (allMenus.contains(OrangeHRMConstants.apply)
	    		||  allMenus.contains(OrangeHRMConstants.myLeave)
	    		||  allMenus.contains(OrangeHRMConstants.entitlements)
	    		||  allMenus.contains(OrangeHRMConstants.reports))) {
	    	
	    	Assert.assertTrue(true, "Non Admin User does not have access to Configure, Leave List and Assign Leave features");
	    }
	    		
	}
	
	@DataProvider
	public Object[][] loginData() throws IOException{
		
		String excelFilePath = "test-data\\TestData.xlsx";
		XSSFWorkbook wb= new XSSFWorkbook(excelFilePath);
		XSSFSheet Sheet= wb.getSheet("validLoginData");
		int rowNumber = Sheet.getPhysicalNumberOfRows();
		int colNumber = Sheet.getRow(0).getPhysicalNumberOfCells();
		Object [][] data = new Object [rowNumber][colNumber];
		for (int row = 0; row < rowNumber; row++) {
			for (int col =0 ; col < colNumber; col++) {
				data[row][col]=Sheet.getRow(row).getCell(col).getStringCellValue();
			}
		}
		wb.close();
		return data;
	}
	
	@AfterMethod
	public void close() {
		driver.close();
	}
}
