package org.intsite.khtml.throttle

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.openqa.selenium.WebDriver
import java.lang.RuntimeException
import java.util.concurrent.ConcurrentHashMap

class Throttle {
    private var isDisableThrottling = 0
    private val defaultTimings = ThrottleTimings()

    private val envEnableThrottle: Boolean
        get() {
            return System.getenv("KHTML_THROTTLE_ENABLE") != null && Integer.parseInt(System.getenv("KHTML_THROTTLE_ENABLE")) == 1
        }

    private val handDisableThrottling
        get() = isDisableThrottling == 1

    private val envTimings: ThrottleTimings? by lazy {
        if (System.getenv("KHTML_THROTTLE") != null) {
            Gson().fromJson(System.getenv("KHTML_THROTTLE"), ThrottleTimings::class.java)
        } else {
            null
        }
    }

    val timings: ThrottleTimings
        get() {
            return if (envEnableThrottle && !handDisableThrottling) {
                if (envTimings == null) {
                    throw RuntimeException("KHTML_THROTTLE_ENABLE is enable, but KHTML_THROTTLE is not set")
                }
                return envTimings as ThrottleTimings
            } else {
                defaultTimings
            }
        }


    companion object {
        private val mapTimings = ConcurrentHashMap<WebDriver, Throttle>()

        private fun initThrottle(driver: WebDriver) {
            synchronized(driver) {
                if (mapTimings[driver] == null) {
                    mapTimings[driver] = Throttle()
                }
            }
        }

        fun disableThrottling(driver: WebDriver) {
            initThrottle(driver)
            mapTimings[driver]?.isDisableThrottling = 1
        }

        fun enableThrottling(driver: WebDriver) {
            initThrottle(driver)
            mapTimings[driver]?.isDisableThrottling = 0
        }

        private fun getTimings(driver: WebDriver): ThrottleTimings {
            initThrottle(driver)
            return mapTimings[driver]!!.timings
        }

        fun timeBeforeFileUpload(driver: WebDriver): Long {
            return getTimings(driver).beforeFileUpload
        }

        fun timeAfterFileUpload(driver: WebDriver): Long {
            return getTimings(driver).afterFileUpload
        }

        fun timeBeforeClick(driver: WebDriver): Long {
            return getTimings(driver).beforeClick
        }

        fun timeAfterClick(driver: WebDriver): Long {
            return getTimings(driver).afterClick
        }

        fun timeAfterMove(driver: WebDriver): Long {
            return getTimings(driver).afterMove
        }

        fun timeBeforeMove(driver: WebDriver): Long {
            return getTimings(driver).beforeMove
        }

        fun timeBeforeGetText(driver: WebDriver): Long {
            return getTimings(driver).beforeGetText
        }

        fun timeAfterGetText(driver: WebDriver): Long {
            return getTimings(driver).afterGetText
        }

        fun timeBeforeIsEnabled(driver: WebDriver): Long {
            return getTimings(driver).beforeIsEnabled
        }

        fun timeAfterIsEnabled(driver: WebDriver): Long {
            return getTimings(driver).afterIsEnabled
        }

        fun timeBeforeIsSelected(driver: WebDriver): Long {
            return getTimings(driver).beforeIsSelected
        }

        fun timeAfterIsSelected(driver: WebDriver): Long {
            return getTimings(driver).afterIsSelected
        }

        fun timeBeforeIsDisplayed(driver: WebDriver): Long {
            return getTimings(driver).beforeIsDisplayed
        }

        fun timeAfterIsDisplayed(driver: WebDriver): Long {
            return getTimings(driver).afterIsDisplayed
        }

        fun timeBeforeClear(driver: WebDriver): Long {
            return getTimings(driver).beforeClear
        }

        fun timeAfterClear(driver: WebDriver): Long {
            return getTimings(driver).afterClear
        }

        fun timeBeforeShowElement(driver: WebDriver): Long {
            return getTimings(driver).beforeShowElement
        }

        fun timeAfterShowElement(driver: WebDriver): Long {
            return getTimings(driver).afterShowElement
        }

        fun timeBeforeSetValue(driver: WebDriver): Long {
            return getTimings(driver).beforeSetValue
        }

        fun timeAfterSetValue(driver: WebDriver): Long {
            return getTimings(driver).afterSetValue
        }

        fun timeBeforeHideElement(driver: WebDriver): Long {
            return getTimings(driver).beforeHideElement
        }

        fun timeAfterHideElement(driver: WebDriver): Long {
            return getTimings(driver).afterHideElement
        }

        fun timeBeforeBlur(driver: WebDriver): Long {
            return getTimings(driver).beforeBlur
        }

        fun timeAfterBlur(driver: WebDriver): Long {
            return getTimings(driver).afterBlur
        }

        fun timeBeforeFocus(driver: WebDriver): Long {
            return getTimings(driver).beforeFocus
        }

        fun timeAfterFocus(driver: WebDriver): Long {
            return getTimings(driver).afterFocus
        }

        fun timeBeforeMouseOver(driver: WebDriver): Long {
            return getTimings(driver).beforeMouseOver
        }

        fun timeAfterMouseOver(driver: WebDriver): Long {
            return getTimings(driver).afterMouseOver
        }

        fun timeBeforeChange(driver: WebDriver): Long {
            return getTimings(driver).beforeChange
        }

        fun timeAfterChange(driver: WebDriver): Long {
            return getTimings(driver).afterChange
        }

        fun timeBeforeInputValue(driver: WebDriver): Long {
            return getTimings(driver).beforeInputValue
        }

        fun timeAfterInputValue(driver: WebDriver): Long {
            return getTimings(driver).afterInputValue
        }
    }
}


class ThrottleTimings {

    @SerializedName("before_input_value")
    val beforeInputValue: Long = 0

    @SerializedName("after_input_value")
    val afterInputValue: Long = 0

    @SerializedName("after_change")
    val afterChange: Long = 0

    @SerializedName("before_change")
    val beforeChange: Long = 0

    @SerializedName("after_mouse_over")
    val afterMouseOver: Long = 0

    @SerializedName("before_mouse_over")
    val beforeMouseOver: Long = 0

    @SerializedName("after_focus")
    val afterFocus: Long = 0

    @SerializedName("before_focus")
    val beforeFocus: Long = 0

    @SerializedName("after_blur")
    val afterBlur: Long = 0

    @SerializedName("before_blur")
    val beforeBlur: Long = 0

    @SerializedName("after_hide_element")
    val afterHideElement: Long = 0

    @SerializedName("before_hide_element")
    val beforeHideElement: Long = 0

    @SerializedName("after_set_value")
    val afterSetValue: Long = 0

    @SerializedName("before_set_value")
    val beforeSetValue: Long = 0

    @SerializedName("after_show_element")
    val afterShowElement: Long = 0

    @SerializedName("before_show_element")
    val beforeShowElement: Long = 0

    @SerializedName("after_clear")
    val afterClear: Long = 0

    @SerializedName("before_clear")
    val beforeClear: Long = 0

    @SerializedName("after_is_displayed")
    val afterIsDisplayed: Long = 0

    @SerializedName("before_is_displayed")
    val beforeIsDisplayed: Long = 0

    @SerializedName("after_is_selected")
    val afterIsSelected: Long = 0

    @SerializedName("before_is_selected")
    val beforeIsSelected: Long = 0

    @SerializedName("after_is_enabled")
    val afterIsEnabled: Long = 0

    @SerializedName("before_is_enabled")
    val beforeIsEnabled: Long = 0

    @SerializedName("after_get_text")
    val afterGetText: Long = 0

    @SerializedName("before_get_text")
    val beforeGetText: Long = 0

    @SerializedName("before_move")
    val beforeMove: Long = 0

    @SerializedName("after_move")
    val afterMove: Long = 0

    @SerializedName("after_click")
    val afterClick: Long = 0

    @SerializedName("before_click")
    val beforeClick: Long = 0

    @SerializedName("before_file_upload")
    val beforeFileUpload: Long = 0

    @SerializedName("after_file_upload")
    val afterFileUpload: Long = 0
}