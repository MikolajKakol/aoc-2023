package util

fun String.retrieveNumbers(): List<Int> {
    return "[0-9]+".toRegex()
        .findAll(this)
        .map { it.value.toInt() }
        .toList()
}

fun String.retrieveLongNumbers(): List<Long> {
    return "[0-9]+".toRegex()
        .findAll(this)
        .map { it.value.toLong() }
        .toList()
}
