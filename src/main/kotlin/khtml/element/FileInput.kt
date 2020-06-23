package khtml.element

import khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver
import java.io.File

class FileInput(_xpath: String, driver: WebDriver, testName: String? = null) : CustomElement<FileInput>(_xpath, driver, testName) {

    fun setFileToUpload(fileName: String): FileInput {
        execWebElementAction(xpath, driver) {
            it.sendKeys(File(fileName).path)
        }
        return this
    }
}
