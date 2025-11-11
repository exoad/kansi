package net.exoad.kansi

object ANSIDefinitions {
    const val CSI = "\u001B["

    val codes = mapOf(
        "reset" to 0,
        "bold" to 1,
        "dim" to 2,
        "italic" to 3,
        "underline" to 4,
        "blink" to 5,
        "reverse" to 7,
        "hidden" to 8,
        "fg:black" to 30,
        "fg:red" to 31,
        "fg:green" to 32,
        "fg:yellow" to 33,
        "fg:blue" to 34,
        "fg:magenta" to 35,
        "fg:cyan" to 36,
        "fg:white" to 37,
        "bg:black" to 40,
        "bg:red" to 41,
        "bg:green" to 42,
        "bg:yellow" to 43,
        "bg:blue" to 44,
        "bg:magenta" to 45,
        "bg:cyan" to 46,
        "bg:white" to 47,
        "fg:bright-black" to 90,
        "fg:bright-red" to 91,
        "fg:bright-green" to 92,
        "fg:bright-yellow" to 93,
        "fg:bright-blue" to 94,
        "fg:bright-magenta" to 95,
        "fg:bright-cyan" to 96,
        "fg:bright-white" to 97,
        "bg:bright-black" to 100,
        "bg:bright-red" to 101,
        "bg:bright-green" to 102,
        "bg:bright-yellow" to 103,
        "bg:bright-blue" to 104,
        "bg:bright-magenta" to 105,
        "bg:bright-cyan" to 106,
        "bg:bright-white" to 107
    )

    fun buildEscapeSequence(ansiCodes: List<Int>): String {
        if (ansiCodes.isEmpty()) {
            return ""
        }
        return ansiCodes.joinToString(separator = ";", prefix = CSI, postfix = "m")
    }

    fun getResetSequence(): String {
        return "${CSI}0m"
    }
}


