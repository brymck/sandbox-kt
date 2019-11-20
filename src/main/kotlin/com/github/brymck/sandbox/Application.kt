package com.github.brymck.sandbox

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.brymck.sandbox.subcommands.Coroutines

class Application : CliktCommand() {
    override fun run() {}
}

fun main(args: Array<String>) {
    Application().subcommands(Coroutines()).main(args)
}
