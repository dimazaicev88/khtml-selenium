package core.khtml.element

import org.openqa.selenium.WebDriver
import java.io.File

class FileInput(private val strXpath: String, driver: WebDriver) : CustomElement<FileInput>(strXpath, driver) {

    fun setFileToUpload(fileName: String) {
        val filePath = getFilePath(fileName)
        element.sendKeys(filePath)
    }

    private fun getFilePath(fileName: String): String {
        return getPathForSystemFile(fileName)
    }

    private fun getPathForSystemFile(fileName: String): String {
        val file = File(fileName)
        return file.path
    }
}
