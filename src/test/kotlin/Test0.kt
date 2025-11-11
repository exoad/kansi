import net.exoad.kansi.kansi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Test0 {
    @Test
    fun test() {
        print("<bg:black fg:yellow>Hello World".kansi)
    }

    @Test
    fun testForegroundColor() {
        val result = "<fg:red>Hello</fg:red>".kansi.toString()
        println("Actual: '$result'")
        assertEquals("\u001B[0m\u001B[31mHello\u001B[0m", result)
    }

    @Test
    fun testBackgroundColor() {
        val result = "<bg:blue>World</bg:blue>".kansi.toString()
        assertEquals("\u001B[0m\u001B[44mWorld", result)
    }

    @Test
    fun testMultipleColors() {
        val result = "<fg:yellow bg:black>Test</fg:yellow bg:black>".kansi.toString()
        assertEquals("\u001B[0m\u001B[40;33mTest", result)
    }

    @Test
    fun testBoldStyle() {
        val result = "<bold>Bold Text</bold>".kansi.toString()
        assertEquals("\u001B[0m\u001B[1mBold Text", result)
    }

    @Test
    fun testNestedTags() {
        val result = "<bold><fg:red>Nested</fg:red></bold>".kansi.toString()
        assertEquals("\u001B[0m\u001B[1m\u001B[0m\u001B[31mNested\u001B[0m\u001B[1m", result)
    }

    @Test
    fun testMultipleTagsInOne() {
        val result = "<fg:green bg:white bold>Styled</fg:green bg:white bold>".kansi.toString()
        assertEquals("\u001B[0m\u001B[47;32;1mStyled", result)
    }

    @Test
    fun testNoClosingTag() {
        val result = "<fg:cyan>Open".kansi.toString()
        assertEquals("\u001B[0m\u001B[36mOpen", result)
    }

    @Test
    fun testEmptyString() {
        val result = "".kansi.toString()
        assertEquals("", result)
    }

    @Test
    fun testNoTags() {
        val result = "Plain text".kansi.toString()
        assertEquals("Plain text", result)
    }

    @Test
    fun testRawContent() {
        val ks = "<fg:red>Hello</fg:red>".kansi
        assertEquals("<fg:red>Hello</fg:red>", ks.rawContent)
        assertEquals(21, ks.length)
        assertEquals('<', ks[0])
    }

    @Test
    fun testSubSequence() {
        val ks = "Hello World".kansi
        val sub = ks.subSequence(6, 11)
        assertEquals("World", sub.toString())
    }

    @Test
    fun testMalformedTag() {
        assertFailsWith<IllegalArgumentException> {
            "<fg:red>Hello<".kansi
        }
    }

    @Test
    fun testDuplicateTagTypes() {
        assertFailsWith<IllegalArgumentException> {
            "<fg:red fg:blue>Hello</fg:red fg:blue>".kansi
        }
    }

    @Test
    fun testBrightColors() {
        val result = "<fg:bright-yellow>Bright</fg:bright-yellow>".kansi.toString()
        assertEquals("\u001B[0m\u001B[93mBright", result)
    }

    @Test
    fun testUnderline() {
        val result = "<underline>Underlined</underline>".kansi.toString()
        assertEquals("\u001B[0m\u001B[4mUnderlined", result)
    }
}