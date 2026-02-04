package io.ljp.simapi.util

inline fun <K, V> simApiMapOf(): Map<K, V> = mapOf()

inline fun <K,V> simApiMapOf(vararg pairs: Pair<K, V>) = listOf(*pairs)
    .mapNotNull { (k, v) -> v?.let { k to it } }
    .toMap()
