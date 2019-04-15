package core.khtml.element

import core.khtml.dump.DumpInfo
import core.khtml.utils.WebDriverUtils.execWebElementAction
import org.openqa.selenium.WebDriver
import java.io.File

class FileInput(private val strXpath: String, driver: WebDriver, private val dumpInfo: DumpInfo? = null) :
    CustomElement<FileInput>(strXpath, driver, dumpInfo) {

    fun setFileToUpload(fileName: String, dumpInfo: DumpInfo?) {
        val filePath = getFilePath(fileName)
        execWebElementAction(strXpath, driver, dumpInfo) {
            it.sendKeys(filePath)
        }
    }

    private fun getFilePath(fileName: String): String {
        return getPathForSystemFile(fileName)
    }

    private fun getPathForSystemFile(fileName: String): String {
        val file = File(fileName)
        return file.path
    }
}
