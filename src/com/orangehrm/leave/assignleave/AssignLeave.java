package com.orangehrm.leave.assignleave;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrm.leave.contants.OrangeHRMConstants;
import com.orangehrm.leave.utility.OrangeHRMUtility;

public class AssignLeave {
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
	
	@Test(dataProvider = "assignLeaveData")
	public void assignLeave(String empNameHint, String leaveTypeText, String fromDate, String comments) throws InterruptedException {
		
		//Going to click on Tab Assign Leave
		String mainMenuXpath="//*[@id=\"app\"]/div[1]/div[1]/header/div[2]/nav/ul/li[7]/a";
		OrangeHRMUtility.navigateToMainMenu(driver, mainMenuXpath);
		
		//Going to fill the assign page
		String empNameXpath=OrangeHRMConstants.empNameXpath;
		OrangeHRMUtility.employeeNameByHint(driver, empNameXpath, empNameHint);
		
		//Leave Type
		OrangeHRMUtility.selectLeaveType(driver, leaveTypeText);
		
		//From Date
		driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[3]/div/div[1]/div/div[2]/div/div/input")).sendKeys(fromDate);
		
		//Find  Comments element and fill value
		driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[4]/div/div/div/div[2]/textarea")).sendKeys(comments);
		
		//Find Assign Button and click it
		driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[6]/button")).click();
		
		Thread.sleep(2000);
		
	}
	
	@DataProvider
	public Object[][] assignLeaveData() throws IOException{
		
		String excelFilePath = "test-data\\TestData.xlsx";
		XSSFWorkbook wb= new XSSFWorkbook(excelFilePath);
		XSSFSheet Sheet= wb.getSheet("assignLeaveData");
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
