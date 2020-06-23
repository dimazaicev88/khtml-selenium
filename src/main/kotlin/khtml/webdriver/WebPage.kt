package khtml.webdriver

import org.openqa.selenium.WebDriver

interface WebPage {

    @khtml.annotations.InjectWebDriver
    fun getDriver(): WebDriver

    fun open(url: String) {
        getDriver().get(url)
    }
}