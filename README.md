[![](https://jitpack.io/v/dimazaicev88/khtml-selenium.svg)](https://jitpack.io/#dimazaicev88/khtml-selenium)

1. **Аннотации** 
2. **Классы врапперы для webelement**
3. **Инициализация Page Object.** 
4. **Ожидания элементов.**

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

**@Fragment(xpath="", val inheritance: Boolean = false)**
- Аннотацией помечается метод или класс. Если аннотация указана над методом то данный метод должен вернуть абстрактный класс фрагмента или коллекцию фрагментов. Данная аннотация принимает  не обязательный параметр селектор xpath, который будет использован для построения xpath, пример:
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
Параметр inheritance разрешает или запрещает наследование xpath от parent класса

Пример:
```kotlin
@Fragment(".//div[@id='fancybox-wrap']", inheritance = true)
interface FancyBoxBase

interface FBBasket : FancyBoxBase {

    @Element(".//div")
    fun content(): HtmlElement
}

content().xpath // вернёт xpath - .//div[@id='fancybox-wrap']/div

```

 
**@Element(xpath="")**
- Аннотаций помечается метод абстрактного класса возвращающий элемент(Button,CheckBox...) или коллекцию элементов, Методы с данной аннотацией могут возвращаться только объекты реализующие абстрактный класс CustomElement<T>,пример:
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
- Аннотация служит для ожидания элементов и фрагментов в абстрактном классе. Аннотация принимает 3 параметра, два из которых имеют значения по умолчанию. Аннотация указывается только над методами.
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

**@JSCall(val funcName: String = "", val js: String = "", val async: Boolean = false)**
- Аннотация служит для выполнения кода на Java Script или вызова функции на JavaScript. Если указан funcName будет вызвана функция указанная в funcName, если funcName не указана, будет выполнен блок кода указанный в  js  
Параметры аннотации:
1. funcName- Название функции объявленной в JavaScript 
2. js- Код на JavaScript
3. async- Способ выполнения JavaScript кода, если true то код будет выполнен асинхронно иначе синхронно

Пример:
```kotlin
@JSCall(funcName = "addInFavorite", async = true)
fun addInFavoriteWithoutUi(@Param("idFavoriteList") idFavoriteList: Int, @Param("codeGoods") codeGoods: Int)

@JSCall(js = "console.log(1)", async = true)
fun printConsoleLog()
```

**@NotUseParentXpath**
- Аннотация которая запрещает наследование xpath от родителя

Пример:
```kotlin
@Fragment(".//div[@id='parent']")
interface ParentFragment {

    @NotUseParentXpath
    @Fragment(".//div[@class='childFragment']")
    fun childFragment(): ChildFragment
}

interface ChildFragment {

    @Element(".//div[@id='btn']")
    fun someButton(): Button
}
childFragment().someButton().xpath // вернет .//div[@class='childFragment']/div[@id='btn']

```
# Классы врапперы для webelement

**В фреймворк встроены следующие классы врапперы для webelement**:
- Button
- CheckBox
- FileInput
- HtmlElement
- Image
- Link
- Radio
- Text
- TextInput
- Select

**Список общих свойств и методов**:
- element
- location: Point
- text: String
- tagName: String
- isSelected: Boolean
- isEnabled: Boolean
- size: Dimension
- rect: Rectangle
- isDisplayed: Boolean
- source: String
- exists: Boolean
- isDisplayedOnJs: Boolean
- addClass(className: String): T
- removeClass(className: String): T
- click(): T
- move(): T
- submit(): T
- jse: JsExecutor<T> - доступ к JsExecutor, классу который содержит методы для работы с элементами с помощью JavaScript.
- xpath - Путь к элементу
- sendKeys(vararg charSequences: CharSequence): T
- setValue(value: CharSequence): T (тоже самое что вызвать clear(),sendKeys() у элемента)
- clear(): T
- attr(name: String): String
- findElements(xpath: String): List<WebElement>
- findElement(xpath: String): WebElement
- setValueOnJs(value: String): T
- cssValue(name: String): String

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
# Ожидания элементов

В фреймворке поддерживается стандартные ожидания, и пользовательские ожидания, причем ожидания доступные у всех элементов.

Пример **стандартного ожидания**:

```kotlin
@Fragment(".//div[contains(@class,'TipTip--is-active') and contains( @class,'TipTip')]", inheritance = true)
interface TipTipBase {

    @Wait(condition = WaitCondition.TEXT_PRESENT)
    @Element(".//div[@class='TipTip__content']")
    fun content(): HtmlElement
}

class MainPageImpl constructor(driver: WebDriver) {
    lateinit var mainPageElements: TipTipBase

    init {
        KHTML.decorate(this, driver)
    }

    @Test
    fun existsLogoImg() {
        mainPageElements.content().wait(WaitCondition.TEXT_PRESENT).text
    }
}
```
Список стандартных ожиданий:
 * TO_CLICKABLE - у элемента свойство displayed==true и enabled==true 
 * ENABLED - У элемента свойство displayed==true 
 * DISABLED - У элемента свойство enabled!=true 
 * AJAX -  window.jQuery.active == 0
 * DISPLAY - У элемента свойство displayed==true 
 * TEXT_PRESENT - Свойство text.length>0 
 * INVISIBLE - Свойство displayed!=true
 * NOT_EXITS - Отсутствие элемента на странице
 
Пример **пользовательского ожидания**:

```kotlin
@Fragment(".//div[contains(@class,'TipTip--is-active') and contains( @class,'TipTip')]", inheritance = true)
interface TipTipBase {

    @Wait(condition = WaitCondition.TEXT_PRESENT)
    @Element(".//div[@class='TipTip__content']")
    fun content(): HtmlElement
}

class MainPageImpl constructor(private val driver: WebDriver) {
    lateinit var mainPageElements: TipTipBase

    init {
        KHTML.decorate(this, driver)
    }

    @Test
    fun existsLogoImg() {
        mainPageElements.content().waitCustomCondition {
            driver.findElement(By.xpath(it)).text.contains("some text")
        }
    }
}
```



