package cn.llonvne.kJlox.token

enum class TokenType(value: String?) {
    // Single-character tokens.
    LEFT_PAREN("("), RIGHT_PAREN(")"),
    LEFT_BRACE("{"), RIGHT_BRACE("}"),
    COMMA(","), DOT("."),
    MINUS("-"), PLUS("+"), SEMICOLON(";"),
    SLASH("/"), STAR("*"),

    // One or two character tokens.
    BANG("!"), BANG_EQUAL("!="),
    EQUAL("="), EQUAL_EQUAL("=="),
    GREATER(">"), GREATER_EQUAL(">="),
    LESS("<"), LESS_EQUAL("<="),

    // Literals.
    IDENTIFIER("identifier"),
    STRING("string"),
    NUMBER("number"),

    // Keywords.
    AND("and"),
    CLASS("class"),
    ELSE("else"),
    FALSE("false"),
    FUN("fun"),
    FOR("for"),
    IF("if"),
    NIL("nil"),
    OR("or"),
    PRINT("print"),
    RETURN("return"),
    SUPER("super"),
    THIS("this"),
    TRUE("true"),
    VAR("var"),
    WHILE("while"),

    EOF(null);
}

/**
 * @brief 标记数据类
 * @property type 标记类型
 * @property lexeme 词素
 * @property literal 原文本
 * @property line 标记所在行
 */
data class Token
    (
    val type: TokenType,
    val lexeme: String,
    val literal: Any?,
    val line: Int,
)