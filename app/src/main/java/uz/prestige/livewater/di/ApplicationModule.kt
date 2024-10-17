package uz.prestige.livewater.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import uz.prestige.livewater.no_connection.InternetConnectionListener
import uz.prestige.livewater.no_connection.view_model.NoConnectionRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideInternetConnectionListener(
        repository: NoConnectionRepositoryImpl
    ): InternetConnectionListener {
        return repository
    }

    @Provides
    @Singleton
    fun provideNoConnectionRepositoryImpl(
        applicationScope: CoroutineScope
    ): NoConnectionRepositoryImpl {
        return NoConnectionRepositoryImpl(applicationScope)
    }

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}