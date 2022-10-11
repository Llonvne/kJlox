package cn.llonvne.kJlox.compiler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CompilerKtTest {

    @Test
    fun test_compileScriptFileNotFound() {
        assertEquals(CompileResult.ScriptFileNotFound, compiler("NotExistFile.kj"))
    }
}