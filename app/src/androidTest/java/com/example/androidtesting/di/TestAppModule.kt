package com.example.androidtesting.di

import com.example.androidtesting.data.source.ShoppingRepository
import dagger.Module
import dagger.Provides
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