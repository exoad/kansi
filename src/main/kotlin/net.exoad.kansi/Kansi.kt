package net.exoad.kansi

 /**
 * Initializes a kansi string instance which will convert [this] to the ANSI delimited character sequence.
 *
 * Note that the usage of [CharSequence.get], [CharSequence.subSequence] functions and dependent will result in
  * the original unmodified string being used, not the ANSI converted one.
  *
  * Example:
  * ```
  * val kansiString = "<fg:red>Hello World</fg:red>".kansi
  * print(kansiString) // Prints "Hello World" in red color
  * ```
  * @return A [KansiString] instance representing the ANSI converted string.
  * @see KansiString
  * @see ANSIDefinitions
 */
val String.kansi get(): KansiString = KansiString(this, this.length)

class KansiString(private val src: String, override val length: Int) : CharSequence {
    private val builtContent: String

    init {
        val stack = ArrayDeque<MutableSet<String>>()
        val sb = StringBuilder()
        fun applyCurrentStyles() {
            sb.append(ANSIDefinitions.getResetSequence())
            val allActiveTags = stack.flatten().toSet()
            if (allActiveTags.isEmpty()) {
                return
            }
            val ansiCodes = allActiveTags.mapNotNull { tagName ->
                ANSIDefinitions.codes[tagName]
            }
            if (ansiCodes.isNotEmpty()) {
                sb.append(ANSIDefinitions.buildEscapeSequence(ansiCodes))
            }
        }
        stack.add(mutableSetOf())
        var currentIndex = 0
        while (currentIndex < src.length) {
            val tagStartIndex = src.indexOf('<', currentIndex)
            if (tagStartIndex == -1) {
                val remainingText = src.substring(currentIndex)
                sb.append(remainingText)
                break
            }
            val plainText = src.substring(currentIndex, tagStartIndex)
            if (plainText.isNotEmpty()) {
                sb.append(plainText)
            }
            val tagEndIndex = src.indexOf('>', tagStartIndex + 1)
            if (tagEndIndex == -1) {
                throw IllegalArgumentException("Kansi Parse Error: Malformed tag. Found '<' at $tagStartIndex but no closing '>'.")
            }
            if (src[tagStartIndex + 1] == '/') {
                val content = src.substring(tagStartIndex + 2, tagEndIndex)
                val tagsToClose = content.trim().split(Regex("\\s+"))
                    .filter { it.isNotEmpty() }
                    .toSet()
                if (tagsToClose.isEmpty() && stack.size > 1) {
                    stack.removeLast()
                } else {
                    val tagsToRemove = tagsToClose.toMutableSet()
                    while (tagsToRemove.isNotEmpty() && stack.size > 1) {
                        val lastPoppedTags = stack.removeLast()
                        tagsToRemove.removeAll(lastPoppedTags)
                    }
                }
                applyCurrentStyles()
            } else {
                val content = src.substring(tagStartIndex + 1, tagEndIndex)
                val tags = content.trim().split(Regex("\\s+"))
                    .filter { it.isNotEmpty() }
                    .toSet()
                if (tags.mapNotNull {
                        it.substringBefore(':', "").ifEmpty { null }
                    }.groupingBy { it }.eachCount().any { it.value > 1 }
                ) {
                    throw IllegalArgumentException(
                        "Kansi Parse Error: Duplicate tag types in <$content>. Found: ${
                            tags.mapNotNull {
                                val prefix = it.substringBefore(':', "")
                                prefix.ifEmpty { null }
                            }.groupingBy { it }.eachCount()
                        }"
                    )
                }
                stack.addLast(tags as? MutableSet ?: tags.toMutableSet())
                applyCurrentStyles()
            }
            currentIndex = tagEndIndex + 1
        }
        builtContent = sb.toString()
    }

    val rawContent get(): String = src

    override fun toString(): String {
        return builtContent
    }

    override fun get(index: Int): Char {
        return src[index]
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return src.subSequence(startIndex, endIndex)
    }
}