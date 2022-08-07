package com.mackenzie.waifuviewer.di

import android.app.Application
import androidx.room.Room
import com.mackenzie.waifuviewer.data.*
import com.mackenzie.waifuviewer.data.datasource.*
import com.mackenzie.waifuviewer.data.db.RoomImDataSource
import com.mackenzie.waifuviewer.data.db.RoomPicDataSource
import com.mackenzie.waifuviewer.data.db.WaifuImDataBase
import com.mackenzie.waifuviewer.data.db.WaifuPicDataBase
import com.mackenzie.waifuviewer.data.server.ServerImDataSource
import com.mackenzie.waifuviewer.data.server.ServerPicDataSource
import com.mackenzie.waifuviewer.usecases.FindWaifuImUseCase
import com.mackenzie.waifuviewer.usecases.FindWaifuPicUseCase
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
    fun provideImDatabase(app: Application) = Room.databaseBuilder(
        app,
        WaifuImDataBase::class.java,
        "waifu-im-database"
    ).build()

    @Provides
    @Singleton
    fun provideImDao(db: WaifuImDataBase) = db.waifuImDao()

    @Provides
    @Singleton
    fun providePicDatabase(app: Application) = Room.databaseBuilder(
        app,
        WaifuPicDataBase::class.java,
        "waifu-pic-database"
    ).build()

    @Provides
    @Singleton
    fun providePicDao(db: WaifuPicDataBase) = db.waifuPicDao()

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {

    @Binds
    abstract fun bindLocalImDataSource(localImDataSource: RoomImDataSource): WaifusImLocalDataSource

    @Binds
    abstract fun bindLocalPicDataSource(localPicDataSource: RoomPicDataSource): WaifusPicLocalDataSource

    @Binds
    abstract fun bindRemoteImDataSource(remoteImDataSource: ServerImDataSource): WaifusImRemoteDataSource

    @Binds
    abstract fun bindRemotePicDataSource(remotePicDataSource: ServerPicDataSource): WaifusPicRemoteDataSource

    // @Binds
    // abstract fun bindRemotePicRepo(): WaifusPicRepository

    // @Binds
    // abstract fun bindRemoteImRepo(localImDataSource: RoomImDataSource, remoteImDataSource: ServerImDataSource): WaifusImRepository

    // @Binds
    // abstract fun bindRemotePicRepo(localPicDataSource: RoomPicDataSource, remotePicDataSource: ServerPicDataSource): WaifusPicRepository

    @Binds
    abstract fun bindLocationDataSource(locationDataSource: PlayServicesLocationDataSource): LocationDataSource

    @Binds
    abstract fun bindPermissionChecker(permissionChecker: AndroidPermissionChecker): PermissionChecker

}