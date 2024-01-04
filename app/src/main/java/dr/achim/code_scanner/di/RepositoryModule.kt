package dr.achim.code_scanner.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dr.achim.code_scanner.data.repo.MainRepoImpl
import dr.achim.code_scanner.domain.repo.MainRepo

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindMainRepo(mainRepoImpl: MainRepoImpl): MainRepo
}