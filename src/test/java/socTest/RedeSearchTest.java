package socTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;

import java.time.Duration;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;

public class RedeSearchTest extends BaseWebDriverTest {
	private RedeSearchPage redeSearchPage;

	@Before
	public void init() {
		driver.get("https://socnet.soc.com.br/");
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));
		driver.manage().window().setSize(new Dimension(1920, 1080));

		redeSearchPage = new RedeSearchPage(driver);
	}

	@Test public void verificarPagina(){
		// Verifica se o título da página é o esperado.
		assertThat(driver.getTitle(), containsString("Rede Credenciada SOC"));

		// Verifica se o título visualizado no topo da página é o correto
		assertThat(redeSearchPage.getTitleClass().getText(), containsString("Rede Credenciada SOCNET"));

		// Verifica se a pagina é inicializada sem resultados visíveis
		assertTrue(redeSearchPage.getNoResultsVisibility());

		takeScreenshot("Rede Inicio");
	}

	@Test
	public void verificarFiltrosConveniencia() throws InterruptedException{
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
