package com.example.androidtesting.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.androidtesting.BuildConfig
import com.example.androidtesting.R
import com.example.androidtesting.data.source.DefaultShoppingRepository
import com.example.androidtesting.data.source.LocalDataSource
import com.example.androidtesting.data.source.RemoteDataSource
import com.example.androidtesting.data.source.ShoppingRepository
import com.example.androidtesting.data.source.local.DefaultLocalDataSource
import com.example.androidtesting.data.source.local.ShoppingDao
import com.example.androidtesting.data.source.local.ShoppingItemDatabase
import com.example.androidtesting.data.source.remote.DefaultRemoteDataSource
import com.example.androidtesting.data.source.remote.PixabayApi
import com.example.androidtesting.ui.picker.ImageListAdapter
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingDatabase(@ApplicationContext context: Context): ShoppingItemDatabase = Room.databaseBuilder(
        context,
        ShoppingItemDatabase::class.java,
        "shopping_db"
    ).build()

    @Singleton
    @Provides
    fun provideShoppingDao(shoppingItemDatabase: ShoppingItemDatabase) =
        shoppingItemDatabase.shoppingDao()

    @Singleton
    @Provides
    fun provideLocalDataSource(shoppingDao: ShoppingDao): LocalDataSource = DefaultLocalDataSource(shoppingDao)

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayApi {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(Gson()))
            .baseUrl(BuildConfig.BASE_URL).build().create(PixabayApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(pixabayApi: PixabayApi): RemoteDataSource = DefaultRemoteDataSource(pixabayApi)

    @Singleton
    @Provides
    fun provideShoppingRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): ShoppingRepository = DefaultShoppingRepository(localDataSource, remoteDataSource)


    @Singleton
    @Provides
    fun provideRequestManager(
        @ApplicationContext context: Context
    ): RequestManager{
        return Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image))
    }


    @Provides
    fun provideImageListAdapter(
        requestManager: RequestManager
    ): ImageListAdapter{
        return ImageListAdapter(requestManager)
    }


}