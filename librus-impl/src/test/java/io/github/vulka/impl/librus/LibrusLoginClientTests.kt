package io.github.vulka.impl.librus

import kotlinx.coroutines.test.runTest
import org.junit.Test

class LibrusLoginClientTests {
    @Test
    fun login() = runTest {
        LibrusLoginClient().login(LOGIN_DATA)
    }
}
