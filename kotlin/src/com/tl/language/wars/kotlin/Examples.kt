package com.tl.language.wars.kotlin

data class Person(val name: String, val age: Int)

fun main(args: Array<String>) {
    val person = Person("Foobar", 33)
    val jPerson = JavaPerson("Foobar",33)
//    when (person) {
//        is ("Foobar",33)
//    }
    fun sum(n1: Int, n2: Int) = n1 + n2
    val n = lazy { sum(2+2,3+3) }
}