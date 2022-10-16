package fragments.inheritance

import org.intsite.khtml.loader.KHTML
import org.mockito.Mockito
import org.openqa.selenium.chrome.ChromeDriver
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class InheritFragments {
    private var driver: ChromeDriver = Mockito.mock(ChromeDriver::class.java)
    private lateinit var fragmentFirst: FragmentFirst

    init {
        KHTML.decorate(this, driver)
    }

    @Test
    fun testInheritance() {
        assertEquals(
            fragmentFirst.elem().xpath,
            ".//div[@id='FragmentSecond']//div[@id='FragmentFirst']//button[@id='btn']"
        )
    }
}