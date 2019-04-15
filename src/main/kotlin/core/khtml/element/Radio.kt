package core.khtml.element

import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement


class Radio(private val strXpath: String, driver: WebDriver) : CustomElement<Radio>(strXpath, driver) {


    //TODO добавить execWebElementAction
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

    fun selectByValue(value: String) {
        val matchingButton = buttons.first { value == it.getAttribute("value") }
        selectButton(matchingButton)
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
