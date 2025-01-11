package com.mackenzie.waifuviewer.di

import com.mackenzie.waifuviewer.data.PushRepository
import com.mackenzie.waifuviewer.data.datasource.NotificationLocalDataSource
import com.mackenzie.waifuviewer.data.datasource.TokenLocalDataSource
import com.mackenzie.waifuviewer.data.db.dao.WaifuFcmTokenDao
import com.mackenzie.waifuviewer.data.db.dao.WaifuPushDao
import com.mackenzie.waifuviewer.data.db.datasources.RoomFcmTokenDataSource
import com.mackenzie.waifuviewer.data.db.datasources.RoomNotificationDataSource
import com.mackenzie.waifuviewer.di.AppModule_ProvideTokenDaoFactory.provideTokenDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /*@Provides
    @Singleton
    fun provideTokenDataSource(
        dao: WaifuFcmTokenDao,
    ): TokenLocalDataSource {
        return RoomFcmTokenDataSource(dao)
    }*/

    /*@Provides
    @Singleton
    fun providePushDataSource(
        dao: WaifuPushDao
    ): NotificationLocalDataSource {
        return RoomNotificationDataSource(dao)
    }*/

    @Provides
    @Singleton
    fun providePushRepository(
        localTokenDataSource: TokenLocalDataSource,
        localPushDataSource: NotificationLocalDataSource
    ): PushRepository {
        return PushRepository(localTokenDataSource, localPushDataSource)
    }
}