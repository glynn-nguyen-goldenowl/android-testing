package com.example.androidtesting.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.androidtesting.R
import com.example.androidtesting.data.source.ShoppingRepository
import com.example.androidtesting.ui.picker.ImageListAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Singleton
    @Provides
    fun provideShoppingRepository() = com.example.androidtesting.data.source.FakeShoppingRepository() as ShoppingRepository

}