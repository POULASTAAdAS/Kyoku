package com.example

fun main() {
    val map = HashMap<Int, String>()

    myList.forEach {
        if (!map.keys.contains(it.first))
            map[it.first] = it.second
    }
    val list1 = map.keys.toList()


    val list2 = myList.associateBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )
        .keys.toList()
}


val myList = listOf(
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(1, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(2, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name"),
    Pair(3, "name")
)