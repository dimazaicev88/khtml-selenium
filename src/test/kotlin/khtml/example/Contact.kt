package khtml.example

import org.intsite.khtml.annotations.Element
import org.intsite.khtml.annotations.Fragment
import org.intsite.khtml.annotations.InjectContext
import org.intsite.khtml.element.Button
import org.intsite.khtml.element.HtmlElement
import org.intsite.khtml.element.TextInput

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
