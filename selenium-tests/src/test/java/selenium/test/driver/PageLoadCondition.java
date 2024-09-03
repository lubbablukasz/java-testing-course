package selenium.test.driver;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class PageLoadCondition implements ExpectedCondition<Boolean> {

	@Override
	public Boolean apply(WebDriver driver) {
		return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
	}

}
