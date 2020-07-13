package threadedAutomation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.mysql.fabric.xmlrpc.base.Array;

public class GetProductUrl {

	static ExecutorService service = Executors.newFixedThreadPool(10/* Runtime.getRuntime().availableProcessors() */);
	//static String url;

	public GetProductUrl(String url) {
		super();
		//this.url = url;
		@SuppressWarnings("unchecked")
		Future<Boolean> future = (Future<Boolean>) service.submit(() -> {
			WebDriver driver = new ChromeDriver();
			driver.navigate().to(url);
			try {
				Utils.scrollSmoothly(driver);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<WebElement> elements = driver.findElements(By.xpath("//div[@class='_3liAhj _2Vsm67 _1xr4tv']/a"));
			List<Object> urls = new ArrayList<>();
			for(WebElement element:elements) {
				String productUrl = element.getAttribute("href");
				urls.add(productUrl);
			}
			
			String sql = "INSERT INTO product_details(product_link, master_url) values(?, ?)";
			DbFunctions dbFunctions = new DbFunctions();
			dbFunctions.insertListInDbInRows(sql, urls, url);
			driver.close();
		});
	}
	/*
	public boolean fetch(String url) {
		return true;
	}
	*/
}
