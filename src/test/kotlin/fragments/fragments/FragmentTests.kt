package fragments.fragments

import org.intsite.khtml.loader.KHTML
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class FragmentTests {
    private var driver: ChromeDriver = Mockito.mock(ChromeDriver::class.java)
    private lateinit var basePage: BasePage

    init {
        KHTML.decorate(this, driver)
    }

    @Test
    fun testFragment() {
        Mockito.`when`(driver.findElements(By.xpath(".//div[@class='first']//div[@class='elements']")))
            .thenReturn(
                listOf(
                    Mockito.mock(WebElement::class.java),
                    Mockito.mock(WebElement::class.java),
                    Mockito.mock(WebElement::class.java),
                )
            )
        Mockito.`when`(driver.findElements(By.xpath(".//div[@class='first']//div[@class='second']//div[@class='elements2']")))
            .thenReturn(
                listOf(
                    Mockito.mock(WebElement::class.java),
                    Mockito.mock(WebElement::class.java),
                    Mockito.mock(WebElement::class.java),
                )
            )
//        val firstFragment = basePage.firstFragment()
//        basePage.firstFragment().secondFragment().findAllElements()[0].context()
        assertEquals(
            basePage.firstFragment().secondFragment().findAllElements()[0].context().xpath,
            ".//div[@class='first']//div[@class='second']//div[@class='elements2']"
        )
//        firstFragment.secondFragment().context()
        assertEquals(basePage.firstFragment().secondFragment().context().xpath, ".//div[@class='first']//div[@class='second']")
    }
}