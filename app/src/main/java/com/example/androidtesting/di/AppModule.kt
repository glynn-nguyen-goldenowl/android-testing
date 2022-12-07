package com.example.androidtesting.di

import android.content.Context
import androidx.room.Room
import com.example.androidtesting.BuildConfig
import com.example.androidtesting.data.source.DefaultShoppingRepository
import com.example.androidtesting.data.source.LocalDataSource
import com.example.androidtesting.data.source.RemoteDataSource
import com.example.androidtesting.data.source.ShoppingRepository
import com.example.androidtesting.data.source.local.DefaultLocalDataSource
import com.example.androidtesting.data.source.local.ShoppingDao
import com.example.androidtesting.data.source.local.ShoppingItemDatabase
import com.example.androidtesting.data.source.remote.DefaultRemoteDataSource
import com.example.androidtesting.data.source.remote.PixabayApi
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


}