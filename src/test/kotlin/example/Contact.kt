package example

import core.khtml.annotations.Element
import core.khtml.annotations.Fragment
import core.khtml.annotations.InjectContext
import core.khtml.element.Button
import core.khtml.element.HtmlElement
import core.khtml.element.TextInput

@Fragment(".//section[@id='contact']")
interface Contact {

    @Element(".//input[@id='name']")
    fun inputName(): List<TextInput>

    @Element(".//input[@id='email']")
    fun inputEmail(): TextInput

    @Element(".//input[@id='phone']")
    fun inputPhone(): TextInput

    @Element(".//input[@id='message']")
    fun inputMessage(): TextInput

    @Element(".//input[@id='sendMessageButton']")
    fun buttonSend(): Button

    @InjectContext
    fun context(): HtmlElement
}
