package selenium.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import selenium.test.base.AbstractTestBase;
import selenium.test.util.XPathBuilder;

public class ReservationsPageTest extends AbstractTestBase {

	private static final String RESERVATIONS_URL = "https://rezerwacja.zielona-gora.pl";

	@Test
	void should_makeReservation() {
		// when
		waitForElementToBeVisible(By.className("submit-button-step-1"));

		// Zamkniecie powiadomienia o ciasteczkach (uniemozliwia klikanie w elementy)
		findElement(By.className("close")).click();

		chooseService();
		chooseDayAndHour();
		insertDataIntoTextFields();

		// Przejscie do podsumowania
		findElement(By.className("submit-button-step-1")).click();

		// then
		waitForElementToBeVisible(By.className("inquiry-title"));
		assertThat(findElement(By.className("inquiry-title")).getText()).isEqualTo("Twoje zamówienie");

	}

	@Test
	void should_getValidationError_whenRequiredTextFields_haveNoValues() {
		// when
		waitForElementToBeVisible(By.className("submit-button-step-1"));

		// Zamkniecie powiadomienia o ciasteczkach (uniemozliwia klikanie w elementy)
		findElement(By.className("close")).click();

		chooseService();
		chooseDayAndHour();

		// Przejscie do podsumowania
		findElement(By.className("submit-button-step-1")).click();

		// then
		waitForElementToBeVisible(By.className("error-block"));
		assertThat(findElement(By.className("error-block")).getText()).contains("Formularz zawiera błędy:");
	}

	private void chooseService() {
		String pathToServiceNameField = new XPathBuilder().element("li")
				.className("multiselect__element")
				.descendant("span")
				.text("Rejestracja - nowy pojazd dotychczas niezarejestrowany")
				.build();
		findElement(By.className("multiselect")).click();
		findElement(By.xpath(pathToServiceNameField)).click();
	}

	private void chooseDayAndHour() {
		waitForRequestedTimeInMilliseconds(1000);
		scrollToElement(findElement(By.cssSelector(".is-valid")));
		findElements(By.cssSelector(".calendar-days-list-cell.is-valid")).getFirst().click();

		waitForElementToBeVisible(By.className("hours-list-item"));
		findElements(By.className("hours-list-item")).getFirst().click();
	}

	private void insertDataIntoTextFields() {
		String pathToVechicleOwnerField = new XPathBuilder().element("label")
				.text("Dane właściciela pojazdu")
				.followingSibling("input")
				.build();

		String pathToRegistrationNumberField = new XPathBuilder().element("label")
				.text("Nr rej. pojazdu lub w przypadku pojazdu nowego i z zagranicy 5 ostatnich cyfr nr VIN")
				.followingSibling("input")
				.build();

		findElement(By.xpath(pathToVechicleOwnerField)).sendKeys("Jan Kowalski");
		findElement(By.xpath(pathToRegistrationNumberField)).sendKeys("FZG12345");
	}

	@Override
	public String getUrl() {
		return RESERVATIONS_URL;
	}

}
