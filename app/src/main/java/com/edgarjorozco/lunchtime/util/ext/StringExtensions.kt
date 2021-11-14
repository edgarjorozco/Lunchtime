package com.edgarjorozco.lunchtime.util

// helpers for databinding

fun withBulletSeparator(vararg strings: String): String = strings.joinToString(" • ")

fun String.repeatN(int: Int): String = this.repeat(int)
