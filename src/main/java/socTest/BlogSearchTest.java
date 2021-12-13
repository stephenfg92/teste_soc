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
		// Verifica se o título da página é o esperado.
		assertThat(driver.getTitle(), containsString("Blog"));

		// Verifica se o elemento responsável por informar inexistência de resultados está oculto
		assertFalse(blogSearchPage.getNoResultsVisibility());

		// Verifica se o número de posts na página inicial é o esperado.
		assertEquals("Número errado de posts na página inicial do blog", 6, blogSearchPage.getPostBoxes().size());

		takeScreenshot("Blog início");
	}

	@Test
	public void verifyBlogSearchFunctionality() {
		for (String title : blogSearchPage.getPostTitlesAsText()) {
			blogSearchPage.searchFor(title);

			// Verifica se o título da página contem a string utilizada na pesquisa.
			assertThat(driver.getTitle(), containsString(title));
			// Verifica se o título da pesquisa conte a string utilizada na pesquisa;
			assertThat(blogSearchPage.getPageHeading().getText(), equalToIgnoringCase("Search Results for: " + title));
			// Verifica se o elemento responsável por informar inexistência de resultados está oculto
			assertFalse(blogSearchPage.getNoResultsVisibility());
			// Verifica se o título do primeiro resultado é igual a string pesquisada.
			assertEquals(title, blogSearchPage.getPostTitlesAsText().get(0));

			takeScreenshot("Search results for " + title.replaceAll("[^a-zA-Z ]", ""));

			driver.navigate().back();
		}
	}

	@Test
	public void verifyEmptyBlogSearch(){
		blogSearchPage.searchFor("");

		// Verifica se pesquisas vazias retornam página de pesquisa genérica.
		assertThat(driver.getTitle(), equalToIgnoringCase("Você pesquisou por | SOC - Software de Saúde e Segurança do Trabalho"));
		assertThat(blogSearchPage.getPageHeading().getText(), equalToIgnoringCase("Search Results for:"));

		// Verifica se o elemento responsável por informar inexistência de resultados está oculto
		assertFalse(blogSearchPage.getNoResultsVisibility());

		takeScreenshot("Pesquisa vazia");

		driver.navigate().back();
	}

	@Test
	public void verifyInvalidBlogSearch(){

		String reversed = new StringBuilder(blogSearchPage.getPostTitlesAsText().get(0)).reverse().toString();
		blogSearchPage.searchFor(reversed);

		// Verifica se o título da página é consistente com a pesquisa
		assertThat(driver.getTitle(), containsString("Você pesquisou por " + reversed));
		// Verifica se o título da pesquisa é consistente com a pesquisa
		assertThat(blogSearchPage.getPageHeading().getText(), equalToIgnoringCase("Search Results for: " + reversed));
		// Verifica se o elemento responsável por informara inexistência de resultados está visível
		assertTrue(blogSearchPage.getNoResultsVisibility());

		takeScreenshot("Pesquisa inválida");

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
