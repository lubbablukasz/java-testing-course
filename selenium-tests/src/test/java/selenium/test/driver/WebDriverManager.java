package selenium.test.driver;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverManager {

	private static final String CHROME_DRIVER_PATH = "C:\\Users\\lukas\\Desktop\\chrome-win64\\chromedriver.exe";
	private static List<String> driverOptions = List.of("--headless", "--disable-gpu");

	public static WebDriver getNewDriver() {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
		ChromeDriver driver = new ChromeDriver(getDriverOptions());
		driver.manage().window().maximize();
		return driver;
	}

	private static ChromeOptions getDriverOptions() {
		ChromeOptions options = new ChromeOptions();
//		options.addArguments(driverOptions);
		return options;
	}

	public static void tearDownDriver(WebDriver driver) {
		driver.quit();
	}
}
