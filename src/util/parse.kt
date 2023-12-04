package util

fun String.retrieveNumbers(): List<Int> {
    return "[0-9]+".toRegex()
        .findAll(this)
        .map { it.value.toInt() }
        .toList()
}
