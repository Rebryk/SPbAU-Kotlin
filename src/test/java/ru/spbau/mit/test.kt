package ru.spbau.mit

import kotlin.test.assertEquals
import org.junit.Test
import kotlin.test.assertTrue

class TestSource {
    @Test
    fun testEmpty() {
        val result = TeX { }.toString()
        assertTrue(result.isEmpty())
    }

    @Test
    fun testDocumentClass() {
        val result = TeX {
            documentClass("beamer")
        }.toString()

        assertEquals("""
            |\documentclass{beamer}
            |""".trimMargin(), result)
    }

    @Test
    fun testUsePackage() {
        val result = TeX {
            usepackage("hyperref")
            usepackage("framed", "array")
            usepackage("geometry", "paperwidh" to "10cm")
        }.toString()

        assertEquals("""
            |\usepackage{hyperref}
            |\usepackage{framed, array}
            |\usepackage[paperwidh = 10cm]{geometry}
            |""".trimMargin(), result)
    }

    @Test
    fun testItemize() {
        val result = TeX {
            document {
                itemize("topsep" to "0cm") {
                    for (item in 1..3) {
                        item {
                            +"Item $item"
                        }
                    }
                }
            }
        }.toString()

        assertEquals("""
            |\begin{document}
            |\begin{itemize}[topsep = 0cm]
            |\item
            |Item 1
            |\item
            |Item 2
            |\item
            |Item 3
            |\end{itemize}
            |\end{document}
            |""".trimMargin(), result)
    }

    @Test
    fun testFrame() {
        val result = TeX {
            document {
                frame("Frame", "arg1" to "value1", "arg2" to "value2") {
                    +"Empty frame"
                }
            }
        }.toString()

        assertEquals("""
            |\begin{document}
            |\begin{frame}[arg1 = value1, arg2 = value2]
            |\frametitle{Frame}
            |Empty frame
            |\end{frame}
            |\end{document}
            |""".trimMargin(), result)
    }

    @Test
    fun testCustomTag() {
        val result = TeX {
            document {
                frame("Frame with code", "arg1" to "value1", "arg2" to "value2") {
                    customTag("code", "language" to "kotlin") {
                        +"val x: Int = 1"
                        +"val y: Int = 2"
                        +"val z = x + y"
                    }
                }
            }
        }.toString()

        assertEquals("""
            |\begin{document}
            |\begin{frame}[arg1 = value1, arg2 = value2]
            |\frametitle{Frame with code}
            |\begin{code}[language = kotlin]
            |val x: Int = 1
            |val y: Int = 2
            |val z = x + y
            |\end{code}
            |\end{frame}
            |\end{document}
            |""".trimMargin(), result)
    }

    @Test
    fun testMath() {
        val result = TeX {
            document {
                math {
                    +"\\frac{4}{2} = 2"
                }
            }
        }.toString()

        assertEquals("""
            |\begin{document}
            |\begin{displaymath}
            |\frac{4}{2} = 2
            |\end{displaymath}
            |\end{document}
            |""".trimMargin(), result)
    }

    @Test
    fun testSimpleDocument() {
        val result = TeX {
            documentClass("beamer")
            usepackage("geometry", "paperwidh" to "10cm")

            title("Test document")

            document {
                +"Test document content"
            }
        }.toString()

        assertEquals("""
            |\documentclass{beamer}
            |\usepackage[paperwidh = 10cm]{geometry}
            |\title{Test document}
            |\begin{document}
            |Test document content
            |\end{document}
            |""".trimMargin(), result)
    }

    @Test
    fun testAdvancedDocument() {
        val result = TeX {
            documentClass("article")
            usepackage("hyperref")
            usepackage("framed", "array")
            usepackage("geometry", "paperwidh" to "10cm")

            title("Test document")
            author("Rebryk Yurii")
            date("2017/11/18")

            document {
                itemize("topsep" to "0cm") {
                    for (i in 1..3) {
                        item {
                            +"Item $i"
                        }
                    }

                    item {
                        enumerate {
                            item { +"Enumerate 1" }
                            item { +"Enumerate 2" }
                        }
                    }
                }

                center {
                    +"Simple math:"
                    math {
                        +"2 + 2 = 4"
                    }
                }

                frame("Frame with code", "arg1" to "value1", "arg2" to "value2") {
                    customTag("code", "language" to "c++") {
                        +"... write your code here ..."
                    }
                }
            }
        }.toString()

        assertEquals("""
            |\documentclass{article}
            |\usepackage{hyperref}
            |\usepackage{framed, array}
            |\usepackage[paperwidh = 10cm]{geometry}
            |\title{Test document}
            |\author{Rebryk Yurii}
            |\date{2017/11/18}
            |\begin{document}
            |\begin{itemize}[topsep = 0cm]
            |\item
            |Item 1
            |\item
            |Item 2
            |\item
            |Item 3
            |\item
            |\begin{enumerate}
            |\item
            |Enumerate 1
            |\item
            |Enumerate 2
            |\end{enumerate}
            |\end{itemize}
            |\begin{center}
            |Simple math:
            |\begin{displaymath}
            |2 + 2 = 4
            |\end{displaymath}
            |\end{center}
            |\begin{frame}[arg1 = value1, arg2 = value2]
            |\frametitle{Frame with code}
            |\begin{code}[language = c++]
            |... write your code here ...
            |\end{code}
            |\end{frame}
            |\end{document}
            |""".trimMargin(), result)
    }
}
