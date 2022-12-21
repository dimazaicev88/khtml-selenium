package fragments.context

import org.intsite.khtml.annotations.*
import org.intsite.khtml.element.*

@Fragment("")
interface EmptyFragmentWithContext {

    @InjectContext
    fun context(): HtmlElement
}

@Page
interface PageWithContext {

    @InjectContext
    fun context(): HtmlElement
}

@Fragment(".//div[@id='TestFragment']")
interface FragmentWithContext {

    @InjectContext
    fun context(): HtmlElement

    fun fragmentTwo(): FragmentTwo

    fun listFragment(): List<ItemFragment>
}

@Fragment(".//span[@class='FragmentItem']")
interface ItemFragment {

    @InjectContext
    fun context(): HtmlElement

    @Element("//div[@id='element-fragmentItem']")
    fun element(): HtmlElement

}

@Fragment("//div[@id='TestFragment2']")
interface FragmentTwo {

    @InjectContext
    fun context(): HtmlElement

    @Element("//div[@id='element-testFragment2']")
    fun element(): HtmlElement
}



@Fragment("(.//h2[text()='Вы недавно смотрели']/ancestor::div/following-sibling::div)[1]")
interface WatchedRecently {
    @Fragment(".//*[@data-test-code-goods='#{codeGoods}']")
    fun findItem(@Param("codeGoods") codeGoods: Int): CatalogItem

    @Fragment(".//*[@data-test-element='goods-item']")
    fun findAllItem(): List<CatalogItem>

    @InjectContext
    fun context(): HtmlElement
}


interface CatalogItem {

    @Element("//*[@data-test-element='icon-hit']")
    fun iconHit(): HtmlElement

    @Element("//*[@data-test-element='link-add-goods-in-compare' and text()='Сравнить']")
    fun linkAddInCompare(): Link

    @Element("//*[@data-test-element='link-add-goods-in-favorite']")
    fun linkInFavorite(): Link

    @Element(".//div[@data-test-element='badge-price-week']")
    fun badgePriceWeek(): HtmlElement

    @Element("//*[@data-test-element='icon-low-price']")
    fun iconLowPrice(): HtmlElement

    @Element("//*[@data-test-element='input-qty']")
    fun inputQty(): TextInput

    @Element("//*[@data-test-element='price-goods' and @data-test-is-current-price='1']")
    fun price(): Text

    @Element("//*[@data-test-element='price-goods' and @data-test-is-current-price='0' and @data-test-is-underline-price='1']")
    fun underlinePrice(): Text

    @Element("//*[@data-test-element='button-add-goods-in-basket']")
    fun btnAddGoodInBasket(): Button

    @Element("//*[@data-test-element='status-available-goods-text']")
    fun textAvailableGoods(): Text

    @Element("//*[@data-test-element='status-available-goods-link']")
    fun linkAvailableGoods(): HtmlElement

    @Element("//span[normalize-space(text())='Гарантия цены']")
    fun iconGuaranteePrice(): HtmlElement

    @Element("//div[contains(@class,'ProductState') and contains(text(),'Временно отсутствует на')]")
    fun textTemporarilyNotAvailable(): HtmlElement

    @Element("//td[normalize-space(text())='На складе']/following-sibling::td")
    fun inStock(): HtmlElement

    @Element("//td/span[@class='AvailabilityBox__nowrap' and text()='#{pzkDays} д.']/parent::td/following-sibling::td")
    fun textAvailableWithinDays(@Param("pzkDays") pzkDays: String): HtmlElement

    @Element("//*[@data-test-element='badge-sale']")
    fun badgeSale(): HtmlElement

    @Element("//*[@data-test-element='incremental-percentage']")
    fun incrementPercent(): HtmlElement

    @Element(".//*[@data-test-element='badge-promo-set'] | .//*[@data-test-element='badge-hot-gift']")
    fun badgePromo(): HtmlElement

    @InjectContext
    fun context(): HtmlElement

    @Element(".//div[text()='#{notice}']")
    fun noticeDeliveryTime(@Param("notice") notice: String): HtmlElement

    @Element(".//div[contains(@class,'actionsBadge--fixPrice')]")
    fun badgePromoFixPrice(): HtmlElement

    @Element(".//span[contains(@class,'actionsBadgeText--price')]")
    fun textBadgeFixPrice(): Text

    @Element(".//div[contains(@class,'actionsBadge--bestPrice')]")
    fun badgeBestPrice(): HtmlElement

    @Element("//div[contains(@class,'actionsBadge--productSpecial')]")
    fun iconDiscountByCount(): HtmlElement

    @Element(".//span[contains(@class,'js-Product__bonus')]")
    fun amountBonuses(): Text

    @Element(".//*[@data-test-is-red-price='1' and @data-test-element='price-goods']")
    fun redPrice(): HtmlElement
}