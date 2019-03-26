package example

import core.khtml.annotations.Page
import core.khtml.loader.KHTML
import core.khtml.webdriver.WebPage
import org.openqa.selenium.WebDriver
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

@Page
interface MainPageElements : WebPage {

    fun mainNav(): MainNav

    fun header(): Header

    fun portfolio(): Portfolio

    fun about(): About

    fun contact(): List<Contact>
}


class MainPageImpl constructor(driver: WebDriver) {
    lateinit var mainPageElements: MainPageElements

    init {
        KHTML.decorate(this, driver)
    }

    @Test
    fun existsLogoImg() {
        assertTrue(mainPageElements.header().logoImg().exists)
    }
}


