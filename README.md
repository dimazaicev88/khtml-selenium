# Аннотации

**@Page** 
- Аннотаций помечается абстрактный класс содержащий в себе фрагменты и элементы, аннотация указывает на то что данный абстрактный класс является итоговой страницей, пример:
```kotlin
@Page
interface AbstractPage {

} 
```
аннотация Page может быть наследуемой, пример:
```kotlin
interface AbstractPage : BasePage

@Page
interface BasePage
```

**@Fragment(xpath="")**
- Аннотаций помечается помечается метод или класс. Если аннотация указана над методом то данный метод должен вернуть абстрактный класс фрагмента или коллекцию фрагментов.Данная аннотация принимает один обязательный параметр селектор xpath, по которому будет искаться данный фрагмент, пример:
```kotlin
@Fragment(".//body")
interface AbstractFragment {

    @Fragment(".//div")
    fun someFragment(): SomeFragment
}

interface SomeFragment {

    @Element("button")
    fun someButton(): Button
}
```

 
**@Element(xpath="")**
- Аннотаций помечается метод абстрактного класса возвращающий элемент(Button,CheckBox...) или коллекцию элементов, Методы с данной аннотацией могут возвращаться только объекты реализующие интерфейс BaseWebElement,пример:
```kotlin
@Fragment(".//body")
interface SomeFragment {

    @Element(".//button")
    fun someButton(): Button

    @Element(".//checkbox")
    fun someCheckBox(): CheckBox

    @Element(".//div")
    fun someCheckHtmlElement(): HtmlElement
}
```


**@Param(value="")**
- Аннотация указывается перед параметрами методов, аннотация служит для замены шаблонов в xpath указанном в аннотации @Fragment или @Element, пример:
```kotlin
@Fragment(".//body")
interface AbstractFragment {

    @Fragment(".//div[text()='#{blockName}']")
    fun someFragment(@Param("blockName") blockName: String): SomeFragment

    @Element(".//a[text()='#{linkName}']")
    fun someLink(@Param("linkName") linkName: String): Link
}
```  

**@InjectWebDriver**
- Аннотация служит для внедрения webdiver в методы абстрактного класса, пример:
```kotlin
@Fragment(".//body")
interface SomeFragment {

    @InjectWebDriver
    fun driverProvider(): WebDriver

    fun defMethod(xpath: String): List<WebElement> {
        return driverProvider().findElements(By.xpath(xpath))
    }
} 
```  

**@InjectContext**
- Аннотация служит для внедрения контекста в методы абстрактного класса,внедрения контекста необходимо если требуется обратиться к текущему контексту.Метод помеченный данной должен возвращать только HtmlElement, пример:
```kotlin
@Fragment(".//body")
interface SomeFragment {

    @InjectContext
    fun context(): HtmlElement

    fun printSource(xpath: String) {
        println(context().source)
    }
}
```

**@Wait(time: Long = 30,condition: WaitCondition,polling: Long = 500)**
- Аннотация служит для ожидания элементов и фрагментов в абстрактном классе.Аннотация принимает 3 параметра, два из которых имеют значения по умолчанию. Аннотация указывается только над методами.
Параметры аннотации:
1. time - Время ожидания
2. condition - Условие ожидания
3. polling - Время между проверками выполнения условия

Пример:
```kotlin
@Fragment(".//body")
interface AbstractFragment {

    @Wait(condition = WaitCondition.ENABLED)
    @Fragment(".//div[text()='#{blockName}']")
    fun someFragment(@Param("blockName") blockName: String): SomeFragment

    @Wait(condition = WaitCondition.TO_CLICKABLE)
    @Element(".//a[text()='#{linkName}']")
    fun someLink(@Param("linkName") linkName: String): Link
}

@Fragment(".//body")
interface SomeFragment {

    @InjectContext
    fun context(): HtmlElement

    fun printSource(xpath: String) {
        println(context().source)
    }
}
```

# Классы врапперы для webelement

В фреймворк встроены следующие классы врапперы для webelement:
- Button
- CheckBox
- FileInput
- HtmlElement
- Image
- Link
- Select

# Инициализация полей класса. 
Для инициализации полей класса имеющих аннотации **@Page** или **@Fragment** используется статический метод **decorate** класса **KHTML**.Данный метод принимает 2 параметра, первый это ссылка на текущий класс, второй параметр это webdriver.
Ниже приведен пример простого PageObject класса в котором инициализируется поле класса simplePage:

```kotlin
package example

import core.khtml.annotations.Page
import core.khtml.loader.KHTML
import core.khtml.webdriver.WebPage
import org.openqa.selenium.WebDriver
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

@Page
interface MainPageElements : WebPage {

    fun mainNav(): MainNav

    fun header(): Header

    fun portfolio(): Portfolio

    fun about(): About

    fun contact(): List<Contact>
}

class MainPageImpl constructor(driver: WebDriver) {
    lateinit var mainPageElements: MainPageElements

    init {
        KHTML.decorate(this, driver)
    }

    @Test
    fun existsLogoImg() {
        assertTrue(mainPageElements.header().logoImg().exists)
    }
}
```


