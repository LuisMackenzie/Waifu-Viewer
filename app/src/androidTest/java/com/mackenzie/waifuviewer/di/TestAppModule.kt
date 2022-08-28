package com.mackenzie.waifuviewer.di

import com.mackenzie.waifuviewer.data.db.FavoriteDao
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
    fun provideImDao(): WaifuImDao = FakeWaifuImDao()

    @Provides
    @Singleton
    fun providePicDao(): WaifuPicDao = FakeWaifuPicDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(): FavoriteDao = FakeFavoriteDao()

    @Provides
    @Singleton
    fun provideWaifuService(): RemoteConnect = RemoteConnect(
        FakeRemoteImService(buildImRemoteWaifus(1, 2, 3, 4, 5, 6)),
        FakeRemotePicsService(buildPicRemoteWaifus(1, 2, 3, 4, 5, 6)))


}