package com.github.brymck.sandbox

import com.github.brymck.sandbox.helpers.CheckExitCalled
import com.github.brymck.sandbox.helpers.captureOutput
import com.github.brymck.sandbox.helpers.preventSystemExit
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test

internal class ApplicationTest {
    @Test
    fun `running the CLI with no subcommands does nothing`() {
        val application = Application()
        Double.NaN
        assertThatCode { application.main(emptyArray()) }
            .doesNotThrowAnyException()
    }

    @Test
    fun `running the main entrypoint with no arguments produces help and exits`() {
        val captured = captureOutput {
            assertThatExceptionOfType(CheckExitCalled::class.java)
                .isThrownBy {
                    preventSystemExit {
                        main(emptyArray())
                    }
                }
                .withMessage("Tried to exit with status 0.")
        }
        assertThat(captured.stdout).startsWith("Usage:")
    }
}
