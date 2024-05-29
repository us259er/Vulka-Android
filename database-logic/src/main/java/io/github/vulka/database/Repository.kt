package io.github.vulka.database

import android.content.Context

interface RepositoryInterface {
    val credentials: CredentialsDao
}

class Repository(context: Context) : RepositoryInterface {
    private val database = DatabaseProvider.getInstance(context)

    override val credentials = database.credentialsDao()
}
