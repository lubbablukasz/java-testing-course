package selenium.test.base;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import selenium.test.driver.PageLoadCondition;
import selenium.test.driver.WebDriverManager;

public abstract class AbstractTestBase {

	private static final Duration WAIT_TIME = Duration.ofSeconds(10);

	private WebDriverWait wait;
	private PageLoadCondition pageLoadCondition = new PageLoadCondition();
	protected WebDriver driver;

	@BeforeEach
	public void init() {
		driver = WebDriverManager.getNewDriver();
		driver.get(getUrl());
//		waitForPage();
	}

	@AfterEach
	public void tearDown() {
		WebDriverManager.tearDownDriver(driver);
	}

	protected void waitForPage() {
		wait = new WebDriverWait(driver, WAIT_TIME);
		wait.until(pageLoadCondition);
	}

	public abstract String getUrl();

}
