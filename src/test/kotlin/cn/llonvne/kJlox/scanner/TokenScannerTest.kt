package cn.llonvne.kJlox.scanner

import cn.llonvne.kJlox.main.KJlox
import cn.llonvne.kJlox.token.TokenType
import cn.llonvne.kJlox.token.printableTokenMap
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class TokenScannerTest {

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


    @Test
    fun test_establish() {
        assertDoesNotThrow {
            TokenScanner(Source(sourceText))
        }
    }

    @Test
    fun test_all_printableTokens() {
        val singleOp = printableTokenMap.values.toMutableList()
        var code = ""
        singleOp.forEach {
            code += " " + it.value
        }
        singleOp.add(TokenType.EOF)
        val tokens = TokenScanner(Source(code)).scanTokens()
        assert(singleOp.size == tokens.size)
        for ((op, tk) in singleOp zip tokens) {
            assertEquals(tk.type.value, op.value)
        }
    }

    @Test
    fun test_enter() {
        val code = "\n\n\n\n\n\n"
        val tokens = TokenScanner(Source(code)).scanTokens()
        assert(tokens.size == 1)
    }

    @Test
    fun test_number() {
        val code = "123 123.123 1256.1314 0 0.1 0.12345"
        val num = code.split(" ").map { it.toDouble() }.toMutableList()
        val tokens = TokenScanner(Source(code)).scanTokens().toMutableList()
        tokens.removeLast()
        val tn = tokens zip num
        for ((tk, nu) in tn) {
            assertEquals(tk.type.value, TokenType.NUMBER.value)
            assertEquals(tk.lexeme.toDouble(), nu)
        }
    }

    @Suppress("NAME_SHADOWING")
    @Test
    fun test_strings() {
        val s1 = "djaoiwjdoaw"
        val s2 = "dawaw"
        val code = """
        "djaoiwjdoaw"
        
        "dawaw" for 
        
                "12345
        "
        
        """

        val tokens = TokenScanner(Source(code)).scanTokens().toMutableList()
        assertEquals(tokens[0].type, TokenType.STRING)
        assertEquals(tokens[0].literal, s1)
        assertEquals(tokens[1].literal, s2)
        assertEquals(tokens[1].type, TokenType.STRING)

        assertFails {
            val code = "\"123"
            TokenScanner(Source(code)).scanTokens().toMutableList()
        }
    }

    @Test
    fun test_Comment() {
        val code = """
        //123
        //123
        """
        val tokens = TokenScanner(Source(code)).scanTokens().toMutableList()
        assert(tokens.size == 1)
    }

    @Test
    fun test_Illegal_identifier_found() {
        val code = "@@@@@@@"
        TokenScanner(Source(code)).scanTokens().toMutableList()
        assert(KJlox.hasError)
    }
}