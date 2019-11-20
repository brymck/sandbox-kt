package com.github.brymck.sandbox.subcommands

import com.github.brymck.sandbox.helpers.captureOutput
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class CoroutinesTest {
    @Test
    fun `coroutines demo output follows expectations around order of operations`() {
        val captured = captureOutput {
            Coroutines().run()
        }
        assertThat(captured.stdout)
            .isEqualTo("""
                main: done in coroutine scope
                producer: created item 0
                processor #0: available
                processor #1: available
                processor #2: available
                producer: created item 1
                producer: created item 2
                producer: created item 3
                producer: created item 4
                batcher: sending 5 items: [0, 1, 2, 3, 4]
                processor #0: received 5 items: [0, 1, 2, 3, 4]
                processor #0: available
                producer: created item 5
                producer: created item 6
                producer: created item 7
                producer: created item 8
                producer: created item 9
                batcher: sending 5 items: [5, 6, 7, 8, 9]
                processor #1: received 5 items: [5, 6, 7, 8, 9]
                processor #1: available
                producer: created item 10
                producer: created item 11
                producer: created item 12
                producer: done
                batcher: done
                processor #2: received 3 items: [10, 11, 12]
                processor #2: available
                processor #2: done
                processor #1: done
                processor #0: done
                blockingSleep: done
                blockingSleep: done
                blockingSleep: done
                main: done
            """.trimIndent())
    }
}
