package com.orangehrm.leave.login;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrm.leave.contants.OrangeHRMConstants;

public class Login {
	
WebDriver driver;
	
	@BeforeMethod
	public void initilaize() throws InterruptedException{
		System.setProperty("webdriver.gecko.driver", "lib\\geckodriver.exe");
		driver = new FirefoxDriver();
		driver.get(OrangeHRMConstants.loginURL);
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(2, TimeUnit.SECONDS);
		//Thread.sleep(1000);
	}
	
	@Test(dataProvider = "validCredentials")
	public void validLogin(String userName, String password) throws InterruptedException{
		driver.findElement(By.name("username")).sendKeys(userName);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.cssSelector("[type=submit]")).click();
		String expectedUrl=OrangeHRMConstants.homePageURL;
		String actualURL = driver.getCurrentUrl();
		Assert.assertEquals(actualURL, expectedUrl);
		Thread.sleep(3000);
	}
	
	@Test(dataProvider = "invalidCredentials")
	public void invalidLogin(String userName, String password) throws InterruptedException{
		driver.findElement(By.name("username")).sendKeys(userName);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.cssSelector("[type=submit]")).click();
		String expectedUrl=OrangeHRMConstants.loginURL;
		String actualURL = driver.getCurrentUrl();
		Assert.assertEquals(actualURL, expectedUrl);
		Thread.sleep(3000);
	}
	
	@DataProvider
	public Object[][] validCredentials() throws IOException{
		
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
	
	@DataProvider
	public Object[][] invalidCredentials() throws IOException{
		
		String excelFilePath = "test-data\\TestData.xlsx";
		XSSFWorkbook wb= new XSSFWorkbook(excelFilePath);
		XSSFSheet Sheet= wb.getSheet("invalidLoginData");
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
