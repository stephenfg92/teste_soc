package socTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
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
		// Verifica se o t?tulo da p?gina ? o esperado.
		assertThat(driver.getTitle(), containsString("Rede Credenciada SOC"));

		// Verifica se o t?tulo visualizado no topo da p?gina ? o correto
		assertThat(redeSearchPage.getTitleClass().getText(), containsString("Rede Credenciada SOCNET"));

		// Verifica se a pagina ? inicializada sem resultados vis?veis
		assertTrue(redeSearchPage.getNoResultsVisibility());

		takeScreenshot("Rede Inicio");
	}

	@Test
	public void verificarFiltrosConveniencia() throws InterruptedException{
		redeSearchPage.searchFor("11013-001");
		redeSearchPage.expandFiltrosConveniencia();

		redeSearchPage.selectFiltroByIndex(1);

		assertTrue(redeSearchPage.isFilterSelected());

		String nomeEmpresa = redeSearchPage.getResultTitleByIndex(0);
		redeSearchPage.selectResultByIndex(0);
		Thread.sleep(1000);

		assertThat(nomeEmpresa, containsString(redeSearchPage.getResultProfileTitleTruncated()));
		assertThat(redeSearchPage.getResultProfileTitle(), equalToIgnoringCase(nomeEmpresa));

		takeScreenshot("Result-click-test");

		driver.navigate().back();
	}

}
