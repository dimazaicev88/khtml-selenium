package org.intsite.khtml.element

import org.intsite.khtml.throttle.Throttle
import org.intsite.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver
import java.io.File

class FileInput(xpath: String, driver: WebDriver) :
    CustomElement<FileInput>(xpath, driver) {

    fun setFileToUpload(fileName: String): FileInput {
        Thread.sleep(Throttle.timeBeforeFileUpload(driver))
        execWebElementAction(xpath, driver) {
            it.sendKeys(File(fileName).path)
        }
        Thread.sleep(Throttle.timeAfterFileUpload(driver))
        return this
    }
}
