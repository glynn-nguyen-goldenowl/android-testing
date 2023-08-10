package com.example.androidtesting.ui.shopping

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.androidtesting.R
import com.example.androidtesting.assertion.RecyclerViewItemCountAssertion
import com.example.androidtesting.data.source.FakeShoppingRepository
import com.example.androidtesting.data.source.local.ShoppingItem
import com.example.androidtesting.launchFragmentInHiltContainer
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ShoppingFragmentTest{

    @get:Rule
    val hiltRule = HiltAndroidRule(this)




    @UiThreadTest
    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @Test
    fun clickOnAddButton_navigateToAddItemScreen(){
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        launchFragmentInHiltContainer<ShoppingFragment> {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(this.requireView(), navController)
        }
        onView(withId(R.id.fabAddShoppingItem)).perform(click())

        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.addShoppingItemFragment)
    }

    @Test
    fun loadShoppingItemSuccess(){
        var testViewModel : ShoppingViewModel? = null
        launchFragmentInHiltContainer<ShoppingFragment> {
            testViewModel = this.viewModel
        }
        val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
        testViewModel?.insertShoppingItem(shoppingItem)
        onView(withId(R.id.rvShoppingItems)).check(RecyclerViewItemCountAssertion(1))
    }

    @Test
    fun loadShoppingItemError(){
        var testViewModel : ShoppingViewModel? = null
        launchFragmentInHiltContainer<ShoppingFragment> {
            testViewModel = this.viewModel
        }
        (testViewModel?.shoppingRepository as FakeShoppingRepository).setReturnShoppingItemError(true)
        val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
        testViewModel?.insertShoppingItem(shoppingItem)
        onView(withId(R.id.rvShoppingItems)).check(RecyclerViewItemCountAssertion(0))
    }

    @Test
    fun loadShoppingItemErrorAndConsumeError(){
        var testViewModel : ShoppingViewModel? = null
        launchFragmentInHiltContainer<ShoppingFragment> {
            testViewModel = this.viewModel
        }
        (testViewModel?.shoppingRepository as FakeShoppingRepository).setReturnShoppingItemError(true)
        val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
        testViewModel?.insertShoppingItem(shoppingItem)

    }

    @Test
    fun swipeToDeleteShoppingItem(){
        var testViewModel : ShoppingViewModel? = null
        launchFragmentInHiltContainer<ShoppingFragment> {
            testViewModel = this.viewModel
        }
        val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
        testViewModel?.insertShoppingItem(shoppingItem)
        onView(withId(R.id.rvShoppingItems)).check(RecyclerViewItemCountAssertion(1))

        onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingItemViewHolder>(
                0,
                ViewActions.swipeLeft()
            )
        )

        onView(withId(R.id.rvShoppingItems)).check(RecyclerViewItemCountAssertion(0))

    }

    @Test
    fun swipeToDeleteShoppingItemAndUndo(){
        var testViewModel : ShoppingViewModel? = null
        var fragment : ShoppingFragment?= null
        launchFragmentInHiltContainer<ShoppingFragment> {
            testViewModel = this.viewModel
            fragment = this
        }
        val shoppingItem = ShoppingItem("banana", 1, 100, "", 1)
        testViewModel?.insertShoppingItem(shoppingItem)


        onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ShoppingItemAdapter.ShoppingItemViewHolder>(
                0,
                ViewActions.swipeLeft()
            )
        )

        onView(withId(R.id.rvShoppingItems)).check(RecyclerViewItemCountAssertion(0))

        fragment?.performSnackBarAction()

        onView(withId(R.id.rvShoppingItems)).check(RecyclerViewItemCountAssertion(1))

    }

}