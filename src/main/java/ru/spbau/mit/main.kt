package ru.spbau.mit

import java.io.InputStreamReader
import java.util.*


class BinarySegmentTree(private val size: Int) {
    private val elements: IntArray = IntArray(4 * size) { 0 }

    fun set(index: Int, value: Boolean) = set(1, 0, size, index, value)

    fun get(index: Int) = get(1, 0, size, index)

    fun getKth(k: Int): Int = getKth(1, 0, size, k)

    private tailrec fun getKth(node: Int, left: Int, right: Int, k: Int): Int {
        if (left == right) {
            return left
        }

        val middle = getMiddle(left, right)

        return if (k <= elements[getLeftNodeIndex(node)]) {
            getKth(getLeftNodeIndex(node), left, middle, k)
        } else {
            getKth(getRightNodeIndex(node), middle + 1, right, k - elements[getLeftNodeIndex(node)])
        }
    }

    private fun set(node: Int, left: Int, right: Int, index: Int, value: Boolean): Unit {
        if (left == right) {
            elements[node] = if (value) 1 else 0
        } else {
            val middle = getMiddle(left, right)

            if (index <= middle) {
                set(getLeftNodeIndex(node), left, middle, index, value)
            } else {
                set(getRightNodeIndex(node), middle + 1, right, index, value)
            }

            elements[node] = elements[getLeftNodeIndex(node)] + elements[getRightNodeIndex(node)]
        }
    }

    private fun get(node: Int, left: Int, right: Int, index: Int): Boolean {
        if (left == right) {
            return elements[node] == 1
        }

        val middle = getMiddle(left, right)

        return if (index <= middle) {
            get(getLeftNodeIndex(node), left, middle, index)
        } else {
            get(getRightNodeIndex(node), middle + 1, right, index)
        }
    }

    private fun getLeftNodeIndex(node: Int) = 2 * node

    private fun getRightNodeIndex(node: Int) = 2 * node + 1

    private fun getMiddle(left: Int, right: Int): Int = left + (right - left) / 2
}

class StringManipulation(private val name: String,
                         private val queries: List<Query>) {
    private val trees: Array<BinarySegmentTree> = Array(LETTERS_COUNT) {
        BinarySegmentTree(name.length)
    }

    fun solve(): String {
        name.forEachIndexed { index, character -> getTreeByCharacter(character).set(index, true) }

        queries.forEach {
            val tree = getTreeByCharacter(it.character)
            tree.set(tree.getKth(it.index), false)
        }

        return name.filterIndexed { index, character -> getTreeByCharacter(character).get(index) }
    }

    private fun getTreeByCharacter(char: Char): BinarySegmentTree = trees[char - 'a']

    companion object {
        val LETTERS_COUNT: Int = 26

        fun read(scanner: Scanner): StringManipulation {
            val repetitionCount = scanner.nextInt()
            scanner.nextLine()
            val pattern = scanner.nextLine()
            val queries = (1..scanner.nextInt()).map { Query.read(scanner) }

            return StringManipulation(pattern.repeat(repetitionCount), queries)
        }
    }


    data class Query(val index: Int, val character: Char) {
        companion object {
            inline fun read(scanner: Scanner) = Query(scanner.nextInt(), scanner.nextLine()[1])
        }
    }
}

fun main(args: Array<String>) = println(StringManipulation.read(Scanner(InputStreamReader(System.`in`))).solve())
