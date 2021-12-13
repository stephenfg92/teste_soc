package socTest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class BlogSearchTest extends BaseWebDriverTest{
	private BlogSearchPage blogSearchPage;

	@Before
	public void init() {
		driver.get("https://www.soc.com.br/blog/");
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));
		driver.manage().window().setSize(new Dimension(1920, 1080));

		blogSearchPage = new BlogSearchPage(driver);
	}

	@Test
	public void verifyBlogSearchPage(){
		// Verifica se o t�tulo da p�gina � o esperado.
		assertThat(driver.getTitle(), containsString("Blog"));

		// Verifica se o elemento respons�vel por informar inexist�ncia de resultados est� oculto
		assertFalse(blogSearchPage.getNoResultsVisibility());

		// Verifica se o n�mero de posts na p�gina inicial � o esperado.
		assertEquals("N�mero errado de posts na p�gina inicial do blog", 6, blogSearchPage.getPostBoxes().size());

		takeScreenshot("Blog in�cio");
	}

	@Test
	public void verifyBlogSearchFunctionality() {
		for (String title : blogSearchPage.getPostTitlesAsText()) {
			blogSearchPage.searchFor(title);

			// Verifica se o t�tulo da p�gina contem a string utilizada na pesquisa.
			assertThat(driver.getTitle(), containsString(title));
			// Verifica se o t�tulo da pesquisa conte a string utilizada na pesquisa;
			assertThat(blogSearchPage.getPageHeading().getText(), equalToIgnoringCase("Search Results for: " + title));
			// Verifica se o elemento respons�vel por informar inexist�ncia de resultados est� oculto
			assertFalse(blogSearchPage.getNoResultsVisibility());
			// Verifica se o t�tulo do primeiro resultado � igual a string pesquisada.
			assertEquals(title, blogSearchPage.getPostTitlesAsText().get(0));

			takeScreenshot("Search results for " + title.replaceAll("[^a-zA-Z ]", ""));

			driver.navigate().back();
		}
	}

	@Test
	public void verifyEmptyBlogSearch(){
		blogSearchPage.searchFor("");

		// Verifica se pesquisas vazias retornam p�gina de pesquisa gen�rica.
		assertThat(driver.getTitle(), equalToIgnoringCase("Voc� pesquisou por | SOC - Software de Sa�de e Seguran�a do Trabalho"));
		assertThat(blogSearchPage.getPageHeading().getText(), equalToIgnoringCase("Search Results for:"));

		// Verifica se o elemento respons�vel por informar inexist�ncia de resultados est� oculto
		assertFalse(blogSearchPage.getNoResultsVisibility());

		takeScreenshot("Pesquisa vazia");

		driver.navigate().back();
	}

	@Test
	public void verifyInvalidBlogSearch(){

		String reversed = new StringBuilder(blogSearchPage.getPostTitlesAsText().get(0)).reverse().toString();
		blogSearchPage.searchFor(reversed);

		// Verifica se o t�tulo da p�gina � consistente com a pesquisa
		assertThat(driver.getTitle(), containsString("Voc� pesquisou por " + reversed));
		// Verifica se o t�tulo da pesquisa � consistente com a pesquisa
		assertThat(blogSearchPage.getPageHeading().getText(), equalToIgnoringCase("Search Results for: " + reversed));
		// Verifica se o elemento respons�vel por informara inexist�ncia de resultados est� vis�vel
		assertTrue(blogSearchPage.getNoResultsVisibility());

		takeScreenshot("Pesquisa inv�lida");

		driver.navigate().back();
	}

	@Ignore
	@Test
	public void verifyBlogPostCard(){
		List<WebElement> postBoxes = driver.findElements(By.xpath("//div[contains(@class,'elementor-post__card')]"));
		for (WebElement post : postBoxes){
			assertTrue(isElementPresent(By.xpath("//div[contains(@class,'elementor-post__thumbnail__link')]")));
			assertTrue(isElementPresent(By.xpath("//div[contains(@class,'elementor-post__thumbnail')]")));
			assertTrue(isElementPresent(By.xpath("//div[contains(@class,'elementor-post__badge')]")));
			assertTrue(isElementPresent(By.xpath("//div[contains(@class,'elementor-post__text')]")));
			assertTrue(isElementPresent(By.xpath("//div[contains(@class,'elementor-post__title')]")));
			assertTrue(isElementPresent(By.xpath("//div[contains(@class,'elementor-post__excerpt')]")));
		}
	}

}
