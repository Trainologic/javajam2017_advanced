package com.tl.language.wars.kotlin

fun oldSchool(name: String): Int {
    return name.length;
}

fun f1(name: String) = name.length
fun calcPrinter(name: String, calc: (String) -> Int = { it.length }) {
    println("calculated $name as ${calc(name)}")
}

fun unitFun() = println("unit fun!!")
fun main(args: Array<String>) {
    calcPrinter("alex") {
        it.length + 1
    }
    val u: Unit = unitFun()
}