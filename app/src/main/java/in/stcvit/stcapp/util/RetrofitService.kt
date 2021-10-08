package `in`.stcvit.stcapp.util

import `in`.stcvit.stcapp.model.Feed
import `in`.stcvit.stcapp.model.ProjectIdea
import `in`.stcvit.stcapp.model.explore.BoardMember
import `in`.stcvit.stcapp.model.explore.Event
import `in`.stcvit.stcapp.model.explore.Project
import `in`.stcvit.stcapp.model.resource.Detail
import `in`.stcvit.stcapp.model.resource.Resource
import `in`.stcvit.stcapp.model.resource.Roadmap
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {

    @GET("project")
    suspend fun getProjects(): Response<List<Project>>

    @GET("event")
    suspend fun getEvents(): Response<List<Event>>

    @GET("feed")
    suspend fun getFeed(@Query("skip") skip: Int): List<Feed>

    @GET("board")
    suspend fun getBoard(): Response<List<BoardMember>>

    @GET("details/{domain}")
    suspend fun getDetails(@Path("domain") domain: String): Response<Detail>

    @GET("roadmap/{domain}")
    suspend fun getRoadmap(@Path("domain") domain: String): Response<Roadmap>

    @GET("resource/{domain}")
    suspend fun getResources(@Path("domain") domain: String): Response<List<Resource>>

    @POST("idea")
    suspend fun postIdea(@Body projectIdeaModel: ProjectIdea): Response<Map<String, String>>

    companion object {
        private const val BASE_URL = "https://stcappbackendnode.azurewebsites.net/"
        fun create(): RetrofitService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService::class.java)
        }
    }
}