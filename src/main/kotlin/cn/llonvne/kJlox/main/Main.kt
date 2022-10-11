package cn.llonvne.kJlox.main

import cn.llonvne.kJlox.compiler.compiler
import cn.llonvne.kJlox.interpreter.interpreter
import kotlin.system.exitProcess

/**
 * # kJlox 全局对象，用于活跃对象注册和关键常量定义
 */
object KJlox {

    /**
     * @brief 定义程序语言名字
     */
    const val kJloxName = "kJlox"

    /**
     * @beief 定义文件标准后缀名
     */
    const val kJloxStandardFileExtension = "kj"

    /**
     * # kJlox 工作模式
     * 定义工作模式
     * 一共有三种模式，两种有效，一种错误
     * 分别为
     * * ArgsInvalid 参数错误
     * * InterpreterMode 解释器模式
     * * CompilerMode 编译器模式
     */
    internal enum class WorkMode(exitCode: Int) {
        ArgsInvalid(-1),
        InterpreterMode(0),
        CompilerMode(0)
    }

    /**
     * @brief 定义标准的前缀打印
     */
    @JvmStatic
    fun printKJloxPrefix() {
        print("$kJloxName: ")
    }

    /**
     * # 在主类中定义错误标记
     */
    var hasError: Boolean = false
}

/**
 * # 启动函数
 * @brief 主函数，负责选择模式
 * @param args 命令行参数
 */
fun main(vararg args: String) {
    when (selectWorkMode(args.toList())) {

        KJlox.WorkMode.ArgsInvalid -> {
            KJlox.printKJloxPrefix()
            println("Wrong parameter length,Usage: kjlox [script file]")
            exitProcess(KJlox.WorkMode.ArgsInvalid.ordinal)
        }

        KJlox.WorkMode.CompilerMode -> {
            compiler(args[0])
        }

        KJlox.WorkMode.InterpreterMode -> {
            interpreter()
        }
    }
}

/**
 * @brief 接受命令行输入，决定工作模式
 * @param args:List<String> 命令行参数
 * @return Kjlox.WorkMode 枚举类结果
 */
private fun selectWorkMode(args: List<String>): KJlox.WorkMode {
    return when (args.size) {
        0 -> KJlox.WorkMode.InterpreterMode

        1 -> KJlox.WorkMode.CompilerMode

        else -> KJlox.WorkMode.ArgsInvalid
    }
}
