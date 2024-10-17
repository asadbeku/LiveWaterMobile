package uz.prestige.livewater.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.prestige.livewater.dayver.network.ApiServiceDayver
import uz.prestige.livewater.dayver.network.NetworkDayver
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkDayverModule {

    @Provides
    @Singleton
    fun providerDayverApiService(): ApiServiceDayver {
        return NetworkDayver.buildService(ApiServiceDayver::class.java)
    }

}