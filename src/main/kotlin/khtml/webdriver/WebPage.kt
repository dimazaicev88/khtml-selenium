package khtml.webdriver

import khtml.annotations.InjectWebDriver
import org.openqa.selenium.WebDriver

interface WebPage {

    @InjectWebDriver
    fun getDriver(): WebDriver

    fun open(url: String) {
        getDriver().get(url)
    }
}