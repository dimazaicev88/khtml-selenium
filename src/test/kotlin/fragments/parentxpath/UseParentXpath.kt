package fragments.parentxpath

import fragments.inheritance.FragmentFirst
import org.intsite.khtml.loader.KHTML
import org.mockito.Mockito
import org.openqa.selenium.chrome.ChromeDriver
import org.testng.Assert
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class UseParentXpath {
    private var driver: ChromeDriver = Mockito.mock(ChromeDriver::class.java)
    private lateinit var parentFragment: ParentFragment

    init {
        KHTML.decorate(this, driver)
    }

    @Test
    fun testParentXpath() {
        assertEquals(
                parentFragment.childFragment().someButton().xpath,
                ".//div[@class='childFragment']//div[@id='btn']"
        )

        assertEquals(
                parentFragment.childFragment2().someButton2().xpath,
                "//div[@class='childFragment2']//div[@id='btn']"
        )

        assertEquals(
                parentFragment.childFragment2().someButton3().xpath,
                ".//div[@id='btn']"
        )
    }
}