package fragments.generic

import org.intsite.khtml.loader.KHTML
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class Generic {
    private var driver: ChromeDriver = Mockito.mock(ChromeDriver::class.java)
    private lateinit var catalogBrandsPage: CatalogBrandsPage

    init {
        KHTML.decorate(this, driver)
    }

    @Test
    fun testFragmentGeneric() {
        assertEquals(
                catalogBrandsPage.findItem(1).qty().xpath,
                "//table[@id='BaseTable']//tr[@class='Item' and @num=1]//li[@id='qty']"
        )
        assertEquals(
                catalogBrandsPage.findItem(1).brandName().xpath,
                "//table[@id='BaseTable']//tr[@class='Item' and @num=1]//span[@class='brandName']"
        )
        assertEquals(catalogBrandsPage.findHeaderItem().logo().xpath, ".//td[@id='Item']//img[@id='logo']")
    }

    @Test
    fun testListFragmentsGeneric() {
        Mockito.`when`(driver.findElements(By.xpath(".//table[@id='BaseTable']//tr[@class='Item']")))
                .thenReturn(
                        listOf(
                                Mockito.mock(WebElement::class.java),
                                Mockito.mock(WebElement::class.java),
                                Mockito.mock(WebElement::class.java),
                        )
                )
        assertEquals(
                catalogBrandsPage.findAllItem()[1].brandName().xpath,
                "(//table[@id='BaseTable'])[2]//span[@class='brandName']"
        )
    }

    @Test
    fun testFragmentWithoutGeneric() {
        Mockito.`when`(driver.findElements(By.xpath(".//span[@id='brands']")))
                .thenReturn(
                        listOf(
                                Mockito.mock(WebElement::class.java),
                                Mockito.mock(WebElement::class.java),
                                Mockito.mock(WebElement::class.java),
                        )
                )
        assertEquals(catalogBrandsPage.giftInfo().context().xpath, "//div[@id='gift']")
        assertEquals(
                catalogBrandsPage.giftInfo().giftName().xpath,
                "//div[@id='gift']//span[@id='giftName']"
        )
        assertEquals(catalogBrandsPage.listBrands()[0].link().xpath,".//span[@id='brands']//a[@id='brandLink']")
    }

    @Test
    fun testListFragmentWithoutGeneric() {
        Mockito.`when`(driver.findElements(By.xpath("//div[@id='gift']//span[@class='prices']")))
                .thenReturn(
                        listOf(
                                Mockito.mock(WebElement::class.java),
                                Mockito.mock(WebElement::class.java),
                                Mockito.mock(WebElement::class.java),
                        )
                )
        assertEquals(catalogBrandsPage.giftInfo().listPriceSteps()[0].context().xpath, "//div[@id='gift']//span[@class='prices']")
        assertEquals(
                catalogBrandsPage.giftInfo().listPriceSteps()[0].price().xpath,
                "//div[@id='gift']//span[@class='prices']//span[@class='priceStep']"
        )
    }

    @Test
    fun testContextFragmentsGeneric() {
        assertEquals(
                catalogBrandsPage.findItem(1).context().xpath,
                "//table[@id='BaseTable']//tr[@class='Item' and @num=1]"
        )
    }

    @Test
    fun testContextFragmentGeneric() {
        Mockito.`when`(driver.findElements(By.xpath("//table[@id='BaseTable']//tr[@class='Item']")))
                .thenReturn(
                        listOf(
                                Mockito.mock(WebElement::class.java),
                                Mockito.mock(WebElement::class.java),
                                Mockito.mock(WebElement::class.java),
                        )
                )
        assertEquals(
                catalogBrandsPage.findAllItem()[1].context().xpath,
                "(//table[@id='BaseTable']//tr[@class='Item'])[2]"
        )
    }
}