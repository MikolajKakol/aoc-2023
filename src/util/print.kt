package util

import java.awt.Color

//Main constants and functions
const val RC = "\u001b[0m" // Reset foreground and background colors. --> $RC
const val R = "\u001b[7m"  // Invert foreground to background. --> $R
const val U = "\u001b[4m"  // Underline. --> $U
const val B = "\u001b[1m"  // Bold. --> $B
const val I = "\u001b[3m"  // Italic. --> $I
const val S = "\u001b[9m"  // Strikethrough the text. --> $S

// 256 Colors
fun fg(n: Int) = "\u001b[38;5;${n}m" // Set a foreground color. --> ${fg(40)} //Sets a green color.
fun bg(n: Int) = "\u001b[48;5;${n}m" // Set a background color. --> ${bg(196)} //Sets a red color.

// RGB Colors
fun rgbfg(r: Int, g: Int, b: Int) =
    "\u001b[38;2;$r;$g;${b}m" // Set a RGB foreground color. --> ${rgbfg(0,255,0)} //Sets a green color.

fun rgbbg(r: Int, g: Int, b: Int) =
    "\u001b[48;2;$r;$g;${b}m" // Set a RGB background color. --> ${rgbbg(255,0,0)} //Sets a red color.

fun <T : Any?> T.println(textColor: Color? = null, backgroundColor: Color? = null): T {
    addColor(textColor, backgroundColor)
    println(this)
    resetColor()
    return this
}

fun <T : Any?> T.print(textColor: Color? = null, backgroundColor: Color? = null): T {
    addColor(textColor, backgroundColor)
    print(this)
    resetColor()
    return this
}

private fun addColor(textColor: Color?, backgroundColor: Color?) {
    if (textColor != null) {
        print(rgbfg(textColor.red, textColor.green, textColor.blue))
    }
    if (backgroundColor != null) {
        print(rgbbg(backgroundColor.red, backgroundColor.green, backgroundColor.blue))
    }
}

fun resetColor() {
    print(RC)
}
