package throttle

import com.google.gson.Gson
import org.intsite.khtml.throttle.Throttle
import org.intsite.khtml.throttle.ThrottleTimings
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.testng.Assert.assertEquals
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

//TODO переделать
class NotEmptyThrottle {
    private var driver: ChromeDriver = Mockito.mock(ChromeDriver::class.java)
    private var driver2: ChromeDriver = Mockito.mock(ChromeDriver::class.java)
    private val timings: ThrottleTimings = Gson().fromJson(throttleTimings, ThrottleTimings::class.java)

    init {
        Mockito.`when`(driver.findElement(By.xpath("")))
            .thenReturn(Mockito.mock(WebElement::class.java))

        Mockito.`when`(driver.findElements(By.xpath("")))
            .thenReturn(listOf(Mockito.mock(WebElement::class.java)))

        Mockito.`when`(driver2.findElement(By.xpath("")))
            .thenReturn(Mockito.mock(WebElement::class.java))

        Mockito.`when`(driver2.findElements(By.xpath("")))
            .thenReturn(listOf(Mockito.mock(WebElement::class.java)))
    }

    @DataProvider(parallel = true)
    fun dpTimings(): Array<Array<Any>> {
        return arrayOf(
            arrayOf(timings.beforeChange, Throttle.timeBeforeChange(driver), "beforeChange"),
            arrayOf(timings.afterChange, Throttle.timeAfterChange(driver), "afterChange"),
            arrayOf(timings.beforeMouseOver, Throttle.timeBeforeMouseOver(driver), "beforeMouseOver"),
            arrayOf(timings.afterMouseOver, Throttle.timeAfterMouseOver(driver), "afterMouseOver"),
            arrayOf(timings.beforeFocus, Throttle.timeBeforeFocus(driver), "beforeFocus"),
            arrayOf(timings.afterFocus, Throttle.timeAfterFocus(driver), "afterFocus"),
            arrayOf(timings.beforeBlur, Throttle.timeBeforeBlur(driver), "beforeBlur"),
            arrayOf(timings.afterBlur, Throttle.timeAfterBlur(driver), "afterBlur"),
            arrayOf(timings.beforeHideElement, Throttle.timeBeforeHideElement(driver), "beforeHideElement"),
            arrayOf(timings.afterHideElement, Throttle.timeAfterHideElement(driver), "afterHideElement"),
            arrayOf(timings.beforeSetValue, Throttle.timeBeforeSetValue(driver), "beforeSetValue"),
            arrayOf(timings.afterSetValue, Throttle.timeAfterSetValue(driver), "afterSetValue"),
            arrayOf(timings.beforeShowElement, Throttle.timeBeforeShowElement(driver), "beforeShowElement"),
            arrayOf(timings.afterShowElement, Throttle.timeAfterShowElement(driver), "afterShowElement"),
            arrayOf(timings.beforeClear, Throttle.timeBeforeClear(driver), "beforeClear"),
            arrayOf(timings.afterClear, Throttle.timeAfterClear(driver), "afterClear"),
            arrayOf(timings.beforeIsSelected, Throttle.timeBeforeIsSelected(driver), "beforeIsSelected"),
            arrayOf(timings.afterIsSelected, Throttle.timeAfterIsSelected(driver), "afterIsSelected"),
            arrayOf(timings.beforeIsEnabled, Throttle.timeBeforeIsEnabled(driver), "beforeIsEnabled"),
            arrayOf(timings.afterIsEnabled, Throttle.timeAfterIsEnabled(driver), "afterIsEnabled"),
            arrayOf(timings.beforeGetText, Throttle.timeBeforeGetText(driver), "beforeGetText"),
            arrayOf(timings.afterGetText, Throttle.timeAfterGetText(driver), "afterGetText"),
            arrayOf(timings.beforeMove, Throttle.timeBeforeMove(driver), "beforeMove"),
            arrayOf(timings.afterMove, Throttle.timeAfterMove(driver), "afterMove"),
            arrayOf(timings.beforeClick, Throttle.timeBeforeClick(driver), "beforeClick"),
            arrayOf(timings.afterClick, Throttle.timeAfterClick(driver), "afterClick"),
            arrayOf(timings.beforeFileUpload, Throttle.timeBeforeFileUpload(driver), "beforeFileUpload"),
            arrayOf(timings.afterFileUpload, Throttle.timeAfterFileUpload(driver), "afterFileUpload"),
            arrayOf(timings.beforeInputValue, Throttle.timeBeforeInputValue(driver), "timeBeforeInputValue"),
            arrayOf(timings.afterInputValue, Throttle.timeAfterInputValue(driver), "timeAfterInputValue")
        )
    }

    @Test(dataProvider = "dpTimings", priority = 1, enabled = false)
    fun throttleTimings(expected: Long, actual: Long, msg: String) {
        assertEquals(actual, expected, msg)
    }

    @Test(priority = 2, enabled = false)
    fun disableAndEnableThrottling() {
        Throttle.disableThrottling(driver)
        assertEquals(Throttle.timeBeforeClick(driver), 0)
        assertEquals(Throttle.timeBeforeClick(driver2), 360)
        Throttle.enableThrottling(driver)
        assertEquals(Throttle.timeBeforeClick(driver), 360)
    }

    companion object {
        const val throttleTimings = """
            {
               "before_change":100,
               "after_change":110,
               "before_mouse_over":120,
               "after_mouse_over":130,
               "before_focus":140,
               "after_focus":150,
               "before_blur":160,
               "after_blur":170,
               "before_hide_element":180,
               "after_hide_element":190,
               "before_set_value":200,
               "after_set_value":210,
               "before_show_element":220,
               "after_show_element":230,
               "before_clear":240,
               "after_clear":250,
               "before_is_selected":280,
               "after_is_selected":290,
               "before_is_enabled":300,
               "after_is_enabled":310,
               "before_get_text":320,
               "after_get_text":330,
               "before_move":340,
               "after_move":350,
               "before_click":360,
               "after_click":370,
               "before_file_upload":380,
               "after_file_upload":390,
               "before_input_value":400,
               "after_input_value":410
            }
        """
    }
}