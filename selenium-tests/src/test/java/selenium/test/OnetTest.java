package selenium.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import selenium.test.base.AbstractTestBase;
import selenium.test.util.XPathBuilder;

public class OnetTest extends AbstractTestBase {

	private static final String ONET_URL = "https://www.onet.pl";

	@BeforeEach
	protected void start() {
		super.start();
		closeCookiesWindow();
	}

	@Test
	void should_haveRightTitle() {
		// then
		assertThat(getTitle()).isNotEmpty().isEqualTo("Onet – Jesteś na bieżąco");
	}

	@Test
	void should_goToNewsSection() {
		// given
		String pathToNews = new XPathBuilder().element("a").text("Wiadomości").build();

		// when
		// przejscie do sekcji wiadomosci
		findElement(By.xpath(pathToNews)).click();

		// then
		waitForPage();
		assertThat(getCurrentUrl()).contains("wiadomosci");
	}

	@Test
	void should_properlySearchForPhrase() {
		// when
		WebElement searchField = findElement(By.cssSelector(".Search_field__WvEQR"));
		searchField.click();
		searchField.sendKeys("sport");
		searchField.submit();

		// then
		waitForPage();
		assertThat(getCurrentUrl()).contains("sport");
	}

	@Test
	void should_properlyFindFooter_clickLink() {
		// given
		String footerPath = new XPathBuilder().element("footer")
											  .descendant("a")
											  .text("Prywatność")
											  .build();

		// when
		WebElement footerLink = findElement(By.xpath(footerPath));
		scrollToElement(footerLink);
		footerLink.click();

		// then
		waitForPage();
		String currentUrl = getCurrentUrl();
		assertThat(currentUrl).contains("polityka-prywatnosci");
	}

	@Test
	void should_openNotificationsPopup() {
		// otwarcie popupu z powiadomieniami
		findElement(By.className("MenuIcon_menuItemNotifications__9Kz8T")).click();
		waitForElementToBeVisible(By.className("UserNotificationsOnLayer_title__wVYjG"));
		assertThat(findElement(By.className("UserNotificationsOnLayer_title__wVYjG")).getText())
				.isEqualTo("Powiadomienia");

		// zamkniecie popupu z powiadomieniami
		findElement(By.className("MenuIcon_menuItemNotifications__9Kz8T")).click();
		assertElementIsNotPresent(By.className("UserNotificationsOnLayer_title__wVYjG"));
	}

	@Test
	void should_properlyLogin() {
		// given
		// when
		// then
	}

	@Override
	public String getUrl() {
		return ONET_URL;
	}

	private void closeCookiesWindow() {
		try {
			findElement(By.className("cmp-intro_acceptAll")).click();
		} catch (NoSuchElementException ex) {
			// raz zamkniete okno wiecej sie nie pokazuje,
			// wiec po pierwszym uzyciu metoda ta wyrzuca wyjatek
		}
	}
}