package com.github.brymck.sandbox

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.brymck.sandbox.subcommands.Coroutines

class Application : CliktCommand() {
    private val count: Int by option(help = "Number of greetings").int().default(1)

    override fun run() {
        for (i in 1..count) {
            echo("Hello Bryan!")
        }
    }
}

fun main(args: Array<String>) {
    Application().subcommands(Coroutines()).main(args)
}
