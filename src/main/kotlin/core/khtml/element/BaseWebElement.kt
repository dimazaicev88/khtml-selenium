package core.khtml.element

import org.openqa.selenium.Dimension
import org.openqa.selenium.Point
import org.openqa.selenium.Rectangle
import org.openqa.selenium.WebElement

interface BaseWebElement<T> {

    val element: WebElement
    val location: Point
    val text: String
    val tagName: String
    val isSelected: Boolean
    val isEnabled: Boolean
    val size: Dimension
    val rect: Rectangle
    val isDisplayed: Boolean
    val source: String
    val exists: Boolean
    val isDisplayedOnJs: Boolean

    fun addClass(className: String): T

    fun removeClass(className: String): T

    fun click(): T

    fun move(): T

    fun jsChange(): T

    fun jsBlur(): T

    fun jsClick(): T

    fun jsFocus(): T

    fun submit(): T

    fun show(): T

    fun hide(): T

    fun sendKeys(vararg charSequences: CharSequence): T

    fun setValue(value: String): T

    fun clear(): T

    fun attr(name: String): String

    fun findElements(xpath: String): List<WebElement>

    fun findElement(xpath: String): WebElement

    fun cssValue(name: String): String
}