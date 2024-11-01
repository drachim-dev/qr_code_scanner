package dr.achim.code_scanner.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dr.achim.code_scanner.data.database.CodeDatabase
import dr.achim.code_scanner.data.repo.CodeRepositoryImpl
import dr.achim.code_scanner.data.repo.SettingsRepositoryImpl
import dr.achim.code_scanner.data.source.ImageStreamSource
import dr.achim.code_scanner.data.source.SettingsSource
import dr.achim.code_scanner.domain.repo.CodeRepository
import dr.achim.code_scanner.domain.repo.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSettingsRepository(
        settingsSource: SettingsSource,
    ): SettingsRepository {
        return SettingsRepositoryImpl(settingsSource)
    }

    @Provides
    @Singleton
    fun provideCodeRepository(
        imageStreamSource: ImageStreamSource,
        database: CodeDatabase
    ): CodeRepository {
        return CodeRepositoryImpl(imageStreamSource, database.dao)
    }
}