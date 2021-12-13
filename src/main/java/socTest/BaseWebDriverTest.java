package socTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.Assert.assertEquals;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

public class BaseWebDriverTest {

	public enum DriverType {
		CHROME, FIREFOX, HEADLESS_CHROME
	}

	protected static WebDriver driver;
	protected static DriverType driverType = DriverType.CHROME;
	protected final static String CHROME_PATH = "webdrivers/chromedriver.exe";
	protected final static String FIREFOX_PATH = "webdrivers/geckodriver.exe";
	protected static DesiredCapabilities capabilities = new DesiredCapabilities();

	protected boolean acceptNextAlert = true;

	@Rule
	public ScreenshotRule screenshotTestRule = new ScreenshotRule();

	@BeforeClass
	public static void prepareDriver() throws Exception {
		ChromeOptions chromeOptions;

		switch (driverType) {
			case CHROME:
				System.setProperty("webdriver.chrome.driver", CHROME_PATH);
				chromeOptions = new ChromeOptions().merge(capabilities);
				driver = new ChromeDriver(chromeOptions);
				break;
			case HEADLESS_CHROME:
				System.setProperty("webdriver.chrome.driver", CHROME_PATH);
				chromeOptions = new ChromeOptions().merge(capabilities).addArguments("--headless");
				driver = new ChromeDriver(chromeOptions);
				break;
			case FIREFOX:
				System.setProperty("webdriver.gecko.driver", FIREFOX_PATH);
				capabilities.setBrowserName(BrowserType.FIREFOX);
				driver = new FirefoxDriver(new FirefoxOptions(capabilities));
				break;
		}
	}

	@AfterClass
	public static void cleanup() {
		driver.quit();
	}

	protected void verifySelectOptions(Select select, String... optionNames){
		List<WebElement> options = select.getOptions();
		assertEquals("Opções", optionNames.length, options.size());
		for (int i = 0; i < optionNames.length; i++) {
			assertEquals("O nome da opção está errado", optionNames[i], options.get(i).getText().trim());
		}
	}

	protected boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e){
			return false;
		}
	}

	protected boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	protected String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	public class ScreenshotRule implements MethodRule {
		public Statement apply(final Statement statement, final FrameworkMethod frameworkMethod, final Object o) {
			return new Statement() {
				@Override
				public void evaluate() throws Throwable {
					try {
						statement.evaluate();
					} catch (Throwable t) {
						takeScreenshot(frameworkMethod.getName() + "-failure");
						throw t;
					}
				}
			};
		}
	}

	protected void takeScreenshot(String name) {
		try {
			String outputDirName = "target/screenshots/";
			System.out.println("Taking screenshot");
			new File(outputDirName).mkdirs();
			File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File outputDir = new File(outputDirName + name + ".png");
			Files.copy(screenshotFile.toPath(), outputDir.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.println("Erro ao criar captura de tela.");
			e.printStackTrace();
		}
	}

}
