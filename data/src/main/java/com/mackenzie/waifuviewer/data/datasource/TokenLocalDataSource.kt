package com.mackenzie.waifuviewer.data.datasource

import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FcmToken
import kotlinx.coroutines.flow.Flow

interface TokenLocalDataSource {
    val token: Flow<FcmToken>
    suspend fun isTokenEmpty(): Boolean
    suspend fun saveToken(token: FcmToken): Error?
    suspend fun saveAllToken(tokens: List<FcmToken>): Error?
}