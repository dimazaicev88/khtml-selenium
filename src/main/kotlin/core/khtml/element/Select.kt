package core.khtml.element

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class Select(private val strXpath: String, driver: WebDriver) : CustomElement<Select>(strXpath, driver) {

    private val select: org.openqa.selenium.support.ui.Select
        get() = org.openqa.selenium.support.ui.Select(element)

    val isMultiple: Boolean
        get() = select.isMultiple

    val options: List<WebElement>
        get() = select.options

    val allSelectedOptions: List<WebElement>
        get() = select.allSelectedOptions

    val firstSelectedOption: WebElement
        get() = select.firstSelectedOption

    val hasSelectedOption: Boolean
        get() = options.any { it.isSelected }

    fun selectByVisibleText(text: String) {
        select.selectByVisibleText(text)
    }

    fun selectByIndex(index: Int) {
        select.selectByIndex(index)
    }

    fun selectByValue(value: String) {
        select.selectByValue(value)
    }

    fun deselectAll() {
        select.deselectAll()
    }

    fun deselectByValue(value: String) {
        select.deselectByValue(value)
    }

    fun deselectByIndex(index: Int) {
        select.deselectByIndex(index)
    }

    fun deselectByVisibleText(text: String) {
        select.deselectByVisibleText(text)
    }
}

