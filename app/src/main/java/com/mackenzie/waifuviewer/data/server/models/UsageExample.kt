package com.mackenzie.waifuviewer.data.server.models

/**
 * Ejemplo de uso del VideoHubService
 */

/*
// En un ViewModel o UseCase:

class VideoHubViewModel @Inject constructor(
    private val remoteVideoHubConnect: RemoteVideoHubConnect
) : ViewModel() {

    // Buscar videos
    fun searchVideos(query: String) {
        viewModelScope.launch {
            try {
                val response = remoteVideoHubConnect.videoHubService.searchVideos(
                    search = query,
                    ordering = "newest",
                    page = 1,
                    thumbsize = "medium"
                )

                // response.videos contiene la lista de videos
                response.videos.forEach { videoWrapper ->
                    val video = videoWrapper.video
                    Log.d("Video", "${video.title} - ${video.url}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Error searching videos", e)
            }
        }
    }

    // Obtener categorías
    fun loadCategories() {
        viewModelScope.launch {
            try {
                val response = remoteVideoHubConnect.videoHubService.getCategories()

                // response.categories contiene la lista de categorías
                response.categories.forEach { categoryWrapper ->
                    Log.d("Category", categoryWrapper.category)
                }
            } catch (e: Exception) {
                Log.e("Error", "Error loading categories", e)
            }
        }
    }

    // Obtener tags
    fun loadTags() {
        viewModelScope.launch {
            try {
                val response = remoteVideoHubConnect.videoHubService.getTags()

                // response.tags contiene la lista de tags
                response.tags.forEach { tagItem ->
                    Log.d("Tag", tagItem.tag.tagName)
                }
            } catch (e: Exception) {
                Log.e("Error", "Error loading tags", e)
            }
        }
    }

    // Obtener pornstars detalladas
    fun loadStarsDetailed() {
        viewModelScope.launch {
            try {
                val response = remoteVideoHubConnect.videoHubService.getStarDetailedList()

                // response.stars contiene la lista de pornstars con info detallada
                response.stars.forEach { starItem ->
                    val star = starItem.star
                    Log.d("Star", "${star.starName} - Videos: ${star.videosCountAll}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Error loading stars", e)
            }
        }
    }

    // Obtener información de un video específico
    fun loadVideoDetails(videoId: Int) {
        viewModelScope.launch {
            try {
                val response = remoteVideoHubConnect.videoHubService.getVideoById(
                    videoid = videoId,
                    thumbsize = "all"
                )

                val video = response.video
                Log.d("VideoDetails", """
                    Title: ${video.title}
                    Duration: ${video.duration}
                    Views: ${video.views}
                    Rating: ${video.rating}
                    URL: ${video.url}
                    Embed URL: ${video.embedUrl}
                """.trimIndent())

                // Thumbnails disponibles
                video.thumbs?.forEach { thumb ->
                    Log.d("Thumb", "${thumb.size}: ${thumb.src}")
                }

                // Tags del video
                video.tags?.forEach { tag ->
                    Log.d("Tag", tag.tagName)
                }

                // Pornstars en el video
                video.stars?.forEach { starWrapper ->
                    Log.d("Star", starWrapper.star.starName)
                }
            } catch (e: Exception) {
                Log.e("Error", "Error loading video details", e)
            }
        }
    }

    // Verificar si un video está activo
    fun checkVideoActive(videoId: Int) {
        viewModelScope.launch {
            try {
                val response = remoteVideoHubConnect.videoHubService.isVideoActive(
                    videoid = videoId
                )

                val isActive = response.active.active == "1"
                Log.d("VideoActive", "Video $videoId is active: $isActive")
            } catch (e: Exception) {
                Log.e("Error", "Error checking video status", e)
            }
        }
    }

    // Buscar videos por categoría
    fun searchByCategory(category: String) {
        viewModelScope.launch {
            try {
                val response = remoteVideoHubConnect.videoHubService.searchVideos(
                    category = category,
                    ordering = "mostviewed",
                    page = 1
                )

                Log.d("Search", "Found ${response.count} videos in category $category")
            } catch (e: Exception) {
                Log.e("Error", "Error searching by category", e)
            }
        }
    }

    // Buscar videos por tags
    fun searchByTags(tags: List<String>) {
        viewModelScope.launch {
            try {
                val response = remoteVideoHubConnect.videoHubService.searchVideos(
                    tags = tags,
                    ordering = "rating",
                    period = "weekly"
                )

                Log.d("Search", "Found ${response.count} videos with tags: ${tags.joinToString()}")
            } catch (e: Exception) {
                Log.e("Error", "Error searching by tags", e)
            }
        }
    }

    // Buscar videos por pornstar
    fun searchByStars(stars: List<String>) {
        viewModelScope.launch {
            try {
                val response = remoteVideoHubConnect.videoHubService.searchVideos(
                    stars = stars,
                    ordering = "newest"
                )

                Log.d("Search", "Found ${response.count} videos with stars: ${stars.joinToString()}")
            } catch (e: Exception) {
                Log.e("Error", "Error searching by stars", e)
            }
        }
    }

    // Verificar múltiples videos eliminados
    fun checkDeletedVideos(videoIds: List<Int>) {
        viewModelScope.launch {
            try {
                val response = remoteVideoHubConnect.videoHubService.areVideosDeleted(
                    videoids = videoIds.joinToString(",")
                )

                response.deleted.videos.forEach { video ->
                    val isDeleted = video.deleted == "1"
                    Log.d("DeletedCheck", "Video ${video.videoId} deleted: $isDeleted")
                }
            } catch (e: Exception) {
                Log.e("Error", "Error checking deleted videos", e)
            }
        }
    }
}
*/

