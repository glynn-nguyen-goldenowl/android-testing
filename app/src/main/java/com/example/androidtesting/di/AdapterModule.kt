package com.example.androidtesting.di

import com.bumptech.glide.RequestManager
import com.example.androidtesting.ui.picker.ImageListAdapter
import com.example.androidtesting.ui.shopping.ShoppingItemAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AdapterModule {
    @Provides
    fun provideImageListAdapter(
        requestManager: RequestManager
    ): ImageListAdapter {
        return ImageListAdapter(requestManager)
    }

    @Provides
    fun provideShoppingItemAdapter(
        requestManager: RequestManager
    ): ShoppingItemAdapter {
        return ShoppingItemAdapter(requestManager)
    }
}