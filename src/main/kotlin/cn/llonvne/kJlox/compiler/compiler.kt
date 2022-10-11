package cn.llonvne.kJlox.compiler

import cn.llonvne.kJlox.main.KJlox
import java.io.File
import java.io.FileNotFoundException

/**
 * @brief 编译结果
 */
enum class CompileResult {
    OK,
    ScriptFileNotFound,
    NotAKJloxFile
}

/**
 * 源代码变量
 */
internal var originCode: String = ""

/**
 * @brief 编译主函数
 * @param scriptFile 编译文件文件名
 * @return CompileResult 编译结果枚举类
 */
fun compiler(scriptFile: String): CompileResult {


    // 对于输入的文件名进行处理 去除多余空格
    val trimScriptFile = scriptFile.trim()

    // 检查文件后缀是否符合标准
    if (!trimScriptFile.endsWith
            (KJlox.kJloxStandardFileExtension, ignoreCase = true)
    ) {
        return CompileResult.NotAKJloxFile
    }

    // 尝试读取文件
    try {
        originCode = File(scriptFile).readText()
    } catch (e: FileNotFoundException) {
        return CompileResult.ScriptFileNotFound
    }

    val source = cn.llonvne.kJlox.scanner.Source(originCode)
    val tokens = cn.llonvne.kJlox.scanner.TokenScanner(source).scanTokens()
//    val parser = Parser(tokens).parse()

    return CompileResult.OK
}