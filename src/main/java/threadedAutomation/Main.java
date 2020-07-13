package threadedAutomation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
	
	public static volatile AtomicInteger count = new AtomicInteger(0);
	
	/*
	 * The DRIVER_INSTANCES defines the number of chrome instances to run.
	 * Change the value as per the instances you want to run
	 */
	public static final int DRIVER_INSTANCES = 3;
	
	
	
	public static void main(String[] args) {
		DbFunctions dbFunctions = new DbFunctions();
		List<String> urls = new ArrayList<String>();
		
		//below commented code is to fetch the product_url link from the links provided in the masterurls table
		/*
		{
		urls = dbFunctions.getUrlListFromTbl("url", "masterurls");
		for(String url: urls)
		{
			GetProductUrl details = new GetProductUrl(url);
			
		}
		//System.out.println(urls);
		
		GetProductUrl.service.shutdown();
		}
		*/
		
		//below process to find the product details from the url of product_details table
		{
			urls = dbFunctions.getUrlListFromTbl("product_link", "product_details");
			PriorityBlockingQueue<String> p = new PriorityBlockingQueue<>();
			p.addAll(urls);
			
			WebDriverPool.initializeWebDriverPool();
			
			while(count.get()<DRIVER_INSTANCES)
			{
				String url = p.poll();
				GetProductDetails getProductDetails = new GetProductDetails(url);
				count.getAndIncrement();
				System.out.println(count.get());
			}
		}
		
		
		GetProductDetails.service.shutdownNow();
		WebDriverPool.quitAllDrivers();
		
		
	}
}
