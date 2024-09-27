package com.crayon.ryan.utils

import org.jetbrains.annotations.TestOnly

/**
 * 移除字符串中的空格、制表符、回车、换行符
 * @receiver String 需要修改的字符串
 * @param count Int 连续的空格的个数,当 count 为 0 时，将会删除所有空格和制表符、回车、换行符,当大于0时，将会 使 连续的 count 个或更多的空格 外加制表符、回车、换行符 变成一个空格
 * @return String 修改后的字符串
 * @sample test1()
 * @sample test2()
 */
fun String.removeBlank(count: Int = 0): String {
    return if (count == 0) {
        this.replace("\\s*|\t|\r|\n".toRegex(), "")
    } else {
        this.replace("\\s{$count,}|\t|\r|\n".toRegex(), " ")
    }
}

private fun test1() {
    val str = "this is   a  test"
    val result = str.removeBlank()//结果: thisisatest
}

private fun test2() {
    val str = "this is   a  test"
    val result = str.removeBlank(2)//结果: this is a test
}