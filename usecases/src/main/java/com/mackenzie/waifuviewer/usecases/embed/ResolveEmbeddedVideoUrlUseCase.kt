package com.mackenzie.waifuviewer.usecases.embed

import arrow.core.Either
import com.mackenzie.waifuviewer.data.embed.EmbeddedVideoResolveError
import com.mackenzie.waifuviewer.domain.video.embed.EmbeddedVideoResolveResult
import com.mackenzie.waifuviewer.data.datasource.EmbeddedVideoResolver
import javax.inject.Inject

class ResolveEmbeddedVideoUrlUseCase @Inject constructor(
    private val resolver: EmbeddedVideoResolver
) {
    suspend operator fun invoke(embedUrl: String): Either<EmbeddedVideoResolveError, EmbeddedVideoResolveResult> {
        return resolver.resolve(embedUrl)
    }
}
