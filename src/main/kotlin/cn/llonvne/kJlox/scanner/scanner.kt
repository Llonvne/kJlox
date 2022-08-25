package cn.llonvne.kJlox.scanner

import cn.llonvne.kJlox.error.reportError
import cn.llonvne.kJlox.token.Token
import cn.llonvne.kJlox.token.TokenType
import cn.llonvne.kJlox.token.TokenType.*

val keyWords = mutableMapOf<String, TokenType>()

class Scanner(private val source: String) {
    private var tokens = mutableListOf<Token>()
    private var start = 0
    private var current = 0
    private var line = 1

    init {
        keyWords["and"] = AND
        keyWords["class"] = CLASS
        keyWords["else"] = ELSE
        keyWords["false"] = FALSE
        keyWords["for"] = FOR
        keyWords["fun"] = FUN
        keyWords["if"] = IF
        keyWords["nil"] = NIL
        keyWords["or"] = OR
        keyWords["print"] = PRINT
        keyWords["return"] = RETURN
        keyWords["super"] = SUPER
        keyWords["this"] = THIS
        keyWords["true"] = TRUE
        keyWords["var"] = VAR
        keyWords["while"] = WHILE
    }

    private fun isAtEnd() = current >= source.length

    private fun scanTokens(): List<Token> {

        // 当没有结束当时候不断扫描 Token
        while (!isAtEnd()) {
            current = start
            scanTokens()
        }

        // 添加 EOF Token 在 Token 列表末尾
        this.tokens.add(Token(EOF, "", null, line))
        return this.tokens
    }

    private fun scanSingleToken() {
        when (advance()) {
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)

            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(LEFT_BRACE)

            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)

            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)

            '!' -> addToken(
                if (match('=')) BANG_EQUAL else BANG
            )

            '=' -> addToken(
                if (match('=')) EQUAL_EQUAL else EQUAL
            )

            '<' -> addToken(
                if (match('=')) LESS_EQUAL else LESS
            )

            '>' -> addToken(
                if (match('=')) GREATER_EQUAL else GREATER
            )

            '/' -> {
                // 出现两个斜杠时，跳过这个行的所有内容
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) {
                        advance()
                    }
                } else {
                    // 简单添加除号
                    addToken(SLASH)
                }
            }

            // 跳过空白字符
            ' ', '\t', '\r' -> {
                // do nothing
            }

            '\n' -> {
                ++line
            }

            '"' -> stringLiteral()

            in '0'..'9' -> {
                number()
            }

            in 'A'..'Z' -> {
                identifier()
            }

            // TODO 非法的标识符错误提示
            else -> reportError(line, "", "Illegal identifier found")
        }
    }

    private fun advance(): Char {
        ++current
        return source[current - 1]
    }

    private fun addToken(type: TokenType, literal: Any? = null) {
        tokens.add(
            Token(
                type,
                source.substring(start, current),
                literal,
                line
            )
        )
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false

        if (source[current] == expected) {
            ++current
            return true
        }
        return false
    }

    private fun peek(): Char {
        return if (isAtEnd()) '\u0000' else source[current]
    }

    private fun peekNext(): Char {
        return if (current + 1 >= source.length) '\u0000' else source[current + 1]
    }

    private fun stringLiteral() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                ++line
            }
            advance()
        }

        if (isAtEnd()) {
            // TODO 完善未结束的字符串的错误提示
            reportError(line, "", "Unterminated string.")
        }

        // 右边的 "
        advance()

        addToken(STRING, source.subSequence(start + 1, current - 1))
    }

    private fun number() {

        // TODO 不合法的数字处理
        fun isDigit(char: Char) = char in '0'..'9'
        while (isDigit(peek())) {
            advance()
        }

        if (peek() == '.' && isDigit(peekNext())) {
            advance()
            while (isDigit(peek())) {
                advance()
            }
        }

        // TODO 使用 try 来捕获转换错误
        addToken(
            NUMBER,
            source.subSequence(start, current) to Double
        )
    }

    private fun identifier() {
        while (isLegalIdentifierChar(peek())) {
            advance()
        }
        val text:String = source.substring(start,current)
        addToken(keyWords[text] ?: IDENTIFIER)
    }

    private fun isLegalIdentifierChar(char: Char): Boolean {
        return char.isLetterOrDigit() || char == '_'
    }
}