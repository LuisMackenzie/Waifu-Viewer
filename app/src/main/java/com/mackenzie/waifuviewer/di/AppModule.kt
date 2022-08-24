package com.mackenzie.waifuviewer.di

import android.app.Application
import androidx.room.Room
import com.mackenzie.waifuviewer.data.*
import com.mackenzie.waifuviewer.data.datasource.*
import com.mackenzie.waifuviewer.data.db.FavoriteDataSource
import com.mackenzie.waifuviewer.data.db.RoomImDataSource
import com.mackenzie.waifuviewer.data.db.RoomPicDataSource
import com.mackenzie.waifuviewer.data.db.WaifuDataBase
import com.mackenzie.waifuviewer.data.server.ServerImDataSource
import com.mackenzie.waifuviewer.data.server.ServerPicDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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