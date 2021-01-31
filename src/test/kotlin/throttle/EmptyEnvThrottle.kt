package throttle

import org.intsite.khtml.throttle.Throttle
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

//TODO переделать
class EmptyEnvThrottle {
    private var driver: ChromeDriver = Mockito.mock(ChromeDriver::class.java)

    init {
        Mockito.`when`(driver.findElements(By.xpath("")))
            .thenReturn(listOf(Mockito.mock(WebElement::class.java)))
        Mockito.`when`(driver.findElement(By.xpath("")))
            .thenReturn(Mockito.mock(WebElement::class.java))
    }

    @Test(expectedExceptions = [RuntimeException::class], enabled = false)
    fun beforeClick() {
        assertEquals(Throttle.timeBeforeClick(driver), 0)
    }
}