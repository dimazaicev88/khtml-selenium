package khtml.fragments.generic

import org.intsite.khtml.annotations.*
import org.intsite.khtml.element.*

@Page
interface CatalogBrandsPage : CatalogProducts<CatalogBrands>, BaseHeader<HeaderMainPage> {

    fun giftInfo(): GiftInfo

}

interface HeaderMainPage : BaseHeaderItem {

    @Element(".//img[@id='logo']")
    fun logo(): Image
}

interface BaseHeaderItem {

    @Element(".//input[@id='find']")
    fun inputFind(): TextInput
}

interface BaseHeader<Item : BaseHeaderItem> {

    @Fragment(".//td[@id='Item']")
    fun findHeaderItem(): Item
}

@Fragment(".//table[@id='BaseTable']")
interface CatalogProducts<out Item : ProductItem> {

    @Fragment(".//tr[@class='Item' and @num=#{id}]")
    fun findItem(@Param("id") id: Int): Item

    @Fragment(".//tr[@class='Item']")
    fun findAllItem(): List<Item>

    @Fragment(".//tr[@class='Item' and @pos=#{pos}]")
    fun findAllItem(@Param("pos") pos: Int): List<Item>
}

interface ProductItem {

    @InjectContext
    fun context(): HtmlElement

    @Element(".//li[@id='name']")
    fun nameProduct(): Text

    @Element(".//li[@id='qty']")
    fun qty(): CheckBox
}

interface CatalogBrands : ProductItem {

    @Element(".//span[@class='brandName']")
    fun brandName(): Text

    @Element(".//span[@class='brandLink']")
    fun brandLink(): Text
}

@Fragment(".//div[@id='gift']")
interface GiftInfo {

    @Element(".//span[@id='giftName']")
    fun giftName(): Text

    fun listPriceSteps(): List<PriceStep>

    @InjectContext
    fun context(): HtmlElement
}


@Fragment(".//span[@class='prices']")
interface PriceStep {

    @Element(".//span[@class='priceStep']")
    fun price(): Text

    @InjectContext
    fun context(): HtmlElement
}
