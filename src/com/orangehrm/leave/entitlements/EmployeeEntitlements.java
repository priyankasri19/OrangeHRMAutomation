package com.orangehrm.leave.entitlements;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrm.leave.contants.OrangeHRMConstants;
import com.orangehrm.leave.utility.OrangeHRMUtility;

import dev.failsafe.internal.util.Assert;

public class EmployeeEntitlements {
	
	WebDriver driver;
	
	@BeforeTest
	public void login() throws InterruptedException{
		System.setProperty("webdriver.gecko.driver", "lib\\geckodriver.exe");
		driver =new FirefoxDriver();
		driver.get(OrangeHRMConstants.loginURL);
		driver.manage().window().maximize();
		Thread.sleep(3000);
		List<String> loginData = OrangeHRMUtility.adminCredentails();
		driver.findElement(By.name("username")).sendKeys(loginData.get(0));
		driver.findElement(By.name("password")).sendKeys(loginData.get(1));
		driver.findElement(By.cssSelector("[type=submit]")).click();
		//Find Leave Menu from Left Navigation Panel and click it
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		OrangeHRMUtility.navigateToLeaveMenu(driver, OrangeHRMConstants.leaveMenuXpath);
		
	}
	
	@Test(dataProvider = "employeeEntitlementsData")
	public void employeeEntitlements(String empNameHint, String dateRange) throws InterruptedException {
		
		String mainMenuXpath="//*[@id=\"app\"]/div[1]/div[1]/header/div[2]/nav/ul/li[3]/span";
		String subMenuXpath="//*[@id=\"app\"]/div[1]/div[1]/header/div[2]/nav/ul/li[3]/ul/li[2]/a";
		
		OrangeHRMUtility.navigateToSubMenu(driver, mainMenuXpath, subMenuXpath);
		//find element employee name and lookup for employee using hint
		String empNameXpath = OrangeHRMConstants.empNameXpath;
		OrangeHRMUtility.employeeNameByHint(driver, empNameXpath, empNameHint);
		
		//find element Leave Period and select a date range
		String leavePeriodXpath = "//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/div[2]/form/div[1]/div/div[3]/div/div[2]/div/div/div[1]";
		OrangeHRMUtility.selectLeavePeriod(driver, leavePeriodXpath, dateRange);
		
		//Going to click on search
		driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[1]/div[2]/form/div[2]/button")).click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		
		boolean searchResultFound = driver.findElement(By.className("oxd-table-card")).isDisplayed();
		assertTrue(searchResultFound, "Employee Leave Entitlements Displayed");
		
	}
	
	@DataProvider
	public Object[][] employeeEntitlementsData() throws IOException{
		
		String excelFilePath = "test-data\\TestData.xlsx";
		XSSFWorkbook wb= new XSSFWorkbook(excelFilePath);
		XSSFSheet Sheet= wb.getSheet("employeeEntitlementData");
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
	
	@AfterTest
	public void close(){
		driver.close();
		
		
	}

}


