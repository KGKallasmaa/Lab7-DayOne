package ee.ut.math.tvt.salessystem.logic

import org.junit.Test

class ShoppingCartTest {

    @Test
    public void AddingExistingItem(){};
    @Test
    public void AddingNewItem(){};
    @Test
    public void AddingItemWithNegativeQuantity(){};
    @Test
    public void AddingItemWithQuantityTooLarge (){};
    @Test
    public void AddingItemWithQuantitySumTooLarge(){};

    @Test
    public void SubmittingCurrentPurchaseDecreasesStockItemQuantity(){};
    @Test
    public void SubmittingCurrentPurchaseBeginsAndCommitsTransaction(){};
    @Test
    public void SubmittingCurrentOrderCreatesHistoryItem(){};
    @Test
    public void SubmittingCurrentOrderSavesCorrectTime(){};
    @Test
    public void CancellingOrder(){};
    @Test
    public void CancellingOrderQuanititesUnchanged(){};

}
