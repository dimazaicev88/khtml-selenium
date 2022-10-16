package fragments.context


import org.intsite.khtml.loader.KHTML
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class ContextFragmentsTest {
    private var driver: ChromeDriver = Mockito.mock(ChromeDriver::class.java)
    private lateinit var pageWithContext: PageWithContext
    private lateinit var emptyFragmentWithContext: EmptyFragmentWithContext
    private lateinit var fragmentWithContext: FragmentWithContext

    init {
        Mockito.`when`(driver.findElements(By.xpath("")))
            .thenReturn(
                listOf(
                    Mockito.mock(WebElement::class.java),
                    Mockito.mock(WebElement::class.java),
                )
            )
        KHTML.decorate(this, driver)
    }

    @Test
    fun testContextPage() {
        assertEquals(pageWithContext.context().xpath, "")
    }

    @Test(enabled = false)
    fun testContextEmptyFragment() {
        assertEquals(emptyFragmentWithContext.context().xpath, "")
    }

    @Test(enabled = false)
    fun testContextFragment() {
        assertEquals(fragmentWithContext.context().xpath, ".//div[@id='TestFragment']")
    }

    @Test
    fun testContextReturnedFragment() {
        assertEquals(
            fragmentWithContext.fragmentTwo().context().xpath,
            "//div[@id='TestFragment']//div[@id='TestFragment2']"
        )
    }

    @Test
    fun testContextListFragment() {
        Mockito.`when`(driver.findElements(By.xpath("//div[@id='TestFragment']//span[@class='FragmentItem']")))
            .thenReturn(
                listOf(
                    Mockito.mock(WebElement::class.java),
                    Mockito.mock(WebElement::class.java),
                    Mockito.mock(WebElement::class.java),
                )
            )
        assertEquals(
            fragmentWithContext.listFragment()[1].context().xpath,
            "(//div[@id='TestFragment']//span[@class='FragmentItem'])[2]"
        )
    }
}