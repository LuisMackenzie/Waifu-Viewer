package com.mackenzie.waifuviewer.data.server

import com.mackenzie.waifuviewer.data.server.models.videohub.*
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoHubService {

    /**
     * Get a list of videos
     * @param data Required - Must be "redtube.Videos.searchVideos"
     * @param output Required - Response format (json or xml)
     * @param page Optional - Page number (default: 1)
     * @param thumbsize Optional - Thumbnail size (small, medium, big, all)
     * @param search Optional - Search query
     * @param tags Optional - Filter by tags (comma separated)
     * @param stars Optional - Filter by pornstar name (comma separated)
     * @param category Optional - Filter by category
     * @param ordering Optional - Sort order (newest, mostviewed, rating)
     * @param period Optional - Time period (alltime, weekly, monthly)
     */
    @GET("/")
    suspend fun searchVideos(
        @Query("data") data: String = "redtube.Videos.searchVideos",
        @Query("output") output: String = "json",
        @Query("page") page: Int? = null,
        @Query("thumbsize") thumbsize: String? = null,
        @Query("search") search: String? = null,
        @Query("tags[]") tags: List<String>? = null,
        @Query("stars[]") stars: List<String>? = null,
        @Query("category") category: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("period") period: String? = null
    ): VideoSearchResponse

    /**
     * Get a list of active video categories
     * @param data Required - Must be "redtube.Categories.getCategoriesList"
     * @param output Required - Response format (json or xml)
     */
    @GET("/")
    suspend fun getCategories(
        @Query("data") data: String = "redtube.Categories.getCategoriesList",
        @Query("output") output: String = "json"
    ): CategoriesResponse

    /**
     * Get a list of active video tags
     * @param data Required - Must be "redtube.Tags.getTagList"
     * @param output Required - Response format (json or xml)
     */
    @GET("/")
    suspend fun getTags(
        @Query("data") data: String = "redtube.Tags.getTagList",
        @Query("output") output: String = "json"
    ): TagsResponse

    /**
     * Get pornstar list
     * @param data Required - Must be "redtube.Stars.getStarList"
     * @param output Required - Response format (json or xml)
     */
    @GET("/")
    suspend fun getStars(
        @Query("data") data: String = "redtube.Stars.getStarList",
        @Query("output") output: String = "json"
    ): StarsResponse

    /**
     * Get detailed pornstar list with extra information
     * @param data Required - Must be "redtube.Stars.getStarDetailedList"
     * @param output Required - Response format (json or xml)
     */
    @GET("/")
    suspend fun getStarDetailedList(
        @Query("data") data: String = "redtube.Stars.getStarDetailedList",
        @Query("output") output: String = "json"
    ): StarsDetailedResponse

    /**
     * Check if a video is active/deleted
     * @param data Required - Must be "redtube.Videos.isVideoActive"
     * @param output Required - Response format (json or xml)
     * @param videoid Required - Video ID to check
     */
    @GET("/")
    suspend fun isVideoActive(
        @Query("data") data: String = "redtube.Videos.isVideoActive",
        @Query("output") output: String = "json",
        @Query("videoid") videoid: Int
    ): VideoActiveResponse

    /**
     * Get video information by ID
     * @param data Required - Must be "redtube.Videos.getVideoById"
     * @param output Required - Response format (json or xml)
     * @param videoid Required - Video ID
     * @param thumbsize Optional - Thumbnail size (small, medium, big, all)
     */
    @GET("/")
    suspend fun getVideoById(
        @Query("data") data: String = "redtube.Videos.getVideoById",
        @Query("output") output: String = "json",
        @Query("videoid") videoid: Int,
        @Query("thumbsize") thumbsize: String? = null
    ): VideoByIdResponse

    /**
     * Get video embed code
     * @param data Required - Must be "redtube.Videos.getVideoEmbedCode"
     * @param output Required - Response format (json or xml)
     * @param videoid Required - Video ID
     */
    @GET("/")
    suspend fun getVideoEmbedCode(
        @Query("data") data: String = "redtube.Videos.getVideoEmbedCode",
        @Query("output") output: String = "json",
        @Query("videoid") videoid: Int
    ): VideoEmbedCodeResponse

    /**
     * Check if videos are deleted (bulk check)
     * @param data Required - Must be "redtube.Videos.areVideosDeleted"
     * @param output Required - Response format (json or xml)
     * @param videoids Required - Comma-separated video IDs
     */
    @GET("/")
    suspend fun areVideosDeleted(
        @Query("data") data: String = "redtube.Videos.areVideosDeleted",
        @Query("output") output: String = "json",
        @Query("videoids") videoids: String
    ): VideosDeletedResponse
}