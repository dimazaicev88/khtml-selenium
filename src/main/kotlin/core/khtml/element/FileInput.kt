package core.khtml.element

import core.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver
import java.io.File

class FileInput(_xpath: String, driver: WebDriver) : CustomElement<FileInput>(_xpath, driver) {

    fun setFileToUpload(fileName: String): FileInput {
        execWebElementAction(xpath, driver) {
            it.sendKeys(File(fileName).path)
        }
        return this
    }
}
