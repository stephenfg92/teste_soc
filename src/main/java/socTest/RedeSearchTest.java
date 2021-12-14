package socTest;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class RedeSearchTest extends BaseWebDriverTest {
	private RedeSearchPage redeSearchPage;

	@Before
	public void init() {
		driver.get("https://socnet.soc.com.br/");
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));
		driver.manage().window().setSize(new Dimension(1920, 1080));

		redeSearchPage = new RedeSearchPage(driver);
	}

	@Test
	public void verifyFilters() throws InterruptedException{
		redeSearchPage.searchFor("11013-001");
		redeSearchPage.expandFiltrosConveniencia();

		redeSearchPage.selectFiltro();
		redeSearchPage.selectFiltro();

		assertTrue(redeSearchPage.isFilterSelected());

		redeSearchPage.selectResult();
		Thread.sleep(1000);
		takeScreenshot("Result-click-test");

		driver.navigate().back();
		redeSearchPage.resetFiltroIter();

		//redeSearchPage.deselectFiltro();
	}

}
