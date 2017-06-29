package com.tl.language.wars.kotlin

class JavaCode {
    fun toJSON(collection: Collection<Int>): String = "[${collection.joinToString(transform = Int::toString)}]"
}