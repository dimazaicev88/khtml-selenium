package org.intsite.khtml.element

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class Select(_xpath: String, driver: WebDriver, testName: String? = null) : CustomElement<Select>(_xpath, driver, testName) {

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

    fun selectByVisibleText(text: String): Select {
        select.selectByVisibleText(text)
        return this
    }

    fun selectByIndex(index: Int): Select {
        select.selectByIndex(index)
        return this
    }

    fun selectByValue(value: String): Select {
        select.selectByValue(value)
        return this
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

