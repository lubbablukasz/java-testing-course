package selenium.test.base;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import selenium.test.driver.PageLoadCondition;
import selenium.test.driver.WebDriverManager;

public abstract class AbstractTestBase {

	private static final Duration WAIT_TIME = Duration.ofSeconds(10);

	private static WebDriver driver;

	/**
	 * Zwraca bazowy URL strony, która ma być aktualnie testowana
	 */
	public abstract String getUrl();

	@BeforeAll
	static void init() {
		driver = WebDriverManager.getNewDriver();
	}

	@AfterAll
	static void tearDown() {
		WebDriverManager.tearDownDriver(driver);
	}

	@BeforeEach
	protected void start() {
		loadPage(getUrl());
		waitForPage();
	}

	protected String getTitle() {
		return driver.getTitle();
	}

	protected String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	protected WebElement findElement(By by) {
		return driver.findElement(by);
	}

	protected List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	protected void loadPage(String url) {
		driver.get(url);
	}

	protected void scrollToElement(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	protected void forceClickElement(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	/**
	 * Użycie metody tylko w ostateczności (tzn najbardziej problemowym przypadku,
	 * gdzie elementów nie można namierzyć bez odczekania chwili)
	 */
	protected void waitForRequestedTimeInMilliseconds(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
	}

	protected void waitForPage() {
		getWait().until(new PageLoadCondition());
	}

	protected void waitForElementToBeClickable(By by) {
		getWait().until(ExpectedConditions.elementToBeClickable(by));
	}

	protected void waitForElementToBeVisible(By by) {
		getWait().until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	protected void waitForElementToHaveClass(By by, String className) {
		getWait().until(ExpectedConditions.attributeContains(by, "class", className));
	}

	protected void assertElementIsNotPresent(By by) {
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> findElement(by));
	}

	private WebDriverWait getWait() {
		return new WebDriverWait(driver, WAIT_TIME);
	}
}
