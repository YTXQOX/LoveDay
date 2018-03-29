package com.ljstudio.android.loveday.entity

/**
 * Created by chenjianbin on 2018/3/9.
 */
class KotinTest {
    var a : Int = 1
    var b : String = "kotlin"
    var c : String = "test"
    var d = 1
    var e = "what"

    var test : String = """$b$c"""
    var test2 : String = """|$b$c""".trimMargin()

//    fun test() {
//        for ((index, value) in array.withIndex()) {
//            println(" $index $value ")
//        }
//    }

    fun test(str : String) {}

    fun test1(str : String) : String {
        return ""
    }

    fun test2() : Unit{}

}