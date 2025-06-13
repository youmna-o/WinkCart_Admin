package com.example.winkcart_admin.application.dependencyInjection

import com.example.winkcart_admin.data.remote.RemoteDataSource
import com.example.winkcart_admin.data.remote.RemoteDataSourceImpl
import com.example.winkcart_admin.data.remote.retrofit.AdminServices
import com.example.winkcart_admin.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_admin.data.repository.ProductRepo
import com.example.winkcart_admin.data.repository.ProductRepoImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DependencyInjectionModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    @Module
    @InstallIn(SingletonComponent::class)
    object DependencyInjectionModule {

        @Provides
        @Singleton
        fun provideAdminServices(): AdminServices {
            return RetrofitHelper.productService

        }

        @Provides
        fun provideRemoteDataSource(
            adminServices: AdminServices
        ): RemoteDataSource = RemoteDataSourceImpl(adminServices)

        @Provides
        fun provideProductRepo(
            remoteDataSource: RemoteDataSource
        ): ProductRepo = ProductRepoImpl(remoteDataSource)
    }

}
