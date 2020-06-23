package example

import khtml.annotations.Element
import khtml.annotations.Fragment
import khtml.annotations.InjectContext
import khtml.element.Button
import khtml.element.HtmlElement
import khtml.element.TextInput

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
