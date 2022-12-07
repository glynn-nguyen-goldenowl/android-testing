package com.example.androidtesting.data.source

import com.example.androidtesting.data.DataResult
import com.example.androidtesting.data.source.local.ShoppingItem
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class FakeShoppingRepositoryTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var shoppingRepository: ShoppingRepository

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun insertShoppingItem() {
        runTest {
            val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
            shoppingRepository.insertShoppingItem(shoppingItem)
            val result = shoppingRepository.observeAllShoppingItem().first()
            assertThat(result).isInstanceOf(DataResult.Success::class.java)
            val allItems = (result as DataResult.Success).data
            assertThat(allItems).contains(shoppingItem)
        }

    }

    @Test
    fun deleteShoppingItem() {
        runTest {
            val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
            val shoppingItem1 = ShoppingItem("apple", 1, 100, "", 2)

            shoppingRepository.insertShoppingItem(shoppingItem)
            shoppingRepository.insertShoppingItem(shoppingItem1)

            shoppingRepository.deleteShoppingItem(shoppingItem)

            val result = shoppingRepository.observeAllShoppingItem().first()
            assertThat(result).isInstanceOf(DataResult.Success::class.java)
            val allItems = (result as DataResult.Success).data
            assertThat(allItems).contains(shoppingItem1)
        }
    }

    @Test
    fun observeAllShoppingItems() {
        runTest {
            val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
            val shoppingItem1 = ShoppingItem("apple", 1, 100, "", 2)
            shoppingRepository.insertShoppingItem(shoppingItem)
            shoppingRepository.insertShoppingItem(shoppingItem1)
            val result = shoppingRepository.observeAllShoppingItem().first()
            assertThat(result).isInstanceOf(DataResult.Success::class.java)
            val allItems = (result as DataResult.Success).data
            assertThat(allItems).contains(shoppingItem)
            assertThat(allItems).contains(shoppingItem1)
        }
    }

    @Test
    fun observeTotalPrice(){
        runTest {
            val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
            val shoppingItem1 = ShoppingItem("apple", 2, 100, "", 2)
            shoppingRepository.insertShoppingItem(shoppingItem)
            shoppingRepository.insertShoppingItem(shoppingItem1)

            val result = shoppingRepository.observeTotalPrice().first()
            assertThat(result).isInstanceOf(DataResult.Success::class.java)

            val totalPrice = (result as DataResult.Success).data

            assertThat(totalPrice).isEqualTo(shoppingItem.amount * shoppingItem.price + shoppingItem1.amount * shoppingItem1.price)
        }
    }

    @Test
    fun searchImageSuccess(){
        runTest {
            val result = shoppingRepository.searchImage("TEST").first()
            assertThat(result).isInstanceOf(DataResult.Success::class.java)
        }
    }

    @Test
    fun searchImageError(){
        runTest {
            (shoppingRepository as FakeShoppingRepository).shouldImageSearchError = true
            val result = shoppingRepository.searchImage("TEST").first()
            assertThat(result).isInstanceOf(DataResult.Error::class.java)
        }
    }
}