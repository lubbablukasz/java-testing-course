package selenium.test.base;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import selenium.test.driver.PageLoadCondition;
import selenium.test.driver.WebDriverManager;

public abstract class AbstractTestBase {

	private static final Duration WAIT_TIME = Duration.ofSeconds(10);

	private WebDriverWait wait;
	private PageLoadCondition pageLoadCondition = new PageLoadCondition();
	private static WebDriver driver;

	/**
	 * Zwraca bazowy URL strony, która ma być aktualnie testowana
	 */
	public abstract String getUrl();

	@BeforeAll
	static void init() {
		driver = WebDriverManager.getNewDriver();
	}

	@BeforeEach
	void start() {
		loadPage(getUrl());
		waitForPage();
	}

	@AfterAll
	static void tearDown() {
		WebDriverManager.tearDownDriver(driver);
	}

	protected WebElement findElement(By by) {
		return driver.findElement(by);
	}

	protected List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	protected void loadPage(String url) {
		if (Objects.nonNull(driver)) {
			driver.get(url);
		}
	}

	protected void waitForPage() {
		wait = new WebDriverWait(driver, WAIT_TIME);
		wait.until(pageLoadCondition);
	}
}
