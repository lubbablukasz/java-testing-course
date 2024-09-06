package selenium.test.driver;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverManager {

	private static final String CHROME_DRIVER_PATH = "C:\\Users\\lukas\\Desktop\\chrome-win64\\chromedriver.exe";
	private static Boolean ADD_OPTIONS = false;
	private static List<String> DRIVER_OPTIONS = List.of("--headless", "--disable-gpu");

	public static WebDriver getNewDriver() {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
		ChromeDriver driver = createChromeDriver();
		driver.manage().window().maximize();
		return driver;
	}

	private static ChromeDriver createChromeDriver() {
		return ADD_OPTIONS ? new ChromeDriver(configureDriverOptions()) : new ChromeDriver();
	}

	private static ChromeOptions configureDriverOptions() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments(DRIVER_OPTIONS);
		return options;
	}

	public static void tearDownDriver(WebDriver driver) {
		driver.quit();
	}
}
