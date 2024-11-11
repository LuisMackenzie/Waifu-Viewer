package com.mackenzie.waifuviewer.di

import android.app.Application
import androidx.room.Room
import com.mackenzie.waifuviewer.data.db.*
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.data.server.WaifuBestService
import com.mackenzie.waifuviewer.data.server.WaifuImService
import com.mackenzie.waifuviewer.data.server.WaifuPicService
import com.mackenzie.waifuviewer.data.server.WaifuTraceMoeService
import com.mackenzie.waifuviewer.domain.ApiUrl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
    fun provideBestDao(db: WaifuDataBase): WaifuBestDao  = db.waifuBestDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(db: WaifuDataBase): FavoriteDao = db.favoriteDao()

    @Provides
    @Singleton
    fun provideApiUrl(): ApiUrl = ApiUrl(
        "http://localhost:8080",
        "http://localhost:8080",
        "http://localhost:8080")

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideWaifuService(apiUrl: ApiUrl, client: OkHttpClient, moshi: Moshi): RemoteConnect {

        val builderIm = Retrofit.Builder()
            .baseUrl(apiUrl.imBaseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val builderPics = Retrofit.Builder()
            .baseUrl(apiUrl.picsBaseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val builderBest = Retrofit.Builder()
            .baseUrl(apiUrl.nekosBaseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val builderMoe = Retrofit.Builder()
            .baseUrl(apiUrl.traceMoeBaseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val serviceIm = builderIm.create(WaifuImService::class.java)
        val servicePics = builderPics.create(WaifuPicService::class.java)
        val serviceBest = builderBest.create(WaifuBestService::class.java)
        val serviceMoe = builderBest.create(WaifuTraceMoeService::class.java)

        val connection = RemoteConnect(serviceIm, servicePics, serviceBest, serviceMoe)

        return connection
    }

    /*@Provides
    @Singleton
    fun provideWaifuService(): RemoteConnect = RemoteConnect(
        FakeRemoteImService(listOf()),
        FakeRemotePicsService(listOf()))*/


}