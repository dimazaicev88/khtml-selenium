package element

import org.intsite.khtml.element.HtmlElement
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class CustomElementTest {
    private var driver: ChromeDriver = Mockito.mock(ChromeDriver::class.java)

    @BeforeClass
    fun init() {
        Mockito.`when`(driver.findElements(By.xpath("")))
                .thenReturn(arrayListOf(Mockito.mock(WebElement::class.java), Mockito.mock(WebElement::class.java)))

        Mockito.`when`(driver.findElement(By.xpath("")))
                .thenReturn(Mockito.mock(WebElement::class.java))
    }

    @Test
    fun testParentElement() {
      assertEquals(HtmlElement("", driver).parent.xpath,"//parent::*")
    }

    @Test
    fun testGetElement() {
    }

    @Test
    fun testGetExists() {

    }

    @Test
    fun testGetNotExists() {

    }

    @Test
    fun testGetSource() {

    }

    @Test
    fun testGetLocation() {

    }

    @Test
    fun testGetText() {

    }

    @Test
    fun testGetTagName() {

    }

    @Test
    fun testIsSelected() {

    }

    @Test
    fun testIsEnabled() {

    }

    @Test
    fun testGetSize() {

    }

    @Test
    fun testGetRect() {

    }

    @Test
    fun testIsDisplayed() {

    }

    @Test
    fun testIsDisplayedOnJs() {

    }

    @Test
    fun testAddClass() {

    }

    @Test
    fun testRemoveClass() {

    }

    @Test
    fun testGetTextOnJs() {

    }

    @Test
    fun testShow() {

    }

    @Test
    fun testSetValueOnJs() {

    }

    @Test
    fun testHide() {

    }

    @Test
    fun testMove() {

    }

    @Test
    fun testTestWait() {

    }

    @Test
    fun testSetValue() {

    }

    @Test
    fun testJsChange() {

    }

    @Test
    fun testJsBlur() {

    }

    @Test
    fun testJsClick() {

    }

    @Test
    fun testJsFocus() {

    }

    @Test
    fun testScrollTop() {

    }

    @Test
    fun testScroll() {

    }

    @Test
    fun testClick() {

    }

    @Test
    fun testSubmit() {

    }

    @Test
    fun testSendKeys() {

    }

    @Test
    fun testClear() {

    }

    @Test
    fun testWaitCustomCondition() {

    }

    @Test
    fun testAttr() {

    }

    @Test
    fun testFindElements() {

    }

    @Test
    fun testFindElement() {

    }

    @Test
    fun testCssValue() {

    }

    @Test
    fun testRepeatClick() {

    }

    @Test
    fun testGetDriver() {

    }
}