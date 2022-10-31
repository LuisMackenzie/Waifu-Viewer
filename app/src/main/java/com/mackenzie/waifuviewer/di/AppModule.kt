package com.mackenzie.waifuviewer.di

import android.app.Application
import androidx.room.Room
import com.mackenzie.waifuviewer.data.*
import com.mackenzie.waifuviewer.data.datasource.*
import com.mackenzie.waifuviewer.data.db.WaifuDataBase
import com.mackenzie.waifuviewer.data.db.datasources.FavoriteDataSource
import com.mackenzie.waifuviewer.data.db.datasources.RoomImDataSource
import com.mackenzie.waifuviewer.data.db.datasources.RoomPicDataSource
import com.mackenzie.waifuviewer.data.server.*
import com.mackenzie.waifuviewer.domain.ApiUrl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideApiUrl(): ApiUrl = ApiUrl()

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

        val builderBest = Retrofit.Builder()
            .baseUrl(apiUrl.nekosBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val serviceIm = builderIm.create(WaifuImService::class.java)
        val servicePics = builderPics.create(WaifuPicService::class.java)
        val serviceBest = builderBest.create(WaifuBestService::class.java)

        val connection = RemoteConnect(serviceIm, servicePics, serviceBest)

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
    abstract fun bindFavoriteDataSource(favoriteDataSource: FavoriteDataSource): FavoriteLocalDataSource

    @Binds
    abstract fun bindRemoteImDataSource(remoteImDataSource: ServerImDataSource): WaifusImRemoteDataSource

    @Binds
    abstract fun bindRemotePicDataSource(remotePicDataSource: ServerPicDataSource): WaifusPicRemoteDataSource

    @Binds
    abstract fun bindLocationDataSource(locationDataSource: PlayServicesLocationDataSource): LocationDataSource

    @Binds
    abstract fun bindPermissionChecker(permissionChecker: AndroidPermissionChecker): PermissionChecker

}