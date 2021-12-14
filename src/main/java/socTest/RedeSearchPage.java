package socTest;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RedeSearchPage {
    private WebDriver driver;

    @FindBy(className = "titulo-rede-credenciada")
    @CacheLookup
    WebElement titleClass;

    @FindBy(xpath = "//*[@id='ipt-busca-credenciado-2']")
    @CacheLookup
    WebElement searchBox;

    @FindBys( { @FindBy(className = "pg-busca-socnet"),
    			@FindBy(id = "div-carregando-operacao"),
    			@FindBy(id = "div-infos-carregando-operacao") } )
    WebElement loadingResults;

    @FindBys( { @FindBy(className = "pg-busca-socnet"),
    			@FindBy(className = "section-content"),
    			@FindBy(id = "conteudo-resultados"),
    			@FindBy(id = "msg-sem-resultados") } )
    WebElement noResultsMessage;

    @FindBy(xpath = "//*[@id='conteudo-resultados']/div[1]/div//*")
    //List<WebElement> searchResults;
    WebElement searchResults;

    @FindBys( {@FindBy(className = "section-sidenav"),
    		   @FindBy(id = "div-filtro-conveniencias"),
    		   @FindBy(className = "expand-filtros")} )
    @CacheLookup
    WebElement expandFiltrosConvenienciaBtn;

    @FindBys( {@FindBy(className = "section-sidenav"),
    	       @FindBy(id = "div-filtro-servicos"),
    	       @FindBy(className = "expand-filtros")} )
    @CacheLookup
    WebElement expandFiltrosServicosBtn;

    @FindAll( { @FindBy(className = "elemento-filtro")})
    List<WebElement> filtrosConveniencia;

    // Controle da seleção dos filtros
    private int m_currentFiltro;


    public RedeSearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public WebElement getTitleClass(){
    	return titleClass;
    }

    public boolean getNoResultsVisibility(){
    	return noResultsMessage.isDisplayed();
    }

    public void searchFor(String text) throws InterruptedException {
        searchBox.sendKeys(text);
        searchBox.sendKeys(Keys.ENTER);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(3000));
        wait.until(ExpectedConditions.invisibilityOf(loadingResults));
        wait.until(ExpectedConditions.visibilityOf(searchResults));
        Thread.sleep(500);
    }

    public void expandFiltrosConveniencia(){
        expandFiltrosConvenienciaBtn.click();
    }

    public void expandFiltrosServicos(){
    	expandFiltrosServicosBtn.click();
    }

    private List<WebElement> getFiltrosConveniencia(){
        return filtrosConveniencia;
    }

    public void selectFiltroByIndex(int index) throws InterruptedException {
    	WebElement e = getFiltrosConveniencia().get(index);
    	this.m_currentFiltro = index;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500));
        wait.until(ExpectedConditions.elementToBeClickable(e));

        Actions actions = new Actions(driver);
        actions.moveToElement(e);
        actions.click(e);

        Action action = actions.build();
        action.perform();
    }

    public void deselectCurrentFiltro(){
    	if (m_currentFiltro < 0){
    		return;
    	}

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500));
        wait.until(ExpectedConditions.elementToBeClickable(getFiltrosConveniencia().get(m_currentFiltro)));

        Actions actions = new Actions(driver);
        actions.moveToElement(getFiltrosConveniencia().get(m_currentFiltro));
        actions.click(getFiltrosConveniencia().get(m_currentFiltro));

        Action action = actions.build();
        action.perform();

        m_currentFiltro = -1;
    }

    public boolean isFilterSelected(){
        WebElement e = this.getFiltrosConveniencia().get(m_currentFiltro).findElement(By.tagName("input"));
        return e.isSelected();
    }

    public void selectResult() throws InterruptedException{
    	JavascriptExecutor js = (JavascriptExecutor) driver;
    	js.executeScript("var clickEvent = document.createEvent('MouseEvents');clickEvent.initEvent('mouseover', true, true); arguments[0].dispatchEvent(clickEvent);", searchResults);

    	WebElement btn = searchResults.findElement(By.tagName("a"));

    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(1500));
        wait.until(ExpectedConditions.elementToBeClickable(btn));

        js.executeScript("arguments[0].scrollIntoView(true);", btn);

        WebElement btnt = searchResults.findElement(By.tagName("button"));
    	js.executeScript("arguments[0].click();", btnt);
    }
}
