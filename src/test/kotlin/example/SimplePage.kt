package example

import core.khtml.loader.KHTML
import core.khtml.waits.WaitCondition
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.testng.Assert.assertTrue
import org.testng.annotations.AfterClass
import org.testng.annotations.Test
import java.util.*


class SimplePage {
    private lateinit var mainPageElements: AbstractPage
    private var driver: WebDriver

    init {
        this.driver = createDriver()
        KHTML.decorate(this, driver)
        driver.get("file:///C:/Users/Dima/WebstormProjects/untitled/index.html")
    }

    @Test(invocationCount = 100)
    fun existsLogoImg() {
        println(mainPageElements.box().attr("data-test"))
    }

    @AfterClass
    fun afterClass() {
        driver.close()
    }

    fun createDriver(): WebDriver {
        val options = ChromeOptions()

        System.setProperty("webdriver.chrome.driver", "F:\\google_driver/chromedriver.exe")
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




