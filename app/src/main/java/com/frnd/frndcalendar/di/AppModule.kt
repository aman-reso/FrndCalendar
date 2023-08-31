package com.frnd.frndcalendar.di

import android.content.Context
import androidx.room.Room
import com.frnd.frndcalendar.remote.ApiService
import com.frnd.frndcalendar.BuildConfig
import com.frnd.frndcalendar.constant.Metadata.Companion.BASE_URL
import com.frnd.frndcalendar.constant.Metadata.Companion.BASE_URL_NO_HTTPS
import com.frnd.frndcalendar.storage.AppDatabase
import com.frnd.frndcalendar.storage.dao.TaskDao
import com.frnd.frndcalendar.storage.repository.TaskLocalRepository
import com.frnd.frndcalendar.storage.repository.TaskLocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //based upon http and https bcz sometimes http works and sometimes https
    @Provides
    fun provideBaseUrl(): String = BASE_URL_NO_HTTPS
    //for base url no https=BASE_URL_NO_HTTPS
    //for base url with https=BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providesAppContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): TaskDao = database.taskDao()


    @Provides
    fun provideTaskRepository(taskDao: TaskDao): TaskLocalRepository {
        return TaskLocalRepositoryImpl(taskDao)
    }

}