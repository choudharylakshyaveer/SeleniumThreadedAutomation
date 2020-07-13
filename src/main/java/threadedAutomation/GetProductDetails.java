package threadedAutomation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class GetProductDetails {
	int i=0;
	static ExecutorService service = Executors.newFixedThreadPool(10/* Runtime.getRuntime().availableProcessors() */);
	
	//String url;

	public GetProductDetails(String url) {
		super();
		
		@SuppressWarnings("unchecked")
		Future<Boolean> future = (Future<Boolean>) service.submit(
				()->{
					WebDriver driver = WebDriverPool.getAndRemove();
					try {
						driver.navigate().to(url);
						Utils.scrollSmoothly(driver);
						Utils.sleep(5);
						System.out.println(i++);
						
						String productNameXpath="//span[@class='_35KyD6']";
						String ratingXpath="//div[1]/span/div[@class='hGSR34']";
						String priceXpath="//div[@class='_1vC4OE _3qQ9m1']";
						
						String productName = driver.findElement(By.xpath(productNameXpath)).getText().trim();
						String rating = driver.findElement(By.xpath(ratingXpath)).getText().trim();
						String price = driver.findElement(By.xpath(priceXpath)).getText().trim().substring(1);
						
						String sql = "UPDATE product_details SET product_name=?, rating=?, price=?, scanned=1 WHERE product_link='"+url+"'";
						List<Object> elements = new ArrayList<>();
						elements.add(productName);
						elements.add(rating);
						elements.add(price);
						DbFunctions db = new DbFunctions();
						db.insertListInDb(sql, elements);
						Main.count.decrementAndGet();
						Thread.sleep(2000);
						System.out.println(WebDriverPool.driverPools.size());
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					WebDriverPool.driverPools.add(driver);
				}
				);
	}
	
	
}
