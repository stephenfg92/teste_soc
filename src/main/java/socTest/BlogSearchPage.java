package socTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BlogSearchPage {
	@SuppressWarnings("unused")
	private WebDriver driver;

	@FindBy(className = "elementor-heading-title")
	private WebElement pageHeading;

	@FindBy(className = "elementor-posts-nothing-found")
	private WebElement noResultsElement;

	@FindBy(xpath = "/html/body/div[2]/div/div/section[1]/div/div/div/div/div/section[2]/div/div/div[2]/div/div/div/div/form/div/input")
	private WebElement searchBox;

	@FindBy(xpath = "//div[contains(@class,'elementor-post__card')]")
	private List<WebElement> postBoxes;

	public BlogSearchPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


	public void searchFor(String text) {
		searchBox.sendKeys(text);
		searchBox.submit();
	}

	public WebElement getPageHeading() {
		return pageHeading;
	}

	public List<WebElement> getPostBoxes(){
		return postBoxes;
	}

	private List<WebElement> getPostTitleElements(){
		return new ArrayList<WebElement>(
			getPostBoxes()
			.stream()
			.map(element -> element.findElement(By.className("elementor-post__title")))
			.collect(Collectors.toList())
		);
	}

	public List<String> getPostTitlesAsText(){
		return new ArrayList<String>(
			getPostTitleElements()
			.stream()
			.map(element -> element.getText())
			.collect(Collectors.toList())
		);
	}

	public boolean getNoResultsVisibility(){
		try{
			boolean bool = noResultsElement.isDisplayed();
			return bool;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
