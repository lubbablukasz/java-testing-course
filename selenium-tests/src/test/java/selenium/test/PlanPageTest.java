package selenium.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import selenium.test.base.AbstractTestBase;

public class PlanPageTest extends AbstractTestBase {

	private static final String PLAN_PAGE_URL = "http://www.plan.uz.zgora.pl";

	@Override
	public String getUrl() {
		return PLAN_PAGE_URL;
	}

	@Test
	void should_enterTeachersPlan() {
		// given
		WebElement teachersPlanButton = driver.findElement(By.linkText("Plan nauczycieli"));

		// when
		teachersPlanButton.click();

		// then
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//		WebElement teachersPlanLabel = driver
//				.findElement(By.linkText("Szukaj nauczyciela wg pierwszej litery nazwiska"));
		WebElement teachersPlanLabel = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.linkText("Szukaj nauczyciela wg pierwszej litery nazwiska")));
		assertThat(teachersPlanLabel).isNotNull();
	}

}
