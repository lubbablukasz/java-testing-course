package selenium.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import selenium.test.base.AbstractTestBase;

public class PlanPageTest extends AbstractTestBase {

	private static final String PLAN_PAGE_URL = "http://www.plan.uz.zgora.pl";

	@Test
	void should_enterTeachersPlan() {
		assertValidPageRedirection("Plan nauczycieli", "Szukaj nauczyciela wg pierwszej litery nazwiska");
	}

	@Test
	void should_enterGroupsPlan() {
		assertValidPageRedirection("Plan grup", "Plan grup - lista kierunków");
	}

	@Test
	void should_enterClassroomPlan() {
		assertValidPageRedirection("Plan sal", "Plan sal - lista budynków (z salami dydaktycznymi)");
	}

	@Test
	void should_enterCallendars() {
		assertValidPageRedirection("Kalendarze", "Lista kalendarzy - semestr zimowy 2024/2025");
	}

	@Test
	void should_enterPlanners() {
		assertValidPageRedirection("Osoby układające plan zajęć", "Osoby układające plan zajęć");
	}

	@Test
	void should_goToMainPage_whenTitleClicked() {
		// given
		List<String> expectedValues = List.of("Plan nauczycieli", "Plan grup", "Plan sal", "Kalendarze",
				"Osoby układające plan zajęć");

		// when
		loadPage("http://www.plan.uz.zgora.pl/grupy_lista_kierunkow.php");
		findElement(By.className("navbar-brand")).click();

		// then
		List<WebElement> elements = findElements(By.className("col-md-4"));
		assertThat(elements.stream().map(WebElement::getText)).allMatch(value -> expectedValues.contains(value));
	}

	@Test
	void should_goToUniversityPage_whenLogoClicked() {
		// when
		findElement(By.tagName("img")).click();

		// then
		waitForPage();
		assertThat(findElement(By.id("drop1")).getText()).isEqualTo("UCZELNIA");
	}

	private void assertValidPageRedirection(String buttonCaption, String expectedLabelCaption) {
		WebElement button = findElement(By.linkText(buttonCaption));
		button.click();
		WebElement label = findElement(By.tagName("h3"));
		assertThat(label.getText()).isEqualTo(expectedLabelCaption);
	}

	public String getUrl() {
		return PLAN_PAGE_URL;
	}
}
