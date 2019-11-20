package com.github.brymck.sandbox.subcommands

import com.github.ajalt.clikt.core.CliktCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Coroutines : CliktCommand() {
    companion object {
        private const val BATCH_SIZE = 5
        private const val BATCH_PROCESSOR_COUNT = 3
        private const val ITEM_COUNT = 13
    }

    @ExperimentalCoroutinesApi
    override fun run() {
        runBlocking {
            val items = produceItems()
            val batcher = dispatchItemsInBatch(items, BATCH_SIZE)
            repeat(BATCH_PROCESSOR_COUNT) { i ->
                launchBatchProcessor(i, batcher)
            }
            println("main: done in coroutine scope")
        }
        println("main: done")
    }

    private fun blockingSleep() {
        Thread.sleep(1000L)
        println("blockingSleep: done")
    }

    @ExperimentalCoroutinesApi
    private fun CoroutineScope.produceItems() = produce {
        repeat(ITEM_COUNT) { i ->
            println("producer: created item $i")
            send(i)
            delay(50L)
        }
        println("producer: done")
    }

    @ExperimentalCoroutinesApi
    private fun <T> CoroutineScope.dispatchItemsInBatch(items: ReceiveChannel<T>, batchSize: Int): ReceiveChannel<List<T>> = produce {
        val currentBatch = mutableListOf<T>()
        for (item in items) {
            currentBatch.add(item)
            if (currentBatch.size >= batchSize) {
                println("batcher: sending ${currentBatch.size} items: $currentBatch")
                send(currentBatch.toList())
                currentBatch.clear()
            }
        }
        if (currentBatch.size > 0) {
            send(currentBatch)
        }
        println("batcher: done")
    }

    private fun <T> CoroutineScope.launchBatchProcessor(id: Int, channel: ReceiveChannel<List<T>>) = launch {
        println("processor #$id: available")
        for (items in channel) {
            println("processor #$id: received ${items.size} items: $items")
            launch(Dispatchers.IO) {
                blockingSleep()
            }
            println("processor #$id: available")
        }
        println("processor #$id: done")
    }
}
