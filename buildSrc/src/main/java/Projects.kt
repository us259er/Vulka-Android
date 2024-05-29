import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

class Projects(dh: DependencyHandler) {
    val uiLogic = dh.project(":ui-logic")
    val databaseLogic = dh.project(":database-logic")

    val coreApi = dh.project(":core-api")
    val vulcanImpl = dh.project(":vulcan-impl")
    val librusImpl = dh.project(":librus-impl")
}

val DependencyHandler.projects: Projects
    get() = Projects(this)
