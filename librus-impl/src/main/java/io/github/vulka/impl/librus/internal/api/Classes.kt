package io.github.vulka.impl.librus.internal.api

import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.impl.librus.internal.api.types.ClassResponse

internal suspend fun LibrusUserClient.internalRequestClass(id: Int) =
    apiGET<ClassResponse>("Classes/$id").class_
