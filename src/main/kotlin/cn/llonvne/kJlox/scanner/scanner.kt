package cn.llonvne.kJlox.scanner

import cn.llonvne.kJlox.error.reportError
import cn.llonvne.kJlox.token.Token
import cn.llonvne.kJlox.token.TokenType
import cn.llonvne.kJlox.token.TokenType.*
import cn.llonvne.kJlox.token.printableTokenList

/**
 * # 代码扫描器类，用于将文本拆解为 Token
 * @constructor 一个代码文件 Source
 */
class TokenScanner(private val source: Source) {

    private var tokens = mutableListOf<Token>()

    /**
     * ## 代码扫描器启动函数
     * 负责对于构造器传入到源代码进行 Token 分析
     * @return List<Token> 分析好的 Token 列表
     */
    fun scanTokens(): List<Token> {

        // 当没有结束当时候不断扫描 Token
        while (!source.isAtEnd()) {
            source.current = source.start
            scanSingleToken()
        }

        // 添加 EOF Token 在 Token 列表末尾
        this.tokens.add(Token(EOF, "", null, source.line))
        return this.tokens
    }

    /**
     * ## 该函数用于分析具体的单个Token
     */
    private fun scanSingleToken() {

        when (source.advance()) {
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
                if (source.match('=')) BANG_EQUAL else BANG
            )

            '=' -> addToken(
                if (source.match('=')) EQUAL_EQUAL else EQUAL
            )

            '<' -> addToken(
                if (source.match('=')) LESS_EQUAL else LESS
            )

            '>' -> addToken(
                if (source.match('=')) GREATER_EQUAL else GREATER
            )

            '/' -> {
                // 出现两个斜杠时，跳过这个行的所有内容
                if (source.match('/')) {
                    while (source.peek() != '\n' && !source.isAtEnd()) {
                        source.advance()
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
                ++source.line
            }

            '"' -> stringLiteral()

            in '0'..'9' -> {
                number()
            }

            in 'A'..'Z' -> {
                identifier()
            }

            // TODO 非法的标识符错误提示
            else -> reportError(source.line, "", "Illegal identifier found")
        }
    }

    /**
     * ## 该函数用于快速添加 Token 自动填入当前 Token 起始坐标和行号
     * @param type Token的类型
     * @param literal 的字面
     * @return Unit 不返回
     */
    private fun addToken(type: TokenType, literal: Any? = null) {
        tokens.add(
            Token(
                type,
                source.sourceText.substring(source.start, source.current),
                literal,
                source.line
            )
        )
    }

    /**
     * ## 解析文本串函数
     */
    private fun stringLiteral() {
        with(source) {
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

            addToken(STRING, sourceText.subSequence(start + 1, current - 1))
        }
    }

    /**
     * ## 解析数字
     */
    private fun number() {
        with(source) {
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
                sourceText.subSequence(start, current) to Double
            )
        }
    }

    /**
     * ## 解析关键字或者用户自定义标识符号
     */
    private fun identifier() {
        with(source) {
            while (isLegalIdentifierChar(peek())) {
                advance()
            }
            val text: String = sourceText.substring(start, current)
            if (text in printableTokenList) {
                addToken(TokenType.valueOf(text))
            } else addToken(IDENTIFIER)
        }
    }

    /**
     * ## 解析是否为合法的标识符字符
     */
    private fun isLegalIdentifierChar(char: Char): Boolean {
        return char.isLetterOrDigit() || char == '_'
    }
}

