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

/**
 * The [Coroutines] subcommand provides a proof of concept of how coroutines can be used to perform IO-intensive
 * operations that would normally block a thread.
 */
class Coroutines : CliktCommand() {
    companion object {
        private const val BATCH_SIZE = 5
        private const val BATCH_PROCESSOR_COUNT = 3
        private const val ITEM_COUNT = 13
    }

    /** Runs the coroutines demo. This is the entrypoint to the subcommand when invoked from the CLI. */
    @ExperimentalCoroutinesApi
    override fun run() {
        runBlocking {
            val items = produceItems(ITEM_COUNT)
            val batcher = dispatchItemsInBatch(items, BATCH_SIZE)
            repeat(BATCH_PROCESSOR_COUNT) { i ->
                launchBatchProcessor(i, batcher)
            }
            println("main: done in coroutine scope")
        }
        println("main: done")
    }

    /**
     * Runs a blocking function to demonstrate how we can dispatch them with [Dispatchers.IO] to a shared pool of
     * threads designed for offloading of blocking operations.
     */
    private fun blockingSleep() {
        Thread.sleep(1000L)
        println("blockingSleep: done")
    }

    /** Produces numbers from 0 to [count] with an 0.05 second delay into a channel. */
    @ExperimentalCoroutinesApi
    private fun CoroutineScope.produceItems(count: Int) = produce {
        repeat(count) { i ->
            println("producer: created item $i")
            send(i)
            delay(50L)
        }
        println("producer: done")
    }

    /**
     * Given [items] in a channel, groups them into lists of the provided [batchSize] and sends them to a new channel.
     */
    @ExperimentalCoroutinesApi
    private fun <T> CoroutineScope.dispatchItemsInBatch(
        items: ReceiveChannel<T>,
        batchSize: Int
    ): ReceiveChannel<List<T>> = produce {
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

    /**
     * Launches a coroutine with ID [id] that will process a batch of items from a [channel]. This simulates processing
     * via a blocking function by calling [blockingSleep].
     */
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
