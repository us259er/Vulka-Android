package io.github.vulka.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.vulka.database.CredentialsDao
import javax.inject.Inject

@HiltViewModel
class VulkaViewModel @Inject constructor(
    val credentialRepository: CredentialsDao,
) : ViewModel()
