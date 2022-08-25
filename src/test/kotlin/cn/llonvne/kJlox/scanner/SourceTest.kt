package cn.llonvne.kJlox.scanner

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals


internal class SourceTest {
    /**
     * 测试 Source 创建
     */
    @Test
    fun test_source_instantiate() {

        val sourceText = """
            package ui;
            import exec.Exec;
            import ui.UIOperations.UIOPerationsInterface;
            import ui.status.UIStatus;
            import ui.status.UIStatusController;
            public final class FormHandler implements Exec {
                private final Displayable display;
                private final UIOPerationsInterface uiOperations;

                public FormHandler(Displayable display, UIOPerationsInterface uiOperations) {
                    this.display = display;
                    this.uiOperations = uiOperations;
                }
                public void exec() {
                    UIStatusController uiStatus = uiOperations.getStatusController();
                    while (uiStatus.getStatus() != UIStatus.END) {
                        switch (uiStatus.getStatus()) {
                            case INIT -> uiOperations.init();
                            case DISPLAY -> display.display();
                            case HANDLER_USER_INPUT -> uiOperations.userInput();
                            case RECALL -> uiOperations.recall();
                        }
                        uiStatus.next_status();
                    }
                }
            }
        """.trimIndent()

        assertDoesNotThrow {
            val sour = Source(sourceText)
            assertEquals(sour.start, 0)
        }
    }

    private val sourceText = """
            package ui;
            import exec.Exec;
            import ui.UIOperations.UIOPerationsInterface;
            import ui.status.UIStatus;
            import ui.status.UIStatusController;
            public final class FormHandler implements Exec {
                private final Displayable display;
                private final UIOPerationsInterface uiOperations;

                public FormHandler(Displayable display, UIOPerationsInterface uiOperations) {
                    this.display = display;
                    this.uiOperations = uiOperations;
                }
                public void exec() {
                    UIStatusController uiStatus = uiOperations.getStatusController();
                    while (uiStatus.getStatus() != UIStatus.END) {
                        switch (uiStatus.getStatus()) {
                            case INIT -> uiOperations.init();
                            case DISPLAY -> display.display();
                            case HANDLER_USER_INPUT -> uiOperations.userInput();
                            case RECALL -> uiOperations.recall();
                        }
                        uiStatus.next_status();
                    }
                }
            }
        """.trimIndent()
    private val sour = Source(sourceText)

    @Test
    fun test_is_end() {
        for (i in 0 until sour.sourceText.length) {
            sour.current = i
            assertFalse(sour.isAtEnd())
        }
        sour.current = sour.sourceText.length
        assert(sour.isAtEnd())
    }

    @Test
    fun test_advance() {
        for (i in 0 until sour.sourceText.length - 1) {
            sour.current = i
            assertEquals(sour.advance(), sour.sourceText[i])
            assertEquals(sour.current, i + 1)
        }
        sour.current = sour.sourceText.length
        assertFails {
            sour.advance()
        }
    }

    @Test
    fun test_match() {
        for (i in 0 until sour.sourceText.length - 1) {
            sour.current = i
            assert(sour.match(sour.sourceText[i]))
            assert(i + 1 == sour.current)
        }

        sour.current = sour.sourceText.length
        assertFalse { sour.match('a') }

        for (i in 0 until sour.sourceText.length - 1) {
            sour.current = i
            assertFalse(sour.match(sour.sourceText[i] + 1))
            assert(i == sour.current)
        }
    }

    @Test
    fun test_peek_and_peekNext() {
        for (i in 0 until sour.sourceText.length - 1) {
            sour.current = i
            assertEquals(sour.peek(), sour.sourceText[i])
            assert(i == sour.current)
            assertEquals(sour.peekNext(), sour.sourceText[i + 1])
            assert(i == sour.current)

            assertNotEquals(sour.peek(), sour.sourceText[i] + 1)
            assert(i == sour.current)
            assertNotEquals(sour.peekNext(), sour.sourceText[i + 1] + 1)
            assert(i == sour.current)
        }
    }
}