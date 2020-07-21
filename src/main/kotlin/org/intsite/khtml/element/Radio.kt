package org.intsite.khtml.element

import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement


class Radio(_xpath: String, driver: WebDriver, testName: String? = null) : CustomElement<Radio>(_xpath, driver, testName) {

    private val buttons: List<WebElement>
        get() {
            val radioName = element.getAttribute("name")

            val xpath: String
            xpath = if (radioName.isEmpty()) {
                "self::* | following::input[@type = 'radio'] | preceding::input[@type = 'radio']"
            } else {
                "self::* | following::input[@type = 'radio' and @name = '$radioName'] | preceding::input[@type = 'radio' and @name = '$radioName']"
            }
            return element.findElements(By.xpath(xpath))
        }

    val selectedButton: WebElement
        get() = buttons.first { it.isSelected }

    fun hasSelectedButton(): Boolean {
        return buttons.any { it.isSelected }
    }

    fun selectByValue(value: String): Radio {
        val matchingButton = buttons.first { value == it.getAttribute("value") }
        selectButton(matchingButton)
        return this
    }

    fun isSelected(value: String): Boolean {
        return buttons.first { value == it.getAttribute("value") }.isSelected
    }

    fun selectByIndex(index: Int) {
        val buttons = buttons
        if (index < 0 || index >= buttons.size) {
            throw NoSuchElementException("Cannot locate radio button with index: $index")
        }

        selectButton(buttons[index])
    }

    private fun selectButton(button: WebElement) {
        if (!button.isSelected) {
            button.click()
        }
    }
}
