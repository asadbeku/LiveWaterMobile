package uz.prestige.livewater.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import uz.prestige.livewater.level.network.ApiService
import uz.prestige.livewater.level.network.DeviceTestNetwork
import uz.prestige.livewater.level.network.NetworkLevel
import uz.prestige.livewater.level.network.TestApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return NetworkLevel.buildService(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTestApiService(): TestApiService {
        return DeviceTestNetwork.buildService(TestApiService::class.java)
    }

}