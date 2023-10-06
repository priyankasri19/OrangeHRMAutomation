package com.orangehrm.leave.utility;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.leave.contants.OrangeHRMConstants;

public class OrangeHRMUtility {

	public static List<String> adminCredentails(){
		
		List<String> credentials = new ArrayList<>();
		String excelFilePath = "test-data\\TestData.xlsx";
		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(excelFilePath);
			XSSFSheet sheet= wb.getSheet("validLoginData");
			credentials.add(sheet.getRow(0).getCell(0).getStringCellValue());
			credentials.add(sheet.getRow(0).getCell(1).getStringCellValue());
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				wb.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return credentials;
	}
	
	public static List<String> userCredentails(){
		
		List<String> credentials = new ArrayList<>();
		String excelFilePath = "test-data\\TestData.xlsx";
		XSSFWorkbook wb = null;
		try {
			wb = new XSSFWorkbook(excelFilePath);
			XSSFSheet sheet= wb.getSheet("validLoginData");
			credentials.add(sheet.getRow(1).getCell(0).getStringCellValue());
			credentials.add(sheet.getRow(1).getCell(1).getStringCellValue());
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				wb.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return credentials;
	}
	
	public static void navigateToLeaveMenu(WebDriver driver, String leaveMenuXpath) throws InterruptedException {
		
        //Find Leave Menu from Left Navigation Panel and click it
        driver.findElement(By.xpath(leaveMenuXpath)).click();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}

	public static void navigateToMainMenu(WebDriver driver, String mainMenuXpath) throws InterruptedException {
		
		driver.findElement(By.xpath(mainMenuXpath)).click();
		Thread.sleep(2000);
	}
	
	public static void navigateToSubMenu(WebDriver driver, String mainMenuXpath, String subMenuXpath) throws InterruptedException {
		
		WebElement mainMenu = driver.findElement(By.xpath(mainMenuXpath));
		Actions action=new Actions(driver);
		action.moveToElement(mainMenu).click().perform();
		Thread.sleep(2000);
		WebElement subMenu = driver.findElement(By.xpath(subMenuXpath));
		action.moveToElement(subMenu).click().perform();
		Thread.sleep(2000);
	}
	
	
	public static void employeeNameByHint(WebDriver driver, String xPath, String empNameHint){
		
		//find Element by xpath and send the search string
		driver.findElement(By.xpath(xPath)).sendKeys(empNameHint);
		// explicit wait to wait for the auto suggestions list to be present
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    WebElement we = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@role='listbox']")));
	    try {
	    	//Wait for 2 seconds to populate the auto suggestion list
	    	Thread.sleep(2000);
	    }catch(Exception e) {
	    	
	    }
	    
	    we.click();
	}
	
	public static void selectLeaveType(WebDriver driver, String type) throws InterruptedException{
		WebElement element=driver.findElement(By.className(OrangeHRMConstants.leaveTypeXpath));
		while(true) {
			element.sendKeys(Keys.ARROW_DOWN);
			if(element.getText().equals(type)) {
				element.sendKeys(Keys.ENTER);
				break;
			}
		}
		
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}
	
	public static void selectLeavePeriod(WebDriver driver, String xPath, String dateRange) {
		driver.findElement(By.xpath(xPath)).sendKeys(dateRange);
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}

}
