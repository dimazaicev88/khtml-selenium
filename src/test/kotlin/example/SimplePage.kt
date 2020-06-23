package example

import khtml.loader.KHTML
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.util.*


class SimplePage {
    lateinit var mainPageElements: AbstractFragment
    private var driver: WebDriver

    init {
        this.driver = createDriver()
        KHTML.decorate(this, driver)
    }

    @Test
    fun existsLogoImg() {
        driver.get("http://autotest.officemag.ru/services/sets/?ID=31850")
        assertTrue(mainPageElements.someFragment().def())
    }

    fun createDriver(): WebDriver {
        val options = ChromeOptions()
        System.setProperty("webdriver.chrome.driver", System.getenv("WEBDRIVER_CHROME"))
        val prefs = HashMap<String, Any>()
        prefs["download.prompt_for_download"] = "false"
        prefs["download.directory_upgrade"] = "true"
        prefs["safebrowsing.enabled"] = "true"
        options.addArguments(
            "--user-agent=webdriver_samson",
            "disable-infobars",
            "--updateFilesAndUpload-maximized",
            "--ignore-certificate-errors",
            "--disable-dev-shm-usage",
            "--no-sandbox",
            "--safebrowsing-disable-download-protection"
        )

        options.setExperimentalOption("prefs", prefs)
        val driver = ChromeDriver(options)
        driver.manage().deleteAllCookies()
        driver.manage().window().maximize()
        return driver
    }

}




