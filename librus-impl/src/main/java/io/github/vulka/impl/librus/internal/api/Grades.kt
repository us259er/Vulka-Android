package io.github.vulka.impl.librus.internal.api

import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.impl.librus.internal.api.types.GradesCategoriesResponse
import io.github.vulka.impl.librus.internal.api.types.GradesResponse

internal suspend fun LibrusUserClient.internalRequestGrades() =
    apiGET<GradesResponse>("Grades").grades

internal suspend fun LibrusUserClient.internalRequestGradesCategories() =
    apiGET<GradesCategoriesResponse>("Grades/Categories").categories
