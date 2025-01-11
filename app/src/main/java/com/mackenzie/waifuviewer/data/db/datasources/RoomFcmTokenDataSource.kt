package com.mackenzie.waifuviewer.data.db.datasources

import com.mackenzie.waifuviewer.data.datasource.TokenLocalDataSource
import com.mackenzie.waifuviewer.data.db.FcmTokenDb
import com.mackenzie.waifuviewer.data.db.dao.WaifuFcmTokenDao
import com.mackenzie.waifuviewer.data.tryCall
import com.mackenzie.waifuviewer.domain.Error
import com.mackenzie.waifuviewer.domain.FcmToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomFcmTokenDataSource @Inject constructor(private val dao: WaifuFcmTokenDao) : TokenLocalDataSource {

    override val token: Flow<FcmToken> = dao.getFcmToken().map { it.toDomainModel() }

    override suspend fun isTokenEmpty(): Boolean = dao.tokenCount() == 0

    override suspend fun saveToken(token: FcmToken): Error? = tryCall {
        dao.insertToken(token.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

    override suspend fun saveAllToken(tokens: List<FcmToken>): Error? = tryCall {
        dao.insertAllTokens(tokens.fromDomainModel())
    }.fold(ifLeft = { it }, ifRight = { null })

}

private fun FcmTokenDb.toDomainModel(): FcmToken =
    FcmToken(
        id,
        token,
        validUtil
    )

private fun List<FcmToken>.fromDomainModel(): List<FcmTokenDb> = map { it.fromDomainModel() }

private fun FcmToken.fromDomainModel(): FcmTokenDb =
    FcmTokenDb(
        id,
        token,
        validUtil
    )