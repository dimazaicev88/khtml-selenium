package example

import core.khtml.loader.KHTML
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.testng.Assert.assertEquals
import org.testng.annotations.AfterClass
import org.testng.annotations.Test
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


class SimplePage {
    private lateinit var elementInPage: ElementInPage
    private lateinit var fragmentInPage: FragmentInPage
    private lateinit var contextInFragment: ContextInFragment
    private lateinit var fragmentInFragment: FragmentInFragment
    private lateinit var inheritanceFragment: InheritanceFragment
    private lateinit var dumpFragment: DumpFragment

    private var driver: WebDriver

    init {
        this.driver = createDriver()
        KHTML.decorate(this, driver)
        val path = Paths.get(System.getProperty("user.dir"), "dump")
        System.setProperty("khtml.dump.dir", path.toString())
    }

//    @Test
//    fun elementInPage() {
//        assertEquals(elementInPage.elementInPage().strXpath, ".//div[@class='elementInPage']")
//    }
//
//    @Test
//    fun fragmentInPage() {
//        assertEquals(fragmentInPage.fragmentInPage().context().strXpath, ".//div[@class='fragmentInPage']")
//    }
//
//    @Test
//    fun contextInFragment() {
//        assertEquals(contextInFragment.context().strXpath, ".//div[@class='moving']")
//    }
//
//    @Test
//    fun fragmentInFragment() {
//        assertEquals(
//            fragmentInFragment.fragmentInFragment().context().strXpath,
//            ".//div[@class='FragmentInFragment']//div[@class='fragmentInFragment']"
//        )
//    }
//
//    @Test
//    fun elementInFragment() {
//        assertEquals(elementInPage.elementInPage().strXpath, ".//div[@class='moving']")
//    }
//
//    @Test
//    fun inheritanceFragment() {
//        assertEquals(inheritanceFragment.context().strXpath, ".//div[@class='moving']")
//    }

    @Test
    fun dumpFragment() {
        dumpFragment.listFragments()
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




