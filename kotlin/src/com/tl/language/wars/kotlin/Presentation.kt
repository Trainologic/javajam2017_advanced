package com.tl.language.wars.kotlin

fun main(args: Array<String>) {

    val x = 123
    var f = 123
//    x = f //Doesn't work
    f = x
    val n1: Int? = null
    val n2: Int? = 2
    if (n1 != null && n2 != null) n1 + n2 else -1
//    n1 + n2
    val fi = five()

    fun <A> Int.repeat(action: ()->A) {
        for (i in 1..this) { action()}
    }

    5.repeat{ println("I like to extend")}
}

class Foo(val name: String)

fun returnNull(x: Int): String? = null
fun returnNullFoo(x: Int): Foo? = Foo("aaa")

fun five() = 5

