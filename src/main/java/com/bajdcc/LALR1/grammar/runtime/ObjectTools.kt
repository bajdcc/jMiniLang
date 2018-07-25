package com.bajdcc.LALR1.grammar.runtime

import java.io.*

object ObjectTools {
    @Suppress("UNCHECKED_CAST")
    fun <T> deserialize(input: InputStream): T? {
        try {
            val objectInputStream = ObjectInputStream(input)
            return objectInputStream.readObject()!! as T
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    fun serialize(any: Any, output: OutputStream) {
        try {
            val out = ObjectOutputStream(output)
            out.writeObject(any)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
