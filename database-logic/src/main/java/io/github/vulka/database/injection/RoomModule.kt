package io.github.vulka.database.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.vulka.database.CredentialsDao
import io.github.vulka.database.Repository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun providesRepository(
        @ApplicationContext context: Context
    ): Repository {
        return Repository(context)
    }

    @Singleton
    @Provides
    fun provideCredentialRepository(repository: Repository): CredentialsDao {
        return repository.credentials
    }
}
