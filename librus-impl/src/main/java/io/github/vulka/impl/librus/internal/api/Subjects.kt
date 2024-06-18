package io.github.vulka.impl.librus.internal.api

import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.impl.librus.internal.api.types.SubjectsResponse

internal suspend fun LibrusUserClient.internalRequestSubjects() =
    apiGET<SubjectsResponse>("Subjects").subjects
