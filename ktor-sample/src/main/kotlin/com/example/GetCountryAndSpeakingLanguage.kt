package com.example

fun main() {
    map.groupBy {
        it.first
    }.forEach {
        println(it)
    }

    println()
    println()
    println()

    map.groupBy {
        it.first
    }.map {
        Pair(
            it.key,
            it.value.sumOf { pair ->
                pair.second
            } + it.value.count()
        )
    }.sortedByDescending {
        it.second
    }.take(2)
        .forEach {
            println(it)
        }

    println()
    println()
    println()

    map.groupBy(
        keySelector = { it.first },
        valueTransform = { it.second }
    ).map { (key, values) ->
        key to (values.sum() + values.size)
    }.sortedBy {
        it.second
    }
}


val map = listOf(
    Pair(
        first = 732,
        second = 10
    ), Pair(
        first = 2743,
        second = 0
    ), Pair(
        first = 6743,
        second = 1
    ), Pair(
        first = 4743,
        second = 2
    ), Pair(
        first = 9743,
        second = 0
    ), Pair(
        first = 732,
        second = 0
    ), Pair(
        first = 7372,
        second = 0
    ), Pair(
        first = 6743,
        second = 2
    ), Pair(
        first = 4743,
        second = 1
    ), Pair(
        first = 9743,
        second = 0
    ), Pair(
        first = 4743,
        second = 5
    ), Pair(
        first = 9743,
        second = 0
    )
)