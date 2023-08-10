package com.example.androidtesting.ui.shopping


import com.example.androidtesting.data.source.FakeShoppingRepository
import com.example.androidtesting.data.source.ShoppingRepository
import com.example.androidtesting.data.source.local.ShoppingItem
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ShoppingViewModelTest{
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var shoppingRepository: ShoppingRepository

    lateinit var viewModel: ShoppingViewModel

    @Before
     fun init(){
        hiltRule.inject()
        viewModel = ShoppingViewModel(shoppingRepository)
    }

    @Test
    fun showLoadingAtTheFirstTime() {
        runTest {
            val result = viewModel.uiState.first()
            Truth.assertThat(result.loading).isTrue()
        }
    }
    @Test
    fun loadShoppingItemListAndPriceSuccess() {
        runTest {
            val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
            shoppingRepository.insertShoppingItem(shoppingItem)
            val result = viewModel.uiState.dropWhile { it.loading }.first()
            Truth.assertThat(result.shoppingItemList).isNotEmpty()
        }
    }
    @Test
    fun loadShoppingItemListError() {
        runTest {
            (shoppingRepository as FakeShoppingRepository).setReturnShoppingItemError(true)
            val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
            shoppingRepository.insertShoppingItem(shoppingItem)
            val result = viewModel.uiState.dropWhile { it.errorMessage == null }.first()
            Truth.assertThat(result.errorMessage).isNotNull()
        }
    }

    @Test
    fun loadTotalPriceError() {
        runTest {
            (shoppingRepository as FakeShoppingRepository).setReturnTotalPriceError(true)
            val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
            shoppingRepository.insertShoppingItem(shoppingItem)
            val result = viewModel.uiState.dropWhile { it.errorMessage == null }.first()
            Truth.assertThat(result.errorMessage).isNotNull()
        }
    }

    @Test
    fun loadShoppingItemListErrorAndConsumeError() {
        runTest {
            (shoppingRepository as FakeShoppingRepository).setReturnShoppingItemError(true)
            val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
            shoppingRepository.insertShoppingItem(shoppingItem)
            val result = viewModel.uiState.dropWhile { it.errorMessage == null }.first()
            Truth.assertThat(result.errorMessage).isNotNull()
            viewModel.onEvent(ShoppingUiEvent.ConsumeError(result.errorMessage!!))
            val consumeResult = viewModel.uiState.dropWhile { it.errorMessage != null }.first()
            Truth.assertThat(consumeResult.errorMessage).isNull()
        }
    }

}