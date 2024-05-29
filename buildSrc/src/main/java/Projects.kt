import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

class Projects(dh: DependencyHandler) {
    val uiLogic = dh.project(":ui-logic")
}

val DependencyHandler.projects: Projects
    get() = Projects(this)
