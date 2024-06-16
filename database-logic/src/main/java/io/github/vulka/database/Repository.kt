package io.github.vulka.database

import android.content.Context

interface RepositoryInterface {
    val credentials: CredentialsDao
    val luckyNumber: LuckyNumberDao
}

class Repository(context: Context) : RepositoryInterface {
    private val database = DatabaseProvider.getInstance(context)

    override val credentials = database.credentialsDao()
    override val luckyNumber = database.luckyNumberDao()
}
