package com.mackenzie.waifuviewer.usecases

import com.mackenzie.waifuviewer.data.RegionRepository
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(private val repo: RegionRepository) {
    suspend operator fun invoke() = repo.findLastRegion()
}