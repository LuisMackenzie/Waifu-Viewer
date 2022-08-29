package com.mackenzie.waifuviewer.di

import android.app.Application
import androidx.room.Room
import com.mackenzie.waifuviewer.data.db.FavoriteDao
import com.mackenzie.waifuviewer.data.db.WaifuDataBase
import com.mackenzie.waifuviewer.data.db.WaifuImDao
import com.mackenzie.waifuviewer.data.db.WaifuPicDao
import com.mackenzie.waifuviewer.data.server.RemoteConnect
import com.mackenzie.waifuviewer.data.server.WaifuImService
import com.mackenzie.waifuviewer.data.server.WaifuPicService
import com.mackenzie.waifuviewer.domain.ApiUrl
import com.mackenzie.waifuviewer.ui.buildImRemoteWaifus
import com.mackenzie.waifuviewer.ui.buildPicRemoteWaifus
import com.mackenzie.waifuviewer.ui.fakes.*
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.inMemoryDatabaseBuilder(
        app,
        WaifuDataBase::class.java
    ).build()

    @Provides
    @Singleton
    fun provideImDao(db: WaifuDataBase): WaifuImDao  = db.waifuImDao()

    @Provides
    @Singleton
    fun providePicDao(db: WaifuDataBase): WaifuPicDao = db.waifuPicDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(db: WaifuDataBase): FavoriteDao = db.favoriteDao()

    @Provides
    @Singleton
    fun provideApiUrl(): ApiUrl = ApiUrl("http://localhost:8080", "http://localhost:8080")

    @Provides
    @Singleton
    fun provideWaifuService(apiUrl: ApiUrl): RemoteConnect {
        val okHttpClient = HttpLoggingInterceptor().run {
            level = HttpLoggingInterceptor.Level.BODY
            OkHttpClient.Builder().addInterceptor(this).build()
        }


        val builderIm = Retrofit.Builder()
            .baseUrl(apiUrl.imBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val builderPics = Retrofit.Builder()
            .baseUrl(apiUrl.picsBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val serviceIm = builderIm.create(WaifuImService::class.java)
        val servicePics = builderPics.create(WaifuPicService::class.java)

        val connection = RemoteConnect(serviceIm, servicePics)

        return connection
    }

    /*@Provides
    @Singleton
    fun provideWaifuService(): RemoteConnect = RemoteConnect(
        FakeRemoteImService(listOf()),
        FakeRemotePicsService(listOf()))*/


}