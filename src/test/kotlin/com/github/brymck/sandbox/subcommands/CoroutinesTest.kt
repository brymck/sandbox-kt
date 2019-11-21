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
        val lines = captured.stdout.lines()
        println(lines)
        assertThat(lines.subList(0, 4)).containsExactlyInAnyOrder(
            "main: done in coroutine scope",
            "processor #0: available",
            "processor #1: available",
            "processor #2: available"
        )
        assertThat(lines.subList(4, 10)).containsExactly(
            "producer: created item 0",
            "producer: created item 1",
            "producer: created item 2",
            "producer: created item 3",
            "producer: created item 4",
            "batcher: sending 5 items: [0, 1, 2, 3, 4]"
        )
        assertThat(lines.subList(10, 11)).containsAnyOf(
            "processor #0: received 5 items: [0, 1, 2, 3, 4]",
            "processor #1: received 5 items: [0, 1, 2, 3, 4]",
            "processor #2: received 5 items: [0, 1, 2, 3, 4]"
        )
        assertThat(lines.subList(11, 17)).containsExactly(
            "producer: created item 5",
            "producer: created item 6",
            "producer: created item 7",
            "producer: created item 8",
            "producer: created item 9",
            "batcher: sending 5 items: [5, 6, 7, 8, 9]"
        )
        assertThat(lines.subList(17, 18)).containsAnyOf(
            "processor #0: received 5 items: [5, 6, 7, 8, 9]",
            "processor #1: received 5 items: [5, 6, 7, 8, 9]",
            "processor #2: received 5 items: [5, 6, 7, 8, 9]"
        )
        assertThat(lines.subList(18, 23)).containsExactly(
            "producer: created item 10",
            "producer: created item 11",
            "producer: created item 12",
            "producer: done",
            "batcher: done"
        )
        assertThat(lines.subList(23, 24)).containsAnyOf(
            "processor #0: received 3 items: [10, 11, 12]",
            "processor #1: received 3 items: [10, 11, 12]",
            "processor #2: received 3 items: [10, 11, 12]"
        )
        assertThat(lines.subList(24, 33)).containsExactlyInAnyOrder(
            "blockingSleep: done",
            "processor #0: available",
            "processor #0: done",
            "blockingSleep: done",
            "processor #1: available",
            "processor #1: done",
            "blockingSleep: done",
            "processor #2: available",
            "processor #2: done"
        )
        assertThat(lines[33]).isEqualTo("main: done")
    }
}
