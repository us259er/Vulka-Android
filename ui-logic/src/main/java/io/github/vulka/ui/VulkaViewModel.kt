package io.github.vulka.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.vulka.database.CredentialsDao
import io.github.vulka.database.GradesDao
import io.github.vulka.database.LuckyNumberDao
import io.github.vulka.database.Repository
import javax.inject.Inject

@HiltViewModel
class VulkaViewModel @Inject constructor(
    val credentialRepository: CredentialsDao,
    val luckyNumberRepository: LuckyNumberDao,
    val gradesRepository: GradesDao
) : ViewModel()
