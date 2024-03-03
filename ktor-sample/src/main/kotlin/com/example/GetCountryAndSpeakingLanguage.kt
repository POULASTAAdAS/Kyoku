package com.example

fun main() {
    val a = data.flatten()
        .distinctBy {
            it.id
        }.filterNot { new ->
            old.any {
                it.name == new.name
            }
        }

    val new = a + old

    println(new)
}

val data
    get() =
        listOf(
            sub,
            sub,
            sub,
            sub,
        )


val sub = listOf(
    Cl(
        1,
        "name"
    ), Cl(
        2,
        "name2"
    ), Cl(
        3,
        "name1"
    ), Cl(
        4,
        "name"
    ), Cl(
        5,
        "name"
    ), Cl(
        6,
        "name1"
    )
)

val old = listOf(
    Cl(
        1,
        "name"
    ), Cl(
        2,
        "name2"
    )
)

data class Cl(
    val id: Int,
    val name: String
)