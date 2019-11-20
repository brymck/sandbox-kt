package com.github.brymck.sandbox.helpers

import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal data class CapturedOutput(
    val stdout: String,
    val stderr: String
)

internal fun captureOutput(block: () -> Unit): CapturedOutput {
    val originalStdout = System.out
    val originalStderr = System.err
    try {
        val capturedStdout = ByteArrayOutputStream()
        val capturedStderr = ByteArrayOutputStream()
        System.setOut(PrintStream(capturedStdout))
        System.setErr(PrintStream(capturedStderr))
        block()
        return CapturedOutput(
            stdout = capturedStdout.toString().trim(),
            stderr = capturedStderr.toString().trim()
        )
    } finally {
        System.setOut(originalStdout)
        System.setErr(originalStderr)
    }
}
