package com.example.androidtesting.data.source.local

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ShoppingDaoTest {
    lateinit var shoppingItemDatabase: ShoppingItemDatabase

    lateinit var shoppingDao: ShoppingDao

    @Before
    fun initDatabase() {
        shoppingItemDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()

        shoppingDao = shoppingItemDatabase.shoppingDao()
    }

    @After
    fun closeDatabase() {
        shoppingItemDatabase.close()
    }

    @Test
    fun insertShoppingItem() {
        runTest {
            val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)

            shoppingDao.insertShoppingItem(shoppingItem)

            val allItems = shoppingDao.observeAllShoppingItem().first()

            assertThat(allItems).contains(shoppingItem)
        }

    }

    @Test
    fun deleteShoppingItem() {
        runTest {
            val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
            val shoppingItem1 = ShoppingItem("apple", 1, 100, "", 2)

            shoppingDao.insertShoppingItem(shoppingItem)
            shoppingDao.insertShoppingItem(shoppingItem1)

            shoppingDao.deleteShoppingItem(shoppingItem)

            val allItems = shoppingDao.observeAllShoppingItem().first()

            assertThat(allItems).doesNotContain(shoppingItem)
        }
    }

    @Test
    fun observeAllShoppingItems() {
        runTest {
            val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
            val shoppingItem1 = ShoppingItem("apple", 1, 100, "", 2)

            shoppingDao.insertShoppingItem(shoppingItem)
            shoppingDao.insertShoppingItem(shoppingItem1)


            val allItems = shoppingDao.observeAllShoppingItem().first()

            assertThat(allItems).contains(shoppingItem)
            assertThat(allItems).contains(shoppingItem1)
        }
    }

    @Test
    fun observeTotalPrice(){
        runTest {
            val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
            val shoppingItem1 = ShoppingItem("apple", 2, 100, "", 2)

            shoppingDao.insertShoppingItem(shoppingItem)
            shoppingDao.insertShoppingItem(shoppingItem1)

            val totalPrice = shoppingDao.observeTotalPrice().first()

            assertThat(totalPrice).isEqualTo(shoppingItem.amount * shoppingItem.price + shoppingItem1.amount * shoppingItem1.price)
        }
    }
}
