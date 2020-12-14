package khtml.ext


import khtml.fragments.context.PageWithContext
import org.intsite.khtml.loader.KHTML
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

class CustomElementExtensionsKtTest {
    private var driver: ChromeDriver = Mockito.mock(ChromeDriver::class.java)
    private lateinit var pageWithContext: PageWithContext

    init {
        Mockito.`when`(driver.findElements(By.xpath("")))
            .thenReturn(arrayListOf(Mockito.mock(WebElement::class.java), Mockito.mock(WebElement::class.java)))

        Mockito.`when`(driver.findElement(By.xpath("//div[@id='button']")))
            .thenReturn(Mockito.mock(WebElement::class.java))
        KHTML.decorate(this, driver)
    }

//    @Test(enabled = false)
//    fun waitAjax() {
//        var seconds = 0
//        Mockito.`when`(
//            (driver.js(
//                "if(window.jQuery==null){\n" +
//                        "  return true\n" +
//                        "}else{\n" +
//                        " return window.jQuery.active == 0\n" +
//                        "}"
//            ))
//        ).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(true)
//        pageWithContext.someButton().click().waitAjax(polling = 1000, timeOut = 4)
//    }
//
//    @Test
//    fun waitTextPresent() {
//        Mockito.`when`(driver.findElement(By.xpath("//div[@id='button']")).text)
//            .thenReturn("").thenReturn("").thenReturn("").thenReturn("").thenReturn("text")
//        pageWithContext.someButton().waitTextPresent(polling = 1000, timeOut = 4)
//    }
//
//    @Test(enabled = false)
//    fun waitDisplay() {
//        Mockito.`when`(driver.findElement(By.xpath("//div[@id='button']")).isDisplayed)
//            .thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(true)
//        pageWithContext.someButton().waitDisplay(polling = 1000, timeOut = 4)
//    }
//
//    @Test(enabled = false)
//    fun waitClickable() {
//        Mockito.`when`(driver.findElement(By.xpath("//div[@id='button']")).isDisplayed)
//            .thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(true)
//        pageWithContext.someButton().waitClickable(polling = 1000, timeOut = 4)
//    }
//
//    @Test(enabled = false)
//    fun waitInvisible() {
//        Mockito.`when`(driver.findElement(By.xpath("//div[@id='button']")).isDisplayed)
//            .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false)
//        pageWithContext.someButton().waitInvisible(polling = 1000, timeOut = 4)
//    }
//
//    @Test(enabled = false)
//    fun waitNotExists() {
//        Mockito.`when`(driver.findElement(By.xpath("//div[@id='button']")).isDisplayed)
//            .thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(true)
//        pageWithContext.someButton().waitNotExists(polling = 1000, timeOut = 4)
//    }
//
//    @Test(enabled = false)
//    fun waitEnabled() {
//        Mockito.`when`(driver.findElement(By.xpath("//div[@id='button']")).isDisplayed)
//            .thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(true)
//        pageWithContext.someButton().waitEnabled(polling = 1000, timeOut = 4)
//    }
//
//    @Test
//    fun waitDisabled() {
//        Mockito.`when`(driver.findElement(By.xpath("//div[@id='button']")).isDisplayed)
//            .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false)
//        pageWithContext.someButton().waitDisabled(polling = 1000, timeOut = 4)
//    }
//
//    @Test(enabled = false)
//    fun waitCustomCondition() {
//        Mockito.`when`(driver.findElement(By.xpath("//div[@id='button']")).isDisplayed)
//            .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false)
//        pageWithContext.someButton().waitCustomCondition(polling = 1000, timeOut = 4) {
//            driver.findElement(By.xpath("//div[@id='button']")).isDisplayed
//        }
//    }
}