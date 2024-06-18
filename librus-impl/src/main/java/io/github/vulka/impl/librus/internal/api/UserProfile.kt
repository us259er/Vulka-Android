package io.github.vulka.impl.librus.internal.api

import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.impl.librus.internal.api.types.UserProfileResponse

internal suspend fun LibrusUserClient.internalRequestUserProfile() =
    apiGET<UserProfileResponse>("UserProfile").userProfile
