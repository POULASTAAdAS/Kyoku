package com.example

fun main() {
    // Sample list
    val myList = mutableListOf(1, 2, 3, 4, 5)

    // Value to add after
    val valueToAddAfter = 3

    // List of numbers to add after the value
    val numbersToAdd = listOf(6, 7, 8)

    // Find the index of the value
    val index = myList.indexOf(valueToAddAfter)

    // Check if the value is present in the list
    if (index != -1) {
        // Add the list of numbers after the found index
        myList.addAll(index + 1, numbersToAdd)
        println("Updated list: $myList")
    } else {
        println("$valueToAddAfter not found in the list.")
    }
}
