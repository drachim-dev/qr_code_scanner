package dr.achim.code_scanner.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dr.achim.code_scanner.data.source.ImageStreamSource
import dr.achim.code_scanner.data.source.ImageStreamSourceImpl
import dr.achim.code_scanner.data.source.SettingsSource
import dr.achim.code_scanner.data.source.SettingsSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule {

    @Singleton
    @Binds
    abstract fun bindImageStreamSource(imageStreamSource: ImageStreamSourceImpl): ImageStreamSource

    @Singleton
    @Binds
    abstract fun bindSettingsSource(settingsSource: SettingsSourceImpl): SettingsSource
}