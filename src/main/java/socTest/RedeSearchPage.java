package socTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RedeSearchPage {
    private WebDriver driver;

    @FindBy(className = "titulo-rede-credenciada")
    @CacheLookup
    WebElement titleClass;

    @FindBy(xpath = "//*[@id='ipt-busca-credenciado-2']")
    @CacheLookup
    WebElement searchBox;

    @FindBys( { @FindBy(className = "pg-busca-socnet"), @FindBy(id = "div-carregando-operacao"), @FindBy(id = "div-infos-carregando-operacao") } )
    WebElement loadingResults;

    @FindBys( { @FindBy(className = "pg-busca-socnet"), @FindBy(className = "section-content"), @FindBy(id = "conteudo-resultados"), @FindBy(id = "msg-sem-resultados") } )
    WebElement noResultsMessage;

    @FindBy(xpath = "//*[@id='conteudo-resultados']/div[1]/div//*")
    //List<WebElement> searchResults;
    WebElement searchResults;

    @FindBys( {@FindBy(className = "section-sidenav"), @FindBy(id = "div-filtro-conveniencias"), @FindBy(className = "expand-filtros")} )
    @CacheLookup
    WebElement expandFiltrosConvenienciaBtn;

    @FindBys( {@FindBy(className = "section-sidenav"), @FindBy(id = "div-filtro-servicos"), @FindBy(className = "expand-filtros")} )
    @CacheLookup
    WebElement expandFiltrosServicosBtn;

    @FindAll( { @FindBy(className = "elemento-filtro")})
    List<WebElement> filtrosConveniencia;
    boolean firstIterCall = true;
    Iterator<WebElement> filtrosIter;
    WebElement currentIterElement;

    public RedeSearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void searchFor(String text) throws InterruptedException {
        searchBox.sendKeys(text);
        searchBox.sendKeys(Keys.ENTER);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(3000));
        wait.until(ExpectedConditions.invisibilityOf(loadingResults));
        wait.until(ExpectedConditions.visibilityOf(searchResults));
        Thread.sleep(5000);
    }

    public void expandFiltrosConveniencia(){
        expandFiltrosConvenienciaBtn.click();
        //expandFiltrosServicosBtn.click();
    }


    private List<WebElement> getFiltrosConveniencia(){
        return filtrosConveniencia;
    }

    public void selectFiltro() throws InterruptedException{
        if (this.firstIterCall){
            this.filtrosIter = getFiltrosConveniencia().iterator();
            this.firstIterCall = false;
        }
        if (this.filtrosIter.hasNext()){
            WebElement e = filtrosIter.next();
            this.currentIterElement = e;

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500));
            wait.until(ExpectedConditions.elementToBeClickable(e));

            Actions actions = new Actions(driver);
            actions.moveToElement(e);
            actions.click(e);

            Action action = actions.build();
            action.perform();

        }
    }

    public void deselectFiltro() throws InterruptedException{

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500));
        wait.until(ExpectedConditions.elementToBeClickable(this.currentIterElement));

        Actions actions = new Actions(driver);
        actions.moveToElement(this.currentIterElement);
        actions.click(this.currentIterElement);

        Action action = actions.build();
        action.perform();

    }

    public void resetFiltroIter(){
    	this.firstIterCall = true;
    }

    public boolean isFilterSelected(){
        WebElement e = this.currentIterElement.findElement(By.tagName("input"));

        return e.isSelected();
    }

    public void selectResult() throws InterruptedException{
    	JavascriptExecutor js = (JavascriptExecutor) driver;
    	js.executeScript("var clickEvent = document.createEvent('MouseEvents');clickEvent.initEvent('mouseover', true, true); arguments[0].dispatchEvent(clickEvent);", searchResults);

    	WebElement btn = searchResults.findElement(By.tagName("a"));

    	//js.executeScript("var clickEvent = document.createEvent('MouseEvents');clickEvent.initEvent('mouseover', true, true); arguments[0].dispatchEvent(clickEvent);", btn);

    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(1500));
        //wait.until(ExpectedConditions.visibilityOf(btn));
        wait.until(ExpectedConditions.elementToBeClickable(btn));

        js.executeScript("arguments[0].scrollIntoView(true);", btn);

    	//Actions _actions = new Actions(driver);
    	//_actions.moveToElement(btn);
    	//_actions.click(btn);
    	//Action _action = _actions.build();
    	//_action.perform();
    	js.executeScript("arguments[0].click();", btn);
        //btn.click();

    	System.out.println("Action performed");
    }
}
