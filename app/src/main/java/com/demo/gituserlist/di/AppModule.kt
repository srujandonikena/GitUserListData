package com.demo.gituserlist.di

import android.content.Context
import com.demo.gituserlist.data.api.ApiService
import com.demo.gituserlist.MyApplication
import com.demo.gituserlist.data.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAuthApi(
        remoteDataSource: RemoteDataSource,
        @ApplicationContext context: Context
    ): ApiService {
        return remoteDataSource.buildApi(ApiService::class.java, context)
    }

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): MyApplication {
        return app as MyApplication
    }
}