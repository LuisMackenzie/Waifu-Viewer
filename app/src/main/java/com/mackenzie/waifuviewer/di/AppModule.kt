package com.mackenzie.waifuviewer.di

import android.app.Application
import androidx.room.Room
import com.mackenzie.waifuviewer.data.*
import com.mackenzie.waifuviewer.data.datasource.*
import com.mackenzie.waifuviewer.data.db.WaifuDataBase
import com.mackenzie.waifuviewer.data.db.datasources.*
import com.mackenzie.waifuviewer.data.server.*
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.domain.ApiUrl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.databaseBuilder(
        app,
        WaifuDataBase::class.java,
        "waifu-database"
    ).build()

    @Provides
    @Singleton
    fun provideImDao(db: WaifuDataBase) = db.waifuImDao()

    @Provides
    @Singleton
    fun providePicDao(db: WaifuDataBase) = db.waifuPicDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(db: WaifuDataBase) = db.favoriteDao()

    @Provides
    @Singleton
    fun provideBestDao(db: WaifuDataBase) = db.waifuBestDao()

    @Provides
    @Singleton
    fun provideApiUrl(): ApiUrl = ApiUrl()

    @Provides
    @Singleton
    fun provideOkHttpClient():OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(this)
            .build()
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

        val builderOpenAi = Retrofit.Builder()
            .baseUrl(apiUrl.openAiBaseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val serviceIm = builderIm.create(WaifuImService::class.java)
        val servicePics = builderPics.create(WaifuPicService::class.java)
        val serviceBest = builderBest.create(WaifuBestService::class.java)
        val serviceMoe = builderMoe.create(WaifuTraceMoeService::class.java)
        val serviceOpenAi = builderOpenAi.create(OpenAIService::class.java)

        val connection = RemoteConnect(serviceIm, servicePics, serviceBest, serviceMoe, serviceOpenAi)

        return connection
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {

    @Binds
    abstract fun bindLocalImDataSource(localImDataSource: RoomImDataSource): WaifusImLocalDataSource

    @Binds
    abstract fun bindLocalPicDataSource(localPicDataSource: RoomPicDataSource): WaifusPicLocalDataSource

    @Binds
    abstract fun bindLocalBestDataSource(localBestDataSource: RoomBestDataSource): WaifusBestLocalDataSource

    @Binds
    abstract fun bindFavoriteDataSource(favoriteDataSource: FavoriteDataSource): FavoriteLocalDataSource

    @Binds
    abstract fun bindRemoteBestDataSource(remoteBestDataSource: ServerBestDataSource): WaifusBestRemoteDataSource

    @Binds
    abstract fun bindRemoteImDataSource(remoteImDataSource: ServerImDataSource): WaifusImRemoteDataSource

    @Binds
    abstract fun bindRemotePicDataSource(remotePicDataSource: ServerPicDataSource): WaifusPicRemoteDataSource

    @Binds
    abstract fun bindRemoteMoeDataSource(remoteMoeDataSource: ServerMoeDataSource): WaifusMoeRemoteDataSource

    @Binds
    abstract fun bindRemoteOpenAiDataSource(remoteOpenAiDataSource: OpenAiDataSource): OpenAiRemoteDataSource

    @Binds
    abstract fun bindLocationDataSource(locationDataSource: PlayServicesLocationDataSource): LocationDataSource

    @Binds
    abstract fun bindPermissionChecker(permissionChecker: AndroidPermissionChecker): PermissionChecker

}