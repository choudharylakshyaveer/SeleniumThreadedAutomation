package threadedAutomation;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/*
 * This class creates the pool of WebDriver instances as defined in the Main.java class by final var "DRIVER_INSTANCES"
 */
public class WebDriverPool {

	
	public static List<WebDriver> driverPools = new ArrayList<WebDriver>();
	
	public static void initializeWebDriverPool() {
		for(int i=0; i<Main.DRIVER_INSTANCES; i++) {
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

	        // Add options to Google Chrome. The window-size is important for responsive sites
	        ChromeOptions options = new ChromeOptions();
	        //options.addArguments("headless");
	        options.addArguments("window-size=1200x600");
	        WebDriver driver = new ChromeDriver(options);
	        driverPools.add(driver);
		}
		System.out.println("Driver pool initialized");
	}
	
	public static WebDriver getAndRemove() {
		WebDriver driver = driverPools.get(0);
		driverPools.remove(0);
		return driver;
	}
	
	/*
	 * When our all the task are finished then this method is called from the Main.java class to close the running chrome instances
	 */
	public static void quitAllDrivers() {
		for(WebDriver driver: driverPools) {
			driver.quit();
		}
	}
}
