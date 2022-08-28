package com.mackenzie.waifuviewer.di

import android.app.Application
import androidx.room.Room
import com.mackenzie.waifuviewer.data.db.FavoriteDao
import com.mackenzie.waifuviewer.data.db.WaifuDataBase
import com.mackenzie.waifuviewer.data.db.WaifuImDao
import com.mackenzie.waifuviewer.data.db.WaifuPicDao
import com.mackenzie.waifuviewer.data.server.RemoteConnect
import com.mackenzie.waifuviewer.ui.buildImRemoteWaifus
import com.mackenzie.waifuviewer.ui.buildPicRemoteWaifus
import com.mackenzie.waifuviewer.ui.fakes.*
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
    fun provideWaifuService(): RemoteConnect = RemoteConnect(
        FakeRemoteImService(listOf()),
        FakeRemotePicsService(listOf()))


}