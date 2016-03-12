package uk.co.listing;

import io.github.bonigarcia.wdm.ChromeDriverManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.Scenario;
import cucumber.api.java.After;

/**
 * Base class to inherit the cucumber steps from, which will drive the selenium
 * tests
 *
 * @author rvinayak
 *
 */
public class SeleniumConnector {

	public static final String ID = "id";
	private static final String PARTIAL_LINK_TEXT = "partiallinktext";
	private static final String LINK_TEXT = "linktext";
	private static final String CSS_SELECTOR = "cssselector";
	private static final String XPATH = "xpath";
	private static final String NAME = "name";
	private final static long DEFAULT_TIMEOUT = 2000;
	private final static long DEFAULT_WAIT = 10;

	private static String SERVER = "localhost";
	private static String PORT = "9999";
	private static String CONTEXT = "listing-ui/dist/#";
	private static boolean HEADLESS = false;

	private static WebDriver driver;
	private static String server_address;

	private static boolean isWindows;
	private static boolean isLinux;
	private static boolean isMac;
	private static final Class<SystemUtils> c = SystemUtils.class;

	public static Log log = LogFactory.getLog(SeleniumConnector.class);

	private static final Thread CLOSE_THREAD = new Thread() {
		@Override
		public void run() {
			try {
				driver.close();
				driver.quit();
			} catch (final Exception e) {
				log.error(
						"exception while shutting down driver: "
								+ e.getMessage(), e);
			}
		}
	};

	static {
		init();
		Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
	}

	private static void init() {
		try {
			isWindows = c.getField("IS_OS_WINDOWS").getBoolean(c);
			isLinux = c.getField("IS_OS_LINUX").getBoolean(c);
			isMac = c.getField("IS_OS_MAC").getBoolean(c);

			HEADLESS = StringUtils.isNotBlank(System.getProperty("headless"));
			SERVER = StringUtils.defaultString(System.getProperty("server"),
					SERVER);
			PORT = StringUtils.defaultString(System.getProperty("port"), PORT);
			CONTEXT = StringUtils.defaultString(System.getProperty("context"),
					CONTEXT);

			server_address = "http://" + SERVER + ":" + PORT;
			log.info("running tests against: " + server_address);

			if (HEADLESS) {
				driver = getPhantomJSDriver();
			} else {
				driver = getChromeDriver();
			}
			// login();
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			log.error(
					"exception while initialising web driver: "
							+ e.getMessage(), e);
		}
	}

	@After
	public void tearDown(final Scenario scenario) {
		if (scenario.isFailed()) { // take a screenshot and save
			final String userDir = System.getProperty("user.dir");
			log.info("\n>>>>>>>>>>>>>>>>>>>> FAILED SCENARIO: "
					+ scenario.getName() + "<<<<<<<<<<<<<<<<<<<<<<\n");

			try {
				final byte[] screenshotByte = ((TakesScreenshot) driver)
						.getScreenshotAs(OutputType.BYTES);
				scenario.embed(screenshotByte, "image/png");
				final FileOutputStream output = new FileOutputStream(new File(
						userDir + "/target/" + scenario.getId() + ".png"));
				IOUtils.write(screenshotByte, output);
			} catch (final IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	// Don't delete below commented method
	// private static WebDriver getHtmlUnitDriver() {
	// HtmlUnitDriver driver = new HtmlUnitDriver();
	// driver.setJavascriptEnabled(true);
	// return driver;
	// }

	private static WebDriver getChromeDriver() {
		try {
			log.info("setting up Chrome driver");
			ChromeDriverManager.setup();
		} catch (final Throwable e) {
			log.info("Could not download latest ChromeDrive");
			final String webdriverPath = System
					.getProperty("webdriver.chrome.driver");
			log.info("Using location from system property webdriver.chrome.driver: "
					+ webdriverPath);
			if (StringUtils.isBlank(webdriverPath)) {
				log.error("webdriver.chrome.driver is not set, please set it to a location where you have chromedriver binary/executable is set");
			}
		}
		return new ChromeDriver();
	}

	private static WebDriver getPhantomJSDriver() {
		if (isWindows) {
			log.info("\n########################### WINDOWS TESTS #################################\n");
			System.setProperty("phantomjs.binary.path",
					"src/test/resources/webdrivers/phantomjs.exe");
		} else if (isLinux) {
			log.info("\n########################### UNIX TESTS #################################\n");
			System.setProperty("phantomjs.binary.path",
					"src/test/resources/webdrivers/phantomjs");
		} else if (isMac) {
			log.info("\n########################### MAC TESTS #################################\n");
		}
		return new PhantomJSDriver();
	}

	public static void openAndWait(final String location) {
		final String url = server_address + "/" + CONTEXT + "/" + location;
		log.info("opening page:" + url);
		driver.get(url);
		driver.manage().window().maximize();
	}

	public static boolean isTextPresent(final String text) {
		log.info("is text present: " + text);
		return waitForTextPresent(text);
	}

	public static boolean isTextNotPresent(final String text) {
		final WebElement content = driver.findElement(By.tagName("body"));
		return !content.getText().contains(text);
	}

	public static boolean waitForTextPresent(final String text) {
		final WebDriverWait webDriverWait = new WebDriverWait(driver,
				DEFAULT_WAIT);
		final boolean bool = webDriverWait.until(ExpectedConditions
				.textToBePresentInElementLocated(By.tagName("body"), text));
		return bool;
	}

	public static boolean waitForTextPresent(final String text,
			final int extraTime) {
		final WebDriverWait webDriverWait = new WebDriverWait(driver,
				DEFAULT_WAIT + extraTime);
		final boolean bool = webDriverWait.until(ExpectedConditions
				.textToBePresentInElementLocated(By.tagName("body"), text));
		return bool;
	}

	public static WebElement waitForElementPresent(final String element) {
		final WebDriverWait webDriverWait = new WebDriverWait(driver,
				DEFAULT_WAIT);
		final WebElement webElement = webDriverWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.id(element)));
		return webElement;
	}

	public static WebElement waitForElementPresent(final String element,
			final String elementSelector) {
		final WebDriverWait webDriverWait = new WebDriverWait(driver,
				DEFAULT_WAIT);
		WebElement webElement;
		if (StringUtils.equalsIgnoreCase(elementSelector, ID)) {
			webElement = webDriverWait.until(ExpectedConditions
					.visibilityOfElementLocated(By.id(element)));
		} else if (StringUtils.equalsIgnoreCase(elementSelector, XPATH)) {
			webElement = webDriverWait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(element)));
		} else if (StringUtils.equalsIgnoreCase(elementSelector, CSS_SELECTOR)) {
			webElement = webDriverWait.until(ExpectedConditions
					.visibilityOfElementLocated(By.cssSelector(element)));
		} else if (StringUtils.equalsIgnoreCase(elementSelector, LINK_TEXT)) {
			webElement = webDriverWait.until(ExpectedConditions
					.visibilityOfElementLocated(By.linkText(element)));
		} else if (StringUtils.equalsIgnoreCase(elementSelector,
				PARTIAL_LINK_TEXT)) {
			webElement = webDriverWait.until(ExpectedConditions
					.visibilityOfElementLocated(By.partialLinkText(element)));
		} else if (StringUtils.equalsIgnoreCase(elementSelector, NAME)) {
			webElement = webDriverWait.until(ExpectedConditions
					.visibilityOfElementLocated(By.name(element)));
		} else {
			webElement = webDriverWait.until(ExpectedConditions
					.visibilityOfElementLocated(By.id(element)));
		}
		return webElement;
	}

	public static boolean isElementPresent(final String elementId) {
		log.info("is element present: " + elementId);
		final WebElement element = waitForElementPresent(elementId);
		return element.isDisplayed();
	}

	public static void clickAndWait(final String value) {
		clickAndWait(value, ID);
	}

	/**
	 * overload clickAndWait with selector type (i.e. id,name,..)
	 *
	 * @param value
	 */
	public static void clickAndWait(final String value, final String selector) {
		log.info("click and wait on: " + selector);
		try {
			final WebElement element = waitForElementClickable(value, selector);
			element.click();
		} catch (final StaleElementReferenceException e) {
			final WebElement element = waitForElementClickable(value, selector);
			element.click();
		}

	}

	public static WebElement waitForElementClickable(final String element) {
		return waitForElementClickable(element, ID);
	}

	public static WebElement waitForElementClickable(final String element,
			final String selector) {
		final WebDriverWait webDriverWait = new WebDriverWait(driver,
				DEFAULT_WAIT);
		WebElement webElement;
		if (StringUtils.equalsIgnoreCase(selector, NAME)) {
			webElement = webDriverWait.until(ExpectedConditions
					.elementToBeClickable(By.name(element)));
		} else {
			webElement = webDriverWait.until(ExpectedConditions
					.elementToBeClickable(By.id(element)));
		}
		return webElement;
	}

	public static String getValue(final String id) {
		final String value = new WebDriverWait(driver, DEFAULT_WAIT)
				.until((ExpectedCondition<String>) driver -> {
					String value1 = null;
					try {
						final WebElement element1 = driver.findElement(By
								.id(id));
						value1 = element1.getAttribute("value");
					} catch (final StaleElementReferenceException e) {
						final WebElement element2 = driver.findElement(By
								.id(id));
						value1 = element2.getAttribute("value");
					}
					if (StringUtils.isNotBlank(value1)) {
						return value1;
					}
					return null;
				});
		return value;
	}

	/**
	 * value may be null or blank
	 */
	public static String getNullableValue(final String id) {
		final String value = new WebDriverWait(driver, DEFAULT_WAIT)
				.until((ExpectedCondition<String>) driver -> {
					String value1 = null;
					try {
						final WebElement element1 = driver.findElement(By
								.id(id));
						value1 = element1.getAttribute("value");
					} catch (final StaleElementReferenceException e) {
						final WebElement element2 = driver.findElement(By
								.id(id));
						value1 = element2.getAttribute("value");
					}

					return value1;

				});
		return value;
	}

	public static String getSpanValue(final String id) {
		String value = null;
		waitForElementPresent(id);
		final long end_time = System.currentTimeMillis() + DEFAULT_TIMEOUT;
		while (System.currentTimeMillis() < end_time) {
			try {
				final WebElement element = driver.findElement(By.id(id));
				value = element.getAttribute("value");
			} catch (final StaleElementReferenceException e) {
				final WebElement element = driver.findElement(By.id(id));
				value = element.getAttribute("value");
			}
			if (StringUtils.isNotBlank(value)) {
				return value;
			}
		}
		return null;
	}

	public static boolean isEnabled(final String id) {
		final WebElement element = waitForElementPresent(id);
		return element.isEnabled();
	}

	public static void selectFromDropDown(final String id,
			final String visibleValue) {
		try {
			isSelectOptionPresent(id, visibleValue);
			final WebElement element = waitForElementPresent(id);
			final Select dropdown = new Select(element);
			dropdown.selectByVisibleText(visibleValue);
		} catch (final StaleElementReferenceException e) {
			final WebElement element = waitForElementPresent(id);
			final Select dropdown = new Select(element);
			dropdown.selectByVisibleText(visibleValue);
		}
	}

	public static void multiSelectFromDropDown(final String id,
			final List<String> visibleValues) {
		try {
			for (final String visibleValue : visibleValues) {
				isSelectOptionPresent(id, visibleValue);
			}
			final WebElement element = waitForElementPresent(id);
			final List<WebElement> lstOptions = element.findElements(By
					.tagName("option"));
			element.sendKeys(Keys.CONTROL);
			final Actions builder = new Actions(driver);
			builder.keyDown(Keys.CONTROL);
			for (final WebElement webElement : lstOptions) {
				final String option = webElement.getText();
				if (visibleValues.contains(option)) {
					builder.click(webElement);
				}
			}
			builder.keyUp(Keys.CONTROL);
			builder.build().perform();
		} catch (final StaleElementReferenceException e) {
		}
	}

	public static void type(final String id, final String text) {
		final WebElement element = waitForElementPresent(id);
		element.clear();
		element.sendKeys(text);
	}

	public static void pressKey(final String id, final Keys key) {
		final WebElement element = waitForElementPresent(id);
		element.sendKeys(key);
	}

	public static void enterDateInSelector(final String elementId,
			final String dd, final String mm, final String yyyy) {
		final WebElement element = driver.findElement(By.id(elementId));
		final String dateFormat = System.getProperty("listing.dateFormat");
		boolean datePickerTypeIsText;
		datePickerTypeIsText = SeleniumConnector.getAttribute("type",
				elementId, "id").equals("text") ? true : false;
		if (datePickerTypeIsText) {
			element.clear();
		}
		log.info("Date Format  : " + dateFormat);
		if (StringUtils.isNotBlank(dateFormat) && dateFormat.equals("mmddyyyy")) {
			element.sendKeys(mm + "/" + dd + "/" + yyyy);
		} else if (StringUtils.isNotBlank(dateFormat)
				&& dateFormat.equals("yyyymmdd")) {
			element.sendKeys(yyyy + "/" + mm + "/" + dd);
		} else if (StringUtils.isNotBlank(dateFormat)
				&& dateFormat.equals("ddmmyyyy")) {
			element.sendKeys(dd + "/" + mm + "/" + yyyy);
		} else {
			element.sendKeys(dd + "/" + mm + "/" + yyyy);
			log.info("Date Format used : ddmmyyyy");
		}
		if (datePickerTypeIsText) {
			String dateCloseButton = ".//*[@id='" + elementId
					+ "']/..//button[text()='Close']";
			SeleniumConnector.waitForElementPresent(dateCloseButton, XPATH);
			SeleniumConnector.clickAndWaitBySelector(dateCloseButton, XPATH);
			SeleniumConnector.waitForElementToDisappear(dateCloseButton, XPATH);
		} else {
			element.sendKeys(Keys.TAB);
		}
	}

	public static void enterDateInSelector(final String elementId,
			final Date date) {
		final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final String dateString = df.format(date);
		final String[] dateArr = dateString.split("/");
		enterDateInSelector(elementId, dateArr[0], dateArr[1], dateArr[2]);
	}

	public static void setInputValue(final String id, final String value) {
		try {
			final WebElement element = waitForElementPresent(id);
			waitForElementClickable(id);
			element.clear();
			element.sendKeys(value);
		} catch (final StaleElementReferenceException e) {
			final WebElement element = waitForElementPresent(id);
			element.clear();
			element.sendKeys(value);
		}
	}

	public static List<WebElement> getOptionForSelect(final String id) {
		final WebElement element = waitForElementPresent(id);
		final Select dropdown = new Select(element);
		return dropdown.getOptions();
	}

	public static boolean isSelectOptionPresent(final String selectElementId,
			final String selectOption) {
		final WebDriverWait webDriverWait = new WebDriverWait(driver,
				DEFAULT_WAIT);
		return webDriverWait.until(ExpectedConditions
				.textToBePresentInElementLocated(By.id(selectElementId),
						selectOption));
	}

	public static void reloadPage() {
		driver.navigate().refresh();
	}

	public static String getSelectedOption(final String selectElementId) {
		final Select selectElement = new Select(
				waitForElementPresent(selectElementId));
		final WebElement selectOption = selectElement.getFirstSelectedOption();
		return selectOption.getText();
	}

	public static boolean isSelectOptionNotPresent(
			final String selectElementId, final String selectOption) {
		final WebDriverWait webDriverWait = new WebDriverWait(driver,
				DEFAULT_WAIT);
		return webDriverWait.until(ExpectedConditions
				.invisibilityOfElementWithText(By.id(selectElementId),
						selectOption));
	}

	public static void typeWithoutClear(final String id, final String text) {
		final WebElement element = waitForElementPresent(id);
		element.sendKeys(text);
	}

	public static void clearInput(final String id) {
		final WebElement element = waitForElementPresent(id);
		element.clear();
		element.sendKeys("");
	}

	public static void waitForTextToAppear(final String id,
			final String textToAppear) {
		final WebElement element = waitForElementPresent(id);
		final WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT);
		wait.until(ExpectedConditions.textToBePresentInElementValue(element,
				textToAppear));
	}

	public static String getTextFromTable(final String tableId,
			final int tableRow, final int tableColumn) {
		waitForElementPresent(tableId);
		final String xpathString = ".//*[@id = '" + tableId + "']//tbody/tr["
				+ tableRow + "]/td[" + tableColumn + "]";
		WebElement cell = driver.findElement(By.xpath(xpathString));
		try {
			return cell.getText();
		} catch (final StaleElementReferenceException e) {
			cell = driver.findElement(By.xpath(xpathString));
			return cell.getText();
		}
	}

	public static void clickOnTable(final String tableId, final int tableRow,
			final int tableColumn) {
		waitForElementPresent(tableId);
		final WebElement cell = driver.findElement(By.xpath(".//*[@id = '"
				+ tableId + "']//tbody/tr[" + tableRow + "]/td[" + tableColumn
				+ "]/a"));
		cell.click();
	}

	public static void clickAndWaitBySelector(final String selector,
			final String selectorType) {
		log.info("click and wait on: " + selector);
		WebElement element = waitForElementClickableBySelector(selector,
				selectorType);
		try {
			element.click();
		} catch (final StaleElementReferenceException e) {
			element = waitForElementClickableBySelector(selector, selectorType);
			element.click();
		}
	}

	public static WebElement waitForElementClickableBySelector(
			final String elementSelector, final String selectorType) {
		final WebDriverWait webDriverWait = new WebDriverWait(driver,
				DEFAULT_WAIT);
		WebElement webElement;
		if (StringUtils.equalsIgnoreCase(selectorType, ID)) {
			webElement = webDriverWait.until(ExpectedConditions
					.elementToBeClickable(By.id(elementSelector)));
		} else if (StringUtils.equalsIgnoreCase(selectorType, XPATH)) {
			webElement = webDriverWait.until(ExpectedConditions
					.elementToBeClickable(By.xpath(elementSelector)));
		} else if (StringUtils.equalsIgnoreCase(selectorType, CSS_SELECTOR)) {
			webElement = webDriverWait.until(ExpectedConditions
					.elementToBeClickable(By.cssSelector(elementSelector)));
		} else if (StringUtils.equalsIgnoreCase(selectorType, LINK_TEXT)) {
			webElement = webDriverWait.until(ExpectedConditions
					.elementToBeClickable(By.linkText(elementSelector)));
		} else if (StringUtils
				.equalsIgnoreCase(selectorType, PARTIAL_LINK_TEXT)) {
			webElement = webDriverWait.until(ExpectedConditions
					.elementToBeClickable(By.partialLinkText(elementSelector)));
		} else if (StringUtils.equalsIgnoreCase(selectorType, NAME)) {
			webElement = webDriverWait.until(ExpectedConditions
					.elementToBeClickable(By.name(elementSelector)));
		} else {
			webElement = webDriverWait.until(ExpectedConditions
					.elementToBeClickable(By.id(elementSelector)));
		}
		return webElement;
	}

	public static String getTextFromElementBySelector(
			final String elementSelector, final String selectorType) {
		String result = "";
		WebElement webElement = getWebElement(elementSelector, selectorType);
		// Doing this because we are getting the stale element exception, that
		// means that the object may have changed on the page, so we are getting
		// it again
		try {
			result = webElement.getText();
		} catch (final StaleElementReferenceException e) {
			webElement = getWebElement(elementSelector, selectorType);
			result = webElement.getText();
		}
		return result;
	}

	private static WebElement getWebElement(final String elementSelector,
			final String selectorType) {
		WebElement webElement;
		if (StringUtils.equalsIgnoreCase(selectorType, ID)) {
			webElement = driver.findElement(By.id(elementSelector));
		} else if (StringUtils.equalsIgnoreCase(selectorType, XPATH)) {
			webElement = driver.findElement(By.xpath(elementSelector));
		} else if (StringUtils.equalsIgnoreCase(selectorType, CSS_SELECTOR)) {
			webElement = driver.findElement(By.cssSelector(elementSelector));
		} else if (StringUtils.equalsIgnoreCase(selectorType, LINK_TEXT)) {
			webElement = driver.findElement(By.linkText(elementSelector));
		} else if (StringUtils
				.equalsIgnoreCase(selectorType, PARTIAL_LINK_TEXT)) {
			webElement = driver
					.findElement(By.partialLinkText(elementSelector));
		} else {
			webElement = driver.findElement(By.id(elementSelector));
		}
		return webElement;
	}

	public static void clickLinkByPartialText(final String partialLinkText) {
		driver.findElement(By.partialLinkText(partialLinkText)).click();
	}

	public static boolean isElementAbsent(final String elementSelector,
			final String selectorType) {
		List<WebElement> webElements;
		boolean absent;
		if (StringUtils.equalsIgnoreCase(selectorType, ID)) {
			webElements = driver.findElements(By.id(elementSelector));
		} else if (StringUtils.equalsIgnoreCase(selectorType, XPATH)) {
			webElements = driver.findElements(By.xpath(elementSelector));
		} else if (StringUtils.equalsIgnoreCase(selectorType, CSS_SELECTOR)) {
			webElements = driver.findElements(By.cssSelector(elementSelector));
		} else if (StringUtils.equalsIgnoreCase(selectorType, LINK_TEXT)) {
			webElements = driver.findElements(By.linkText(elementSelector));
		} else if (StringUtils
				.equalsIgnoreCase(selectorType, PARTIAL_LINK_TEXT)) {
			webElements = driver.findElements(By
					.partialLinkText(elementSelector));
		} else {
			webElements = driver.findElements(By.id(elementSelector));
		}
		absent = 0 == webElements.size() ? true : false;
		return absent;
	}

	// Wait for an element to disappear from DOM
	public static void waitForElementToDisappear(final String elementSelector,
			final String selectorType) {
		final WebDriverWait webDriverWait = new WebDriverWait(driver,
				DEFAULT_WAIT);
		if (StringUtils.equalsIgnoreCase(selectorType, ID)) {
			webDriverWait.until(ExpectedConditions
					.invisibilityOfElementLocated(By.id(elementSelector)));
		} else if (StringUtils.equalsIgnoreCase(selectorType, XPATH)) {
			webDriverWait.until(ExpectedConditions
					.invisibilityOfElementLocated(By.xpath(elementSelector)));
		} else if (StringUtils.equalsIgnoreCase(selectorType, CSS_SELECTOR)) {
			webDriverWait.until(ExpectedConditions
					.invisibilityOfElementLocated(By
							.cssSelector(elementSelector)));
		} else if (StringUtils.equalsIgnoreCase(selectorType, LINK_TEXT)) {
			webDriverWait
					.until(ExpectedConditions.invisibilityOfElementLocated(By
							.linkText(elementSelector)));
		} else if (StringUtils
				.equalsIgnoreCase(selectorType, PARTIAL_LINK_TEXT)) {
			webDriverWait.until(ExpectedConditions
					.invisibilityOfElementLocated(By
							.partialLinkText(elementSelector)));
		} else {
			webDriverWait.until(ExpectedConditions
					.invisibilityOfElementLocated(By.id(elementSelector)));
		}
	}

	/*
	 * Wait for an element to be selected.
	 * 
	 * @param elementSelector Selector used for identifying the element
	 * 
	 * @param selectorType Type of the above Selector. E.g id, name,
	 * cssselector, xpath etc
	 */
	public static boolean waitForElementToBeSelected(
			final String elementSelector, final String selectorType) {
		final WebDriverWait webDriverWait = new WebDriverWait(driver,
				DEFAULT_WAIT);
		boolean isSelected;
		if (StringUtils.equalsIgnoreCase(selectorType, ID)) {
			isSelected = webDriverWait.until(ExpectedConditions
					.elementToBeSelected(By.id(elementSelector)));
		} else if (StringUtils.equalsIgnoreCase(selectorType, XPATH)) {
			isSelected = webDriverWait.until(ExpectedConditions
					.elementToBeSelected(By.xpath(elementSelector)));
		} else if (StringUtils.equalsIgnoreCase(selectorType, CSS_SELECTOR)) {
			isSelected = webDriverWait.until(ExpectedConditions
					.elementToBeSelected(By.cssSelector(elementSelector)));
		} else if (StringUtils.equalsIgnoreCase(selectorType, LINK_TEXT)) {
			isSelected = webDriverWait.until(ExpectedConditions
					.elementToBeSelected(By.linkText(elementSelector)));
		} else if (StringUtils
				.equalsIgnoreCase(selectorType, PARTIAL_LINK_TEXT)) {
			isSelected = webDriverWait.until(ExpectedConditions
					.elementToBeSelected(By.partialLinkText(elementSelector)));
		} else {
			isSelected = webDriverWait.until(ExpectedConditions
					.elementToBeSelected(By.id(elementSelector)));
		}
		return isSelected;
	}

	// Wait for a specific option to be selected in a select element
	public static boolean waitForOptionSelectedInSelectElement(
			final String selectId, final String optionText) {
		final WebDriverWait webDriverWait = new WebDriverWait(driver,
				DEFAULT_WAIT);
		boolean isSelected;
		final String optionXpath = ".//*[@id = '" + selectId
				+ "']/option[text() = '" + optionText + "']";
		isSelected = webDriverWait.until(ExpectedConditions
				.elementToBeSelected(By.xpath(optionXpath)));
		return isSelected;
	}

	/*
	 * Wait for attribute of a web element to be set to a particular value.
	 * Attributes can be value, text, id etc E.g Wait for value of an input
	 * element to be set to a specific value. Attributes can be value, text, id
	 * etc E.g Wait for value of an input element to be set to a specific value.
	 * 
	 * @param elementId Id of the element to wait for attribute
	 * 
	 * @param attribute Attribute name to wait for
	 * 
	 * @param value Value of the attribute to wait for
	 */
	public static void waitForElementAttribute(final String elementId,
			final String attribute, final String value) {
		final WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT);
		wait.until((ExpectedCondition<Boolean>) driver -> {
			WebElement elem;
			String attrib;
			try {
				elem = driver.findElement(By.id(elementId));
				attrib = elem.getAttribute(attribute);
			} catch (final StaleElementReferenceException e) {
				elem = driver.findElement(By.id(elementId));
				attrib = elem.getAttribute(attribute);
			}
			if (attrib.equals(value)) {
				return true;
			} else {
				return false;
			}
		});
	}

	public static boolean waitForElementAttributeByLooping(
			final String elementId, final String attribute, final String value) {
		final long end_time = System.currentTimeMillis() + DEFAULT_TIMEOUT;
		while (System.currentTimeMillis() < end_time) {
			try {
				waitForElementPresent(elementId);
				final String attrib = driver.findElement(By.id(elementId))
						.getAttribute(attribute);
				if (attrib.equals(value)) {
					return true;
				} else {
					return false;
				}
			} catch (final StaleElementReferenceException e) {
			}
		}
		return false;
	}

	/*
	 * Return the attribute of a web element identified by a locator. Attribute
	 * for web element can be id, text, value etc Return the attribute of a web
	 * element identified by a locator. Attribute for web element can be id,
	 * text, value etc
	 * 
	 * @param attributeName Name of the attribute. E.g. id, value, text etc
	 * 
	 * @param locator Selector used for identifying the element
	 * 
	 * @param locatorType Type of the above Selector. E.g id, name, cssselector,
	 * xpath etc
	 * 
	 * @return Attribute of the element
	 */
	public static String getAttribute(final String attributeName,
			final String selector, final String locatorType) {
		String elementAttribute = null;
		final WebElement element = waitForElementClickableBySelector(selector,
				locatorType);
		elementAttribute = element.getAttribute(attributeName);
		return elementAttribute;
	}

	/*
	 * Set the path of the file in the input element to a specified file path.
	 * 
	 * @param filePath Path of file.
	 * 
	 * @param inputId Id of the input element.
	 */
	public static void setFileBrowsePath(final String filePath,
			final String inputId) {
		String decodedPath = "";
		try {
			decodedPath = URLDecoder.decode(filePath, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			log.info("Exception while decoding filePath: " + filePath);
		}
		final WebElement fileInput = waitForElementClickableBySelector(inputId,
				"id");
		fileInput.sendKeys(decodedPath);
	}

	/*
	 * Set the value of an Input element using any Selector.
	 * 
	 * @param selector Selector used for identifying the element
	 * 
	 * @value value to be set for the Input element
	 * 
	 * @param selectorType Type of the above Selector. E.g id, name,
	 * cssselector, xpath etc
	 */
	public static void setInputValueBySelector(final String selector,
			final String value, final String selectorType) {
		try {
			final WebElement element = waitForElementClickableBySelector(
					selector, selectorType);
			element.clear();
			element.sendKeys(value);
		} catch (final StaleElementReferenceException e) {
			final WebElement element = waitForElementClickableBySelector(
					selector, selectorType);
			element.clear();
			element.sendKeys(value);
		}
	}

	/*
	 * Return the number of rows in a table body
	 * 
	 * @param tableId Id of the table
	 * 
	 * @return Number of rows in table body
	 */
	public static int getNumberOfTableBodyRows(final String tableId) {
		int noRows = 0;
		final String rowXpath = ".//*[@id='" + tableId + "']//tbody/tr";
		try {
			noRows = driver.findElements(By.xpath(rowXpath)).size();
		} catch (final Exception e) {
		}
		return noRows;
	}

	/*
	 * Scroll page to bring an element into view
	 * 
	 * @param elementId Id of the element
	 */
	public static void scrollToElement(final String elementId) {
		final WebElement adminMenu = driver.findElement(By
				.xpath(".//a[@href='#/admin']"));
		final Actions actions = new Actions(driver);
		actions.moveToElement(adminMenu, 500, 0).click().perform();

		// Page Up is usually not required. Adding it as Chrome is not
		// automatically
		// scrolling up.
		actions.sendKeys(Keys.PAGE_UP, Keys.PAGE_UP).perform();
		try {
			Thread.sleep(2000);
		} catch (final InterruptedException e) {
		}
		final WebElement elementToScrollTo = driver.findElement(By
				.id(elementId));
		waitForElementClickable(elementId);
		actions.moveToElement(elementToScrollTo).perform();
		try {
			Thread.sleep(2000);
		} catch (final InterruptedException e) {
		}
	}

	public static void openAndWaitForManageCase() {
		SeleniumConnector.openAndWait("manage-case");
	}
}
