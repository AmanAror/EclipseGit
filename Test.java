package com.iss.qbit.apptest.sf.pages;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.tools.ant.taskdefs.WaitFor;
import org.apache.zookeeper.Op.Check;
import java.awt.Window.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.iss.qbit.apptest.extended.base.ExtendedComponentBase;
import com.iss.qbit.apptest.extended.elementhelper.ExtendedElementHelper;
import com.iss.qbit.apptest.extended.perf.ResponseTime;
import com.iss.qbit.apptest.extended.perf.ResponseTimeCalculator;
import com.iss.qbit.apptest.sf.Config;
import com.iss.qbit.apptest.sf.ErrorCheck;
import com.iss.qbit.apptest.sf.ExcelUtility;
import com.iss.qbit.apptest.sf.SFAssertion;
import com.iss.qbit.apptest.sf.SFElementHelper;
import com.iss.qbit.apptest.sf.SFMain;
import com.iss.qbit.apptest.sf.utils.DBHelper;

public class DashboardsPage extends ExtendedComponentBase<SFElementHelper> {

	public ErrorCheck ec;
	public DBHelper dbHelper;
	public String sisenseFrame = "myId";

	public DashboardsPage(SFElementHelper elementHelper)throws InvalidFormatException, FileNotFoundException, IOException{

		super(elementHelper);

		ec = new ErrorCheck(elementHelper);
		dbHelper = new DBHelper();

	}

	@Override
	public WebElement element() {
		return elementHelper.driver().findElement(By.xpath("//div[contains(@id,'main-content')]"));
	}

	public String dashboardSelected1() throws InterruptedException {

		ec.switchToFrame("Default");
		elementHelper.waitForAngular();
		WebElement selectedDashboard = elementHelper.getValidatedElement(By.xpath("//div[@class='p-3']//h5"));
		
		String currentDashboard = selectedDashboard.getText();
        Config.LOGGER.info("Current Dashboard is selected as [" + currentDashboard + "].");
		ec.switchToFrame("Default");
		return currentDashboard;
	}
	
	public String dashboardSelected() throws InterruptedException {

		ec.switchToFrame("Default");
		elementHelper.waitForAngular();
		WebElement selectedDashboard = elementHelper.getValidatedElement(By.xpath("//div[@class='p-3']//h5"));
		WebElement workbookName = elementHelper.getValidatedElement(By.xpath("//div[@class='p-3']//h5//*[not(name()='small')]"));
		String dashboardName = selectedDashboard.getText();
		String replacement = "";
		String selectedDashboardName = dashboardName.replace(workbookName.getText(), replacement);		
		String currentDashboard = selectedDashboardName;
        Config.LOGGER.info("Current Dashboard is selected as [" + currentDashboard + "].");
		ec.switchToFrame("Default");
		return currentDashboard;
	}	
	
	public String workbookSelected() throws InterruptedException {

		ec.switchToFrame("Default");
		elementHelper.waitForAngular();
		WebElement selectedWorkbook = elementHelper.getValidatedElement(By.xpath("//div[@class='p-3']//h5//span"));
		String currentWorkbook = selectedWorkbook.getText();
        Config.LOGGER.info("Current Workbook is selected as [" + currentWorkbook + "].");
		ec.switchToFrame("Default");
		return currentWorkbook;
	}
	public void waitforVisibilityOfDashboard(String dashboardName)
	{
		By workbookName=By.xpath("//div[@class='side-bar-button'][text()='"+dashboardName+"']");
		elementHelper.waitForLoading(60,workbookName);
	}
	public ResponseTime navigateToDashboard(String dashboardName, String workbookName) throws InterruptedException {
		By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		if (!(containsIgnoreCase(dashboardSelected(), dashboardName)&& containsIgnoreCase(workbookSelected(), workbookName))) {

			WebElement dashboard = elementHelper.getValidatedElement(By.xpath("//div[@class='side-bar-button' and contains(text(),'" + dashboardName + "')]"));

			if (dashboard != null) {
				ResponseTime time = elementHelper.click(dashboard);
                Config.LOGGER.info("[" + dashboardName + "] is clicked.");
				time.add(elementHelper.waitForAngular());
				time.add(elementHelper.waitForDashboardWidgetsLoad());
				//time.add(elementHelper.waitForLoading(60, layout));
				
				/*
				time.add(elementHelper.waitForAngular());
				time.add(elementHelper.waitForAngular());
				time.add(elementHelper.waitForAngular());*/
				
				ec.switchToFrame(sisenseFrame);
				time.add(elementHelper.waitForAngular());
				time.add(elementHelper.waitForDashboardWidgetsLoad());				
				//time.add(elementHelper.waitForLoading(60, layout));
				
				/*
				time.add(elementHelper.waitForAngular());
				time.add(elementHelper.waitForAngular());
				time.add(elementHelper.waitForAngular());*/
                Config.LOGGER.info("Dashboard is selected as [" + dashboardName + "].");
				ec.switchToFrame("Default");
				ec.errorCheck();
				Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
				return time;
			} else
				throw new RuntimeException("Unable to click link containing text: [" + dashboardName + "]");
		} else {
			elementHelper.waitForAngular();
			ec.switchToFrame(sisenseFrame);
			elementHelper.waitForAngular();
			ec.switchToFrame("Default");
			ResponseTime time = restoreFilter();
			time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForDashboardWidgetsLoad());
			//time.add(elementHelper.waitForLoading(60, layout));
			/*
			time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForAngular());*/
			ec.switchToFrame(sisenseFrame);
			time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForDashboardWidgetsLoad());
			//time.add(elementHelper.waitForLoading(60, layout));
			/*
			time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForAngular());*/
			ec.switchToFrame("Default");
			Config.LOGGER.info("[" + dashboardName + "] is already selected.");
			ec.errorCheck();
			Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
			return time;
		}

	}
    	public static String getCurrentMethodName() {
		return Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	public void verifyAttachment() throws InterruptedException, IOException {
		ec.switchToFrame(sisenseFrame);
		elementHelper.waitForAngular();
		WebElement prism = elementHelper.getValidatedElement(By.xpath("//div[contains(@class, 'prism-toolbar__cell')]"));
		WebElement toolbar = prism.findElement(By.xpath("//span[contains(@class,'general-more') and contains(@data-icon-name,'general-more')]"));
		toolbar.click();
		Config.LOGGER.info("General more button is clicked.");
		//Thread.sleep(1500);
		//WebElement downloadbutton = elementHelper.getValidatedElement(By.xpath("//div[@class='menu-item' and @title='Download']"));
		By downloadbutton = By.xpath("//div[@class='mi-caption' and @title='Download']");
		elementHelper.waitForLoading(5,downloadbutton);
		elementHelper.click(downloadbutton);
		//downloadbutton.click();
		//Thread.sleep(1500);
		//WebElement downloadPDFbutton = elementHelper.getValidatedElement(By.xpath("//div[@class='menu-item' and @title='Download PDF']"));
		By downloadPDFbutton = By.xpath("//div[@class='mi-caption' and @title='Download PDF']");
		elementHelper.waitForLoading(5,downloadPDFbutton);
		elementHelper.click(downloadPDFbutton);
		//downloadPDFbutton.click();
        Config.LOGGER.info("Download PDF button is clicked.");
		ec.switchToFrame(sisenseFrame);
		WebElement selectionPopUp = elementHelper.getValidatedElement(By.xpath("//section[@class='db-preview-container']"));
		WebElement selectAllButton = selectionPopUp.findElement(By.xpath("(//div[@class='drop-value' and @data-ng-bind='getDisplayText()'])[1]"));
		if (!(selectAllButton.getText().equalsIgnoreCase("A3"))) {
			WebElement loadDropdown = elementHelper.getValidatedElement(By.xpath("//section[@class='db-preview-container']"));
			WebElement selectdropdown = elementHelper.getValidatedElement(By.xpath("(//div[@class='drop-presenter'])[1]"));
			selectdropdown.click();
			List<WebElement> selectPaperSize = loadDropdown.findElements(By.xpath("//span[@class='item-text' and @data-ng-bind='item.text']"));
			for (int i = 0; i < selectPaperSize.size(); i++) {
				String paperSizeText = selectPaperSize.get(i).getText();
				if (paperSizeText.equalsIgnoreCase("A3")) {
					selectPaperSize.get(i).click();
					break;
				}
			}
		}
		Config.LOGGER.info("The Paper Size is selected as A3.");
		
		/* WebElement checkLandscape = elementHelper.getValidatedElement(By.xpath("//input[contains(@class,'checked') and @type='radio']/parent::button"));
		  if (!checkLandscape.getText().equalsIgnoreCase("Landscape")) {
			WebElement checkbox = elementHelper.getValidatedElement(By.xpath("//div[contains(@class,'ux-rbx-list')]"));
			List<WebElement> selectPaperSize = checkbox.findElements(By.xpath("//div[@data-value='option.value']"));
			for (int i = 0; i < selectPaperSize.size(); i++) {
				String mode = selectPaperSize.get(i).getText();
				if (mode.equalsIgnoreCase("Landscape")) {
					selectPaperSize.get(i).click();
					break;
				}
			}
		}*/
		Thread.sleep(1500);
		WebElement selectLandscape = elementHelper.getValidatedElement(By.xpath("//div[contains(text(),'Landscape')]/parent::label //span[contains(@class,'custom-radiobtn__icon')]"));
		Thread.sleep(1500);
		selectLandscape.click();
		Config.LOGGER.info("The Orientation is selected as Landscape.");
		WebElement clickDownloadButton = elementHelper.getValidatedElement(By.xpath("//button[@title='Download']"));
		Thread.sleep(1500);
		clickDownloadButton.click();
		//Thread.sleep(1500);
		verifyDownload("PDF");
		ec.switchToFrame("Default");
		ec.errorCheck();
		Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
	
	}

	public void downloadTable(String option) throws InterruptedException, IOException {
		ec.switchToFrame(sisenseFrame);
		WebElement pivotTable = elementHelper.getValidatedElement(By.xpath("//*[local-name()='pivot'][contains(@class,'pivot-body')]"));
		elementHelper.scrollByVisibleElement(pivotTable);
		Config.LOGGER.info("Scrolled to the pivot table.");
		//Thread.sleep(1000);
		//WebElement widgetHeader = elementHelper.findElement(By.xpath("//*[local-name()='widget'][@type='pivot'] //*[local-name()='widget-header']"));
		By widgetHeader = By.xpath("//*[local-name()='widget'][@type='pivot'] //*[local-name()='widget-header']");
		elementHelper.waitForLoading(5,widgetHeader);
		//elementHelper.scrollByVisibleElement(widgetHeader);
		elementHelper.scrollByVisibleElement(elementHelper.findElement(widgetHeader));
		Config.LOGGER.info("Scrolled to the pivot header.");
		//Thread.sleep(1000);
		try {
			elementHelper.JShover(elementHelper.findElement(widgetHeader));
            Config.LOGGER.info("Hovering over the widget header.");
			elementHelper.click(widgetHeader);
            Config.LOGGER.info("Widget Header is clicked.");
		} catch (Exception e) {
			//WebElement firstCell = elementHelper.findElement(By.xpath("//*[local-name()='td'][@originalid='pivot__R0']"));
			//elementHelper.scrollByVisibleElement(firstCell);
			By firstCell = By.xpath("//*[local-name()='td'][@originalid='pivot__R0']");
			elementHelper.scrollByVisibleElement(elementHelper.findElement(firstCell));
			elementHelper.waitForLoading(5,firstCell);
			Config.LOGGER.info("Scrolled to the ["+elementHelper.findElement(firstCell).getText()+"].");
			Thread.sleep(2500);
			elementHelper.JShover(elementHelper.findElement(firstCell));
			Config.LOGGER.info("Hovering over ["+elementHelper.findElement(firstCell).getText()+"].");
			elementHelper.click(firstCell);
			Config.LOGGER.info("["+elementHelper.findElement(firstCell).getText()+"] is clicked.");
			Thread.sleep(500);
		}
		WebElement moreOptions = elementHelper.findElement(By.xpath("//*[local-name()='widget'][@type='pivot'] //*[local-name()='span'][contains(@class,'general-more')]/parent::button"));
		elementHelper.JShover(moreOptions);
		Thread.sleep(500);
        //Config.LOGGER.info("Hovering over the more-options options.");
		//elementHelper.click(moreOptions);
        //Config.LOGGER.info("More-options button is clicked.");
		//Thread.sleep(2000);
		//WebElement download = elementHelper.getValidatedElement(By.xpath("//*[local-name()='div'][@class='mi-caption' and text()='Download']"));
		//elementHelper.waitForVisibility(download, true);
        By download = By.xpath("//*[local-name()='div'][@class='mi-caption' and text()='Download']");
        elementHelper.waitForLoading(5,download);
		elementHelper.click(download);
        Config.LOGGER.info("Download button is clicked."); 
		WebElement downloadOption = elementHelper.getValidatedElement(By.xpath("//*[local-name()='div'][@class='mi-caption' and contains(text(),'" + option + "')]"));
		elementHelper.waitForVisibility(downloadOption, true);
        Config.LOGGER.info("Waiting for the [" + option + "] File to get visible.");
		elementHelper.click(downloadOption);
		verifyDownload(option);
		ec.switchToFrame("Default");
		ec.errorCheck();
		Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
	}

	public void verifyDownload(String option) throws IOException, InterruptedException {
		Thread.sleep(2000);
		By downloadDialog = By.xpath("//div[@class='dashboard-export-dialog-content']");
		if (isPresent(downloadDialog)) {
			WebElement filename = elementHelper.getValidatedElement(By.xpath("//p[@class='filename']"));
			String attachmentName = filename.getText().replace("/", "_");
			Config.LOGGER.info("The name of the report is "+attachmentName+".");
			Thread.sleep(20000);
			//elementHelper.getValidatedElement(By.xpath("//div[@class='dialog-buttons']/button")).click();//clicking cancelled button
			elementHelper.waitForInvisibility(360, downloadDialog);
			By errorMessage = By.xpath("//p[contains(@data-ng-show,Error) and contains(@class,'error')]");
			if (!isPresent(downloadDialog) || isPresent(errorMessage)) {
				
				Path currentDir = Paths.get(System.getProperty("user.home") + "\\Downloads");
				
				DirectoryStream<Path> dirStream = Files.newDirectoryStream(currentDir);
				long newest = 0L;
				Path downloadPath = null;
				for (Path path : dirStream) {
					BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
					long createTime = attr.creationTime().toMillis();
					if (createTime > newest) {
						downloadPath = path.toAbsolutePath();
						newest = createTime;
					}
				}
				Config.LOGGER.info("The latest folder in the directory is" + downloadPath+".");

				File fileDirectory = new File(downloadPath +  "" );
				Config.LOGGER.info("File Directory is ["+fileDirectory.toString()+"]");
				//Thread.sleep(2000);

				errorMessage = By.xpath("//p[contains(@data-ng-show,Error) and contains(@class,'error')]");
				if (isPresent(errorMessage)) {
					if (fileDirectory.exists() && (!elementHelper.findElement(errorMessage).getText().contains("This shouldn't have happened. Please ask your administrator for assistance."))) {
						Assert.assertTrue(fileDirectory.exists());
						Config.LOGGER.info("Path exists. File successfully downloaded.");
					}

					else if (fileDirectory.exists() && elementHelper.getValidatedElement(By.xpath("//p[contains(@data-ng-show,Error) and contains(@class,'error')]")).getText().contains("This shouldn't have happened. Please ask your administrator for assistance.")) {
						throw new RuntimeException("Dashbord PDF downloaded successfully but an error occurred.");
					} else {
						throw new RuntimeException("File has not been downloaded successfully.");
					}

				} else {
					if (fileDirectory.exists()) {
						Config.LOGGER.info("Path exists. File successfully downloaded.");
					}

					else {
						throw new RuntimeException("File has not been downloaded successfully.");
					}

				}
			} else {
				throw new RuntimeException("The file is taking too long to download.");

			}
		}
		else {
			if (option.equals("CSV") || option.equals("Excel")) {
				String attachmentName = "";
				if (option.equals("CSV")) {
					WebElement filename = elementHelper.getValidatedElement(By.xpath("//*[local-name()='widget'][@type='pivot'] //*[local-name()='widget-header'] //*[local-name()='widget-title']"));
					if (filename != null) {
						attachmentName = filename.getText().replace("/", "_").replace(" ", "") + ".csv";
						Config.LOGGER.info("The name of the report is "+attachmentName+".");
					} else {
						List<WebElement> filenames = elementHelper.getValidatedElements(By.xpath("//*[local-name()='div'][@id='editor-container'] //*[local-name()='font'][@size='5']"));
						filename = filenames.get(filenames.size() - 1);
						attachmentName = filename.getText().replace("/", "_").replace(" ", "") + ".csv";
						Config.LOGGER.info("The name of the report is "+attachmentName+".");
					}
				} else {
					WebElement filename = elementHelper.getValidatedElement(By.xpath("//*[local-name()='widget'][@type='pivot'] //*[local-name()='widget-header'] //*[local-name()='widget-title']"));
					if (filename != null) {
						attachmentName = filename.getText().replace("/", "_") + " pivot.xlsx";
						Config.LOGGER.info("The name of the report is "+attachmentName+".");
					} else {
						List<WebElement> filenames = elementHelper.getValidatedElements(By.xpath("//*[local-name()='div'][@id='editor-container'] //*[local-name()='font'][@size='5' or @size='6']"));
						filename = filenames.get(filenames.size() - 1);
						attachmentName = filename.getText().replace("/", "_") + " pivot.xlsx";
						Config.LOGGER.info("The name of the report is "+attachmentName+".");
					}
				}
				Path currentDir = Paths.get(System.getProperty("user.home") + "\\Downloads");
				//Path currentDir = Paths.get(System.getProperty("user.dir") + "\\OUTPUT");
				Config.LOGGER.info(currentDir.toString());
				DirectoryStream<Path> dirStream = Files.newDirectoryStream(currentDir);
				long newest = 0L;
				Path downloadPath = null;
				for (Path path : dirStream) {
					BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
					long createTime = attr.creationTime().toMillis();
					if (createTime > newest) {
						downloadPath = path.toAbsolutePath();
						newest = createTime;
					}
					
				}
				Config.LOGGER.info("The latest folder in the directory is" + downloadPath+".");
				File fileDirectory = new File(downloadPath +  "" );
				//File fileDirectory = new File(downloadPath + "\\EXPORT" + "\\" + attachmentName);
				Config.LOGGER.info("File Directory is ["+fileDirectory.toString()+"]");
				//Thread.sleep(2000);
				if (fileDirectory.exists()) {
					Config.LOGGER.info("Path exists. File successfully downloaded.");
				}

				else
				{
					throw new RuntimeException("File has not been downloaded successfully.");
				}

			}
			if (option.equals("PDF")) {
				WebElement filename = elementHelper.getValidatedElement(By.xpath("//*[local-name()='div'][@class='dashboard-title']"));
				String attachmentName = filename.getText().trim().replace(" ", "-").replace("(", "").replace(")", "")+ "-" + getCurrentDate() + ".pdf";
				Config.LOGGER.info("The name of the report is "+attachmentName+".");
				Path currentDir = Paths.get(System.getProperty("user.home") + "\\Downloads");
				//Path currentDir = Paths.get(System.getProperty("user.dir") + "\\OUTPUT");
				Config.LOGGER.info(currentDir.toString());
				DirectoryStream<Path> dirStream = Files.newDirectoryStream(currentDir);
				long newest = 0L;
				Path downloadPath = null;
				for (Path path : dirStream) {
					BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
					long createTime = attr.creationTime().toMillis();
					if (createTime > newest) {
						downloadPath = path.toAbsolutePath();
						newest = createTime;
					}
				}
				Config.LOGGER.info("The latest folder in the directory is" + downloadPath+".");
				File fileDirectory = new File(downloadPath +  "" );
				//File fileDirectory = new File(downloadPath + "\\EXPORT" + "\\" + attachmentName);
				Config.LOGGER.info("File Directory is ["+fileDirectory.toString()+"]");
				//Thread.sleep(2000);
				if (fileDirectory.exists()) {
					Config.LOGGER.info("Path exists. File successfully downloaded.");
				}

				else {
					throw new RuntimeException("File has not been downloaded successfully.");
				}
			}
		}
	}

	public void selectWorkbook(String workbookName) throws InterruptedException {
		elementHelper.waitForAngular();
		ec.switchToFrame(sisenseFrame);
		elementHelper.waitForAngular();
		ec.switchToFrame("Default");
		//Thread.sleep(5000);
		//WebElement dropdown = elementHelper.getValidatedElement(By.id("dropdown-basic-button"));
		//elementHelper.click(dropdown);
		By dropdown  = By.id("dropdown-basic-button");
		elementHelper.waitForLoading(5, dropdown);
		//elementHelper.click(dropdown);
		elementHelper.findElement(dropdown).click();
        Config.LOGGER.info("The workbooks are loaded in the dropdown menu.");
		//Thread.sleep(5000);
        By dropdownMenu = By.xpath("//div[contains(@class,'dropdown-menu')]");
        elementHelper.waitForLoading(5,dropdownMenu);
		WebElement workbook = elementHelper.getValidatedElement(By.xpath("//div[@class='side-bar-menu-item' and contains(text(),'" + workbookName + "')]"));
		if (workbook != null) {
			elementHelper.click(workbook);
            Config.LOGGER.info("The workbook [" + workbookName + "] is selected.");
			elementHelper.waitForAngular();
		} else{
			throw new RuntimeException("No such workbook present");
	}
		}

	public ResponseTime restoreFilter() {
		ec.switchToFrame(sisenseFrame);
		WebElement filterPanel = elementHelper.findElement(By.xpath("//div[@class='filters-headline']"));
		ResponseTime time = null;
		if (filterPanel.isDisplayed()) {
			WebElement restoreFilter = elementHelper.findElement(By.xpath("//span[@class='fl-restore-filter']"));
			time = elementHelper.click(restoreFilter);
            Config.LOGGER.info("The restore filter button is clicked.");
			time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForAngular());
            /*time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForDashboardWidgetsLoad());
			By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
			time.add(elementHelper.waitForLoading(60, layout));
             */
		} else {
			WebElement restoreFilter = elementHelper.findElement(By.xpath("//div[@id='toolbarResetButton']"));
			time = elementHelper.click(restoreFilter);
            Config.LOGGER.info("The reset button is clicked.");
			time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForAngular());
            /*time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForDashboardWidgetsLoad());*/
            Config.LOGGER.info("The filters are cleared.");
		}
		ec.errorCheck();
        Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
		return time;
	}

	public ResponseTime bubbleMenuChange(String menu, String link) {
		By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		ec.switchToFrame(sisenseFrame);
		WebElement dropdown = elementHelper.getValidatedElement(By.xpath("//div[contains(text(),'" + menu+ "')]/parent::div //div[contains(@class,'fields-switcher-dropdown')]"));
		if (dropdown != null) {

			if (!containsIgnoreCase(dropdown.getText(), link)) {
				elementHelper.click(dropdown);
                Config.LOGGER.info("Dropdown menu is loaded.");
				ResponseTime time = elementHelper.click(By.xpath("//div[@class='mi-caption' and contains(text(),'" + link + "')]"));
				Config.LOGGER.info("[" + link + "] is clicked.");
                /*time.add(elementHelper.waitForAngular());
				time.add(elementHelper.waitForAngular());
				time.add(elementHelper.waitForAngular());*/
				time.add(elementHelper.waitForAngular());
				time.add(elementHelper.waitForDashboardWidgetsLoad());
				time.add(elementHelper.waitForLoading(60, layout));
                Config.LOGGER.info(menu + " is selected as [" + link + "]");
				ec.errorCheck();
                Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
				return time;
			} else
				Config.LOGGER.info(menu + " is already selected as [" + link + "]");
		}

		return null;
	}

	/*public ResponseTime multiPickerContainer(String title, String value) {
		By layout = By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		ec.switchToFrame(sisenseFrame);
		By dimensionContainer = By.xpath("//div[contains(@class,'uneditable-title')]//*[text()='" + title+ "']/ancestor::widget[@type='DimensionPicker']//div[contains(@class,'value fields-switcher-dropdown')]");
		By menu = By.xpath("//div[contains(@class,'menu-content')]");
		By dimension = By.xpath("//div[@class='mi-caption' and contains(text(),'" + value + "')]");
		elementHelper.click(dimensionContainer);
		elementHelper.waitForLoading(30, menu);
		ResponseTime time = elementHelper.click(dimension);
		time.add(elementHelper.waitForAngular());				
		time.add(elementHelper.waitForLoading(60, layout));
		Config.LOGGER.info(title + " is selected as [" + value + "]");
		ec.errorCheck();
		Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
		return time;
	}*/
	
	public String bubbleMenuSelected(String menu) {
		ec.switchToFrame(sisenseFrame);
		WebElement dropdown = elementHelper.getValidatedElement(By.xpath("//div[contains(text(),'" + menu+ "')]/parent::div //div[contains(@class,'fields-switcher-dropdown')]"));
		return dropdown.getText();
	}

	public ResponseTime slicerChange(String slicerName, int slicerNumber, String link) {
		By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		ec.switchToFrame(sisenseFrame);
		List<WebElement> allDropdown = elementHelper.getValidatedElements(By.xpath("//span[contains(text(),'"+ slicerName + "')]/parent::div //div[contains(@class,'fields-switcher-dropdown')]"));
		WebElement dropdown = allDropdown.get(slicerNumber - 1);
		String slicerValue=dropdown.getText();
		if(link.contains("-") && slicerValue.replaceAll("\\s", "").equalsIgnoreCase(link.replaceAll("\\s", "")))
		{
			Config.LOGGER.info(slicerName + " is already selected as [" + link + "]");
		}
		else {
		if (dropdown != null) {

			if (!containsIgnoreCase(dropdown.getText(), link)) {
				elementHelper.click(dropdown);
                Config.LOGGER.info("Slicer dropdown menu loaded.");
				ResponseTime time = elementHelper.click(By.xpath("//div[@class='mi-caption' and contains(text(),'" + link + "')]"));
                Config.LOGGER.info("Axis changed to [" + link + "]");
				/*time.add(elementHelper.waitForAngular());
				time.add(elementHelper.waitForAngular());
				time.add(elementHelper.waitForAngular());*/
                time.add(elementHelper.waitForAngular());
    			time.add(elementHelper.waitForDashboardWidgetsLoad());    			
    			time.add(elementHelper.waitForLoading(60, layout));
				ec.errorCheck();
                Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
				return time;
			} else
            	Config.LOGGER.info(slicerName + " is already selected as [" + link + "]");
		}
		}
		return null;
	}
	
	public String slicerSelected(String slicerName, int slicerNumber) {
		ec.switchToFrame(sisenseFrame);
		List<WebElement> allDropdown = elementHelper.getValidatedElements(By.xpath("//span[contains(text(),'"+ slicerName + "')]/parent::div //div[contains(@class,'fields-switcher-dropdown')]"));
		WebElement dropdown = allDropdown.get(slicerNumber - 1);
		return dropdown.getText();
	}

	public void showFilterPanel() throws InterruptedException {
		ec.switchToFrame(sisenseFrame);
		WebElement filterPanel = elementHelper.findElement(By.xpath("//div[@class='filters-headline']"));
		if (!filterPanel.isDisplayed()) {
			WebElement showFilterPanel = elementHelper.findElement(By.id("toolbarFilterShowHideButton"));
			elementHelper.click(showFilterPanel);
            Config.LOGGER.info("The Filter button is clicked.");
			//Thread.sleep(2500);
            By allFilter = By.xpath("//div[contains(@class,'global-filters')]");
            elementHelper.waitForLoading(5, allFilter);
            
		}
	}

	public void checkTimePeriod(String filter) throws InterruptedException {
		ec.switchToFrame(sisenseFrame);
		showFilterPanel();
		List<WebElement> months = elementHelper.getValidatedElementsNoWait(By.xpath("//span[text()='" + filter+ "']/ancestor::div[@class='f-mini-host'] //div[@id='summary'] //div[@class='uc-tag']"));
		if (months.size() == 12) {
			// Calendar cal = Calendar.getInstance();
			// int currentMonth = cal.get(Calendar.MONTH);
			// int currentYear = cal.get(Calendar.YEAR);

			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
			String strDate = formatter.format(date);
			Config.LOGGER.info("Date is " + strDate);

		}

	}

	public ResponseTime filterChange(String link) throws InterruptedException {
		By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		ec.switchToFrame(sisenseFrame);
		showFilterPanel();
		Thread.sleep(1000);
		WebElement editFilterIcon = elementHelper.findElement(By.xpath("//span[text()='" + link + "']/parent::div //div[contains(@title,'Edit Filter')]"));
		if (editFilterIcon != null) {

			elementHelper.JShover(editFilterIcon);
            Config.LOGGER.info("Hovering over the edit filter button of [" + link + "].");
			elementHelper.click(editFilterIcon);
            Config.LOGGER.info("Edit filter button of [+" + link + "] is clicked");
			WebElement selectionPopUp = elementHelper.getValidatedElement(By.xpath("//div[@class='popup-inner']"));
			//Thread.sleep(1500);
			By selectAll = By.xpath("//div[contains(@data-ng-class,'selectModeClass()')]");
			elementHelper.waitForLoading(5, selectAll);
			//Thread.sleep(1500);
			if (isPresent(selectAll)) {
				WebElement selectAllButton = selectionPopUp.findElement(By.xpath("//div[contains(@data-ng-class,'selectModeClass()')]"));
				if (!selectAllButton.getAttribute("class").contains("empty")) {
					elementHelper.click(selectAllButton);
					Config.LOGGER.info("Select All button for the [" + link + "] is clicked and all checkboxes are unticked.");
				} else {
					elementHelper.click(selectAllButton);
					Config.LOGGER.info("Select All button for the [" + link + "] is clicked and all checkboxes are ticked.");
					elementHelper.click(selectAllButton);
					Config.LOGGER.info("Select All button for the [" + link+ "] is clicked again and all checkboxes are unticked.");
				}
			}
			List<WebElement> optionsCheckBox = elementHelper.getValidatedElements(By.xpath("//div[@class='popup-inner'] //div[@class='list-item'] //div[contains(@class,'uc-chk-icon')]"));
            	Config.LOGGER.info("Total Options available are : " + optionsCheckBox.size());
			Set<Integer> randomOptions = randomSelectOptions(optionsCheckBox.size());
			for (int i : randomOptions) {
				Thread.sleep(1000);
				WebElement menuItem = optionsCheckBox.get(i);
				elementHelper.click(menuItem);
			}

			ResponseTime time = elementHelper.click(By.xpath("//div[@class='uc-ok' and contains(text(),'OK')]"));
            Config.LOGGER.info("OK button has been clicked.");
			/*time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForAngular());*/
            time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForDashboardWidgetsLoad());
			time.add(elementHelper.waitForLoading(60, layout));
			ec.errorCheck();
			Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
			restoreFilter();
			return time;

		} else
			throw new RuntimeException("Unable to locate element");
	}

	public ResponseTime codedfilterChange(String tableName, String link) throws InterruptedException {
		By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		ec.switchToFrame(sisenseFrame);
		showFilterPanel();
		Thread.sleep(1000);
		WebElement editFilterIcon = elementHelper.findElement(By.xpath("//span[text()='" + link + "']/parent::div //div[contains(@title,'Edit Filter')]"));

		if (editFilterIcon != null) {
			Thread.sleep(1000);
			elementHelper.JShover(editFilterIcon);
            Config.LOGGER.info("Hovering over the edit filter button of [" + link + "].");
			//elementHelper.click(editFilterIcon);
            Config.LOGGER.info("Filter button of [" + link + "] is clicked.");
			WebElement selectionPopUp = elementHelper.getValidatedElement(By.xpath("//div[@class='popup-inner']"));
			//Thread.sleep(1500);
			By selectAll = By.xpath("//div[contains(@class,'select-all')] //button");
			elementHelper.waitForLoading(5, selectAll);
			//Thread.sleep(1500);
			if (isPresent(selectAll)) {
				WebElement selectAllButton = selectionPopUp.findElement(By.xpath("//div[contains(@class,'select-all')] //button"));
				//if (selectAllButton.getAttribute("checked").contains("checked")){
				if(isAttributePresent(selectionPopUp.findElement(By.xpath("//div[contains(@class,'select-all')] //button/input")), "checked")) {
					elementHelper.click(selectAllButton);
                    Config.LOGGER.info("Select All button for the [" + link + "] is clicked and all checkboxes are unticked.");
                    
				}
				else
				{
					elementHelper.click(selectAllButton);
                    Config.LOGGER.info("Select All button for the [" + link + "] is clicked and all checkboxes are ticked.");
					elementHelper.click(selectAllButton);
                    Config.LOGGER.info("Select All button for the [" + link+ "] is clicked again and all checkboxes are unticked.");
				}
			}
			String testCaseName = "Filter" + removeSpace(link);
            Config.LOGGER.info("Testcase name is " + testCaseName);
			Map<String, Object> testData = dbHelper.getFilterValue(tableName, testCaseName);
			List<String> options = getDataAsList(testData);
			Config.LOGGER.info("Value fetched from the database is " + options);
			WebElement searchBox = elementHelper.findElement(By.xpath("//label[contains(@class,'uc-ms-search-filter-placeholder')]/following-sibling::input"));
			if (searchBox.isDisplayed()) {
				for (int i = 0; i < options.size(); i++) {
					Config.LOGGER.info("[" + options.get(i) + "] has been entered.");
					elementHelper.actionSendKeys(searchBox, options.get(i));
					Thread.sleep(2500);
					List<WebElement> optionsCheckBox = elementHelper.getValidatedElements(By.xpath("//div[@class='popup-inner'] //div[@class='list-item'] //button"));
					List<WebElement> optionsCheckBoxText = elementHelper.getValidatedElements(By.xpath("//div[@class='popup-inner'] //div[@class='list-item'] //div[@class='uc-checker-content']/span"));
					for (int j = 0; j < optionsCheckBox.size(); j++) {
						if (optionsCheckBoxText.get(j).getText().equals(options.get(i))) {
							Thread.sleep(1000);
							WebElement menuItem = optionsCheckBox.get(j);
                            Config.LOGGER.info("The data fetched from database has matched the UI.");
							elementHelper.click(menuItem);
							Config.LOGGER.info("[" + optionsCheckBoxText.get(j).getText() + "] is selected.");
						}
					}

				}
			} else {
				for (int i = 0; i < options.size(); i++) {
					List<WebElement> optionsCheckBox = elementHelper.getValidatedElements(By.xpath("//div[@class='popup-inner'] //div[@class='list-item'] //button"));
					List<WebElement> optionsCheckBoxText = elementHelper.getValidatedElements(By.xpath("//div[@class='popup-inner'] //div[@class='list-item'] //div[@class='uc-checker-content']/span"));
					for (int j = 0; j < optionsCheckBox.size(); j++) {
						if (optionsCheckBoxText.get(j).getText().equals(options.get(i))) {
							Thread.sleep(1000);
							WebElement menuItem = optionsCheckBox.get(j);
							Config.LOGGER.info("The data fetched from database has matched the UI.");
							elementHelper.click(menuItem);
							Config.LOGGER.info("[" + optionsCheckBoxText.get(j).getText() + "] is clicked.");
						
						}
					}
				}
			}
			ResponseTime time = elementHelper.click(By.xpath("//div[@class='uc-ok' and contains(text(),'OK')]"));
            Config.LOGGER.info("OK button is clicked.");
			/*time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForAngular());*/
            time.add(elementHelper.waitForAngular());
			time.add(elementHelper.waitForDashboardWidgetsLoad());
			time.add(elementHelper.waitForLoading(60, layout));
			ec.errorCheck();
            Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
			restoreFilter();
			return time;

		} else
			throw new RuntimeException("Unable to locate element");
	}

	public ResponseTime bubbleDrill(String link) throws InterruptedException {
		By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		ec.switchToFrame(sisenseFrame);
		resetDrill();
		List<WebElement> graphBubbles = elementHelper.getValidatedElements(By.xpath("//*[local-name()='path'][contains(@class,'highcharts-point')]"));
		WebElement drillBubble = graphBubbles.get(randomNumGen(graphBubbles.size()));
		elementHelper.rightClick(drillBubble);
        Config.LOGGER.info("Random bubble is right clicked.");
		WebElement optionChoose = elementHelper.getValidatedElement(By.xpath("//*[local-name()='div'][contains(text(),'Choose Another')]"));
		WebElement drillInto = elementHelper.getValidatedElement(By.xpath("//*[local-name()='div'][contains(text(),'Drill Into')]"));
		if (optionChoose != null) {
			optionChoose.click();
            Config.LOGGER.info("Choose another option is selected from the dropdown menu.");
			elementHelper.waitForAngular();
			elementHelper.waitForAngular();

		}
		if (drillInto != null) {
			drillInto.click();
			elementHelper.waitForAngular();
			elementHelper.waitForAngular();
		}

		boolean checkNext = false;
		ResponseTime time = null;
		while (checkNext == false) {
			List<WebElement> drillOptions = elementHelper.getValidatedElements(By.xpath("//*[local-name()='div'][@class='smart-item-dim']/a"));

			for (int i = 0; i < drillOptions.size(); i++) {
				WebElement option = drillOptions.get(i);
				if (option.getText().equalsIgnoreCase(link)) {
					try {
						time = elementHelper.click(option);
                        Config.LOGGER.info("[" + link + "] is clicked.");
						/*time.add(elementHelper.waitForAngular());
						time.add(elementHelper.waitForAngular());
						time.add(elementHelper.waitForAngular());*/
                        time.add(elementHelper.waitForAngular());
            			time.add(elementHelper.waitForDashboardWidgetsLoad());            			
            			time.add(elementHelper.waitForLoading(60, layout));
						checkNext = true;

					} catch (Exception e) {
						WebElement nextButton = elementHelper.getValidatedElement(By.xpath("//*[local-name()='span'][contains(@class,'arrow-right')]"));
						elementHelper.click(nextButton);
                        Config.LOGGER.info("Next button is clicked to load more items.");
						time = elementHelper.click(option);
                        Config.LOGGER.info("[" + link + "] is clicked.");;
						/*time.add(elementHelper.waitForAngular());
						time.add(elementHelper.waitForAngular());
						time.add(elementHelper.waitForAngular());*/
                        time.add(elementHelper.waitForAngular());
            			time.add(elementHelper.waitForDashboardWidgetsLoad());
            			time.add(elementHelper.waitForLoading(60, layout));
						checkNext = true;
					}

				}
				if (checkNext == true)
					break;
			}
			if (checkNext == true)
				break;
			//Thread.sleep(3000);
			//WebElement nextButton = elementHelper.getValidatedElement(By.xpath("//*[local-name()='span'][contains(@class,'arrow-right')]"));
			By nextButton = By.xpath("//*[local-name()='span'][contains(@class,'arrow-right')]");
			elementHelper.waitForLoading(5,nextButton);
			elementHelper.click(nextButton);
			By drillGrid = By.xpath("//div[contains(@class,'smart-item-holder')]");
			elementHelper.waitForLoading(5,drillGrid);
			//Thread.sleep(3000);
		}
		ec.errorCheck();
        Config.LOGGER.info("Error check for the method " + getCurrentMethodName() + " is completed.");
		resetDrill();
		return time;

	}

	public void resetDrill() {
		ec.switchToFrame(sisenseFrame);
		By resetDrill = By.xpath("//*[local-name()='li'][@class='bc-reset-crumb']");
		if (isPresent(resetDrill)) {
			elementHelper.click(resetDrill);
            Config.LOGGER.info("Drill is reset.");
			elementHelper.waitForAngular();
			elementHelper.waitForAngular();
			elementHelper.waitForAngular();

		}

	}

	public String bubbleDrillSelected() {
		ec.switchToFrame(sisenseFrame);
		List<WebElement> bubbleDrillSelected = elementHelper.getValidatedElements(By.xpath("//*[local-name()='span'][contains(@class,'bc-sub-crumb-name')]"));
		return bubbleDrillSelected.get(1).getText();
	}

	public ResponseTime linkJumpTo() {
		By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		ec.switchToFrame(sisenseFrame);
		WebElement pivotTable = elementHelper.getValidatedElement(By.xpath("//*[local-name()='pivot'][contains(@class,'pivot-body')]"));
		List<WebElement> links = pivotTable.findElements(By.xpath("//*[local-name()='a'][contains(@href,'javascript:void')]"));
		int selectedLink = randomNumGen(links.size());
		ResponseTime time = elementHelper.click(links.get(selectedLink));
		Config.LOGGER.info("Jumping to thee link ["+selectedLink+"].");
		WebElement frame = elementHelper.getValidatedElement(By.className("drill-popup"));
		ec.switchToFrame("class", "drill-popup");
        Config.LOGGER.info("Switching to the drill-popup frame.");
		/*time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());*/
        time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForDashboardWidgetsLoad());
		time.add(elementHelper.waitForLoading(60, layout));
		ec.errorCheck(selectedLink);
		return time;

	}

	public ResponseTime barJumpTo(String widgetName, String value) throws InterruptedException, AWTException {
		By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		hideThumbnail();
		elementHelper.waitForDashboardWidgetsLoad();
		ec.switchToFrame(sisenseFrame);
		// WebElement widget =
		// elementHelper.getValidatedElement(By.xpath("//*[local-name()='widget'][@type='chart/bar']//*[local-name()='widget-title'][text()='"+widgetName+"']//ancestor::widget"));
		List<WebElement> values = elementHelper.getValidatedElements(By.xpath("//*[local-name()='widget'][@type='chart/bar']//*[local-name()='widget-title'][text()='" + widgetName+ "']//ancestor::widget //*[local-name()='g'][contains(@class,'highcharts-xaxis-labels')] //*[local-name()='tspan']"));
		int valueIndex = 0;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i).getText().contains(value)) {
				valueIndex = i;
				break;
			}
		}
		// widget =
		// elementHelper.getValidatedElement(By.xpath("//*[local-name()='widget'][@type='chart/bar']//*[local-name()='widget-title'][text()='"+widgetName+"']//ancestor::widget"));
		List<WebElement> bar = elementHelper.getValidatedElements(By.xpath("//*[local-name()='widget'][@type='chart/bar']//*[local-name()='widget-title'][text()='" + widgetName+ "']//ancestor::widget //*[local-name()='g'][contains(@class,'highcharts-tracker')] //*[local-name()='rect']"));
		WebElement targetBar = bar.get(valueIndex);
		elementHelper.rightClick(targetBar);
		
		//elementHelper.robotRightClick(targetBar);
        Config.LOGGER.info("Bar is clicked");
        By menu = By.xpath("//div[contains(@class,'menu-content')]");
        elementHelper.waitForLoading(5, menu);
		//Thread.sleep(3000);
		WebElement jumpToLink = elementHelper.findElement(By.xpath("//div[@class='mi-caption' and contains(@title,'Jump to')]"));
		ResponseTime time = elementHelper.click(jumpToLink);
		ec.switchToFrame("class", "drill-popup");
		/*time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());*/
		time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForDashboardWidgetsLoad());
		time.add(elementHelper.waitForLoading(60, layout));
		ec.errorCheck();
        Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
		ec.switchToFrame("Default");
		return time;

	}
	
	public ResponseTime selectLine(String widgetName, String value) throws InterruptedException {
		By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		restoreFilter();
		ec.switchToFrame(sisenseFrame);
		
		List<WebElement> values = elementHelper.getValidatedElements(By.xpath("//*[local-name()='widget'][@type='chart/line']//*[local-name()='widget-title'][text()='" + widgetName+ "']//ancestor::widget //*[local-name()='g'][contains(@class,'highcharts-xaxis-labels')] //*[local-name()='tspan']"));
		int valueIndex = 0;
		for (int i = 0; i < values.size(); i++) {
			System.out.println(values.get(i).getText());
			String currentValue=values.get(i).getText();
			if (currentValue.contains(value)) {
				valueIndex = i;
				break;
			}
		}
		List<WebElement> bar = elementHelper.getValidatedElements(By.xpath("//*[local-name()='widget'][@type='chart/line']//*[local-name()='widget-title'][text()='" + widgetName+ "']//ancestor::widget //*[local-name()='g'][contains(@class,'highcharts-tracker')] //*[local-name()='rect']"));
		WebElement targetBar = bar.get(valueIndex);
		ResponseTime time = elementHelper.click(targetBar);
        Config.LOGGER.info("Bar is clicked");
		/*time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());*/
        time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForDashboardWidgetsLoad());
		time.add(elementHelper.waitForLoading(60, layout));
		ec.errorCheck();
        Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
        restoreFilter();
		ec.switchToFrame("Default");
		return time;

	}
	
	public ResponseTime selectTimeDuration(String value) throws InterruptedException {
		By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		ec.switchToFrame(sisenseFrame);
		WebElement timeDuration = elementHelper.findElement(By.xpath("//*[local-name()='div'][@class='listDefaultCSS'] //*[local-name()='span'][text()='"+value+"']"));
		ResponseTime time = elementHelper.click(timeDuration);
        Config.LOGGER.info("Time duration of "+value+" is clicked");
		/*time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());*/
        time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForDashboardWidgetsLoad());
		time.add(elementHelper.waitForLoading(60, layout));
		ec.errorCheck();
        Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
		ec.switchToFrame("Default");
		return time;

	}
	
	public ResponseTime selectDimension (String value) throws InterruptedException {
		By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		ec.switchToFrame(sisenseFrame);
		WebElement timeDuration = elementHelper.findElement(By.xpath("//*[local-name()='div'][@class='listDimensionChanger'] //*[local-name()='span'][text()='"+value+"']"));
		ResponseTime time = elementHelper.click(timeDuration);
        Config.LOGGER.info("Time duration of "+value+" is clicked");
		/*time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());*/
        time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForDashboardWidgetsLoad());
		time.add(elementHelper.waitForLoading(60, layout));
		ec.errorCheck();
        Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
		ec.switchToFrame("Default");
		return time;

	}
	
	public void hideThumbnail() {

		By hideThumbnail = By.xpath("//button[@class='btn btn-simfundlink' and text()='Hide Thumbnails']");
		if (isPresent(hideThumbnail)) {
			elementHelper.click(hideThumbnail);
            Config.LOGGER.info("Hide Thumbnails is clicked.");
			elementHelper.waitForAngular();
			elementHelper.waitForAngular();
			elementHelper.waitForAngular();
			elementHelper.waitForAngular();
			elementHelper.waitForAngular();
			ec.switchToFrame(sisenseFrame);
			elementHelper.waitForAngular();
			elementHelper.waitForAngular();
			elementHelper.waitForAngular();
			elementHelper.waitForAngular();
			elementHelper.waitForAngular();
			ec.switchToFrame("Default");
		}
	}

	public List<String> getDataAsList(Map testData) {
		ArrayList<String> options = new ArrayList<String>();
		for (int i = 1; i <= 10; i++) {
			if (!testData.get("Coded_Value_" + i).toString().equals("")) {
				options.add(testData.get("Coded_Value_" + i).toString());
			}

		}
		return options;
	}

	public Set<Integer> randomSelectOptions(int size) {
		Random randNum = new Random();
		Set<Integer> optionsUsed = new LinkedHashSet<Integer>();
		while (optionsUsed.size() < (size / 2) & optionsUsed.size() < 10) {
			optionsUsed.add(randNum.nextInt(size));
		}
        Config.LOGGER.info("The Set comprises of : " + optionsUsed);
		return optionsUsed;

	}

	public int randomNumGen(int size) {
		Random randNum = new Random();
        Config.LOGGER.info("Random number generated is : " + randNum.nextInt(size));
		return randNum.nextInt(size);
	}

	public boolean containsIgnoreCase(String main, String sub) {
		main = main.toLowerCase();
		sub = sub.toLowerCase();
		return main.contains(sub);
	}

	public String removeSpace(String str) {
		str = str.replaceAll("\\s", "");
		return str;
	}

	public boolean isPresent(By by) {
		List<WebElement> elements = elementHelper.getValidatedElements(by);
		if (elements.size() == 1)
			return true;
		else if (elements.size() > 1)
			throw new RuntimeException("Multiple elements present for the same locator.");
		else
			return false;
	}
	
	
	public String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		Date date = new Date();
		return formatter.format(date).toString();
	}
	public ResponseTime addToWorkbook(String dashboardName, String workbookName) throws InterruptedException
	{
		By addToWorkbook=By.xpath("//button[contains(text(),'Add to Workbook')]");
		By saveDashboard=By.xpath("//button[contains(text(),'Save Dashboard')]");
		By dashboardTextBox=By.xpath("//input[@id='dashboardName']");
		By warning=By.xpath("//div[text()='Dashboard with this name already exists in the chosen Workbook.']");
		By closeButton=By.xpath("//div[text()='Add Dashboard to a Workbook']/parent::div/following-sibling::button");
		
		ResponseTime time=elementHelper.click(addToWorkbook);
		time.add(elementHelper.waitForLoading(15, dashboardTextBox));
		elementHelper.getValidatedElement(dashboardTextBox).clear();
		Thread.sleep(1000);
		elementHelper.getValidatedElement(dashboardTextBox).sendKeys(dashboardName);
		Select workbook = new Select(elementHelper.getValidatedElement(By.xpath("//div[@class='col'] //select[@class='form-control']")));
		workbook.selectByVisibleText(workbookName);
		time.add(elementHelper.click(saveDashboard));
		By dialogSpinner = By.xpath("//div[@class='modal-footer'] //div[@role='status' and @class='spinner-border']");
		elementHelper.waitForLoading(5, dialogSpinner);
		elementHelper.waitForInvisibility(10, dialogSpinner);
		
		By internalError=By.xpath("//div[text()='Internal error has occurred. Contact support.']");
		if(isPresent(internalError))
		{
			Assert.assertFalse(true,"Internal error has occurred. Contact support error alert pops up on adding the dashboard ["+dashboardName+"] to the workbook ["+workbookName+"].");
		}
		try {
			if(isPresent(warning))
			{
				Config.LOGGER.info("Dashboard with the name ["+dashboardName+" ] already exists in the worbook [ "+workbookName+" ]");
				elementHelper.click(closeButton);
			}
		}
		catch(Exception e)
		{
			elementHelper.waitForInvisibility(60, dashboardTextBox);	
		}
		ec.errorCheck();
		return time;
	}
	

public ResponseTime selectESGPicker(String value) throws InterruptedException {
		By layout=By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		ec.switchToFrame(sisenseFrame);
		WebElement timeDuration = elementHelper.findElement(By.xpath("//*[local-name()='div'][@class='listDimensionChanger'] //*[local-name()='span'][text()='"+value+"']"));
		ResponseTime time = elementHelper.click(timeDuration);
        Config.LOGGER.info(value+" is clicked");
		/*time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());*/
        time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForDashboardWidgetsLoad());
		time.add(elementHelper.waitForLoading(60, layout));
		ec.errorCheck();
        Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
		ec.switchToFrame("Default");
		return time;

	}

	public ResponseTime selectDimensionPicker(String title, String... rating) throws InterruptedException {
		ResponseTime time = null;
		By layout = By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
		ec.switchToFrame(sisenseFrame);
		By picker = By.xpath("//div[contains(@class,'uneditable-title')]//*[text()='" + title+ "']/ancestor::widget[@type='DimensionPicker']//div[contains(@class,'value fields-switcher-dropdown')]");
		By menu = By.xpath("//data-menu[contains(@class,'dim-dropdown-menu')]");
		By widget = By.xpath("//widget-title[contains(text(),'" + title + "')]");	
		elementHelper.scrollByVisibleElement(elementHelper.getValidatedElement(widget));
		Thread.sleep(1500);
		List<WebElement> dropdown = elementHelper.getValidatedElements(picker);
		System.out.println(dropdown.size());
		for (int i = 0; i < dropdown.size(); i++) {
			for (String name : rating) {
				if (!containsIgnoreCase(dropdown.get(i).getText(), name)) {
					elementHelper.scrollByVisibleElement(elementHelper.getValidatedElement(widget));
					time = elementHelper.click(dropdown.get(i));
					elementHelper.waitForLoading(20, menu);
					List<WebElement> list = elementHelper.getValidatedElements(By.xpath("//data-menu[contains(@class,'dim-dropdown-menu')]//div[contains(@class,'menu-item-container')]"));
					for (int j = 0; j < list.size(); j++) {
						if (containsIgnoreCase(list.get(j).getText(), name)) {
							time = elementHelper.click(list.get(j));
							Thread.sleep(1500);
							time.add(elementHelper.waitForAngular());
							time.add(elementHelper.waitForAngular());
							time.add(elementHelper.waitForAngular());
							time.add(elementHelper.waitForDashboardWidgetsLoad());
							time.add(elementHelper.waitForLoading(60, layout));
							break;
						}
					}
					i++;
					elementHelper.scrollByVisibleElement(elementHelper.getValidatedElement(widget));
					Config.LOGGER.info("Dimension Picker is selected as [" + name + "].");
				} else {
					Config.LOGGER.info("Dimension Picker is already selected as [" + name + "].");
					i++;
				}
			}

		}
		ec.errorCheck();
		Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
		return time;
	}
	
	public ResponseTime selectDimensionPicker(String title, String value) {
	By layout = By.xpath("//div[contains(@class,'dashboard-layout-column-host')] //div[contains(@class,'dashboard-layout-column')]");
	ec.switchToFrame(sisenseFrame);
	ResponseTime time = null;
	By picker = By.xpath("//div[contains(@class,'uneditable-title')]//*[text()='" + title+ "']/ancestor::widget[@type='DimensionPicker']//div[contains(@class,'value fields-switcher-dropdown')]");
	//By picker = By.xpath("//div[contains(@class,'uneditable-title')]//*[text()='" + widget + "']/ancestor::div[@class='dashboard-layout-column']//div[contains(@class,'value fields-switcher-dropdown')]");
	By menu = By.xpath("//data-menu[contains(@class,'dim-dropdown-menu')]");
	String defaultValue = elementHelper.getValidatedElement(picker).getText();
	if (!containsIgnoreCase(defaultValue, value)) {
		elementHelper.getValidatedElement(picker).click();
		elementHelper.waitForLoading(60, menu);
		List<WebElement> list = elementHelper.getValidatedElements(By.xpath("//data-menu[contains(@class,'dim-dropdown-menu')]//div[contains(@class,'menu-item-container')]"));
		for (int i = 0; i < list.size(); i++) {
			if (containsIgnoreCase(list.get(i).getText(), value)) {
				time = elementHelper.click(list.get(i));
				break;
			}
		}
		time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForAngular());
		time.add(elementHelper.waitForDashboardWidgetsLoad());
		time.add(elementHelper.waitForLoading(60, layout));
		Config.LOGGER.info("Dimension Picker is selected as [" + value + "]");
		ec.errorCheck();
		Config.LOGGER.info("Error check for " + getCurrentMethodName() + " is completed.");
		return time;
	} else {
		Config.LOGGER.info("Dimension Picker is already selected as [" + value + "]");
		return null;
	}
}
	
	
	public boolean isAttributePresent(WebElement element, String attrName) {
	    JavascriptExecutor executor = (JavascriptExecutor) elementHelper.driver();
	    String script = "return arguments[0].getAttributeNames();";
	    Object attributes = executor.executeScript(script, element);
	    return ((ArrayList) attributes).contains(attrNam43);
	}
}
