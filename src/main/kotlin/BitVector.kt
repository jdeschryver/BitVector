/**
 * @author Jan De Schryver <Jan.DeSchryver@bucephalus.be>
 */


const val INITIAL_ARRAY_SIZE = 4
const val WORD_SIZE = 32
const val WORD_INDEX_CALC: Int = 5

class BitVector {

    private var words = IntArray(INITIAL_ARRAY_SIZE)

    private fun getWordIndex(bitIndex: Int) = bitIndex shr WORD_INDEX_CALC

    private fun Int.toMask() = 1 shl this

    operator fun set(index: Int, status: Boolean) {
        val wordIndex = getWordIndex(index)
        val newIndex = index % WORD_SIZE
        ensureCapacity(wordIndex)
        if (status) {
            words[wordIndex] = words[wordIndex] or newIndex.toMask()
        } else {
            words[wordIndex] = words[wordIndex] and newIndex.toMask().inv()
        }
    }

    operator fun get(index: Int) : Boolean {
        val wordIndex = getWordIndex(index)
        val newIndex = index % WORD_SIZE
        if (wordIndex > words.size) return false
        return isSet(wordIndex, newIndex)
    }

    private fun isSet(wordIndex: Int, index: Int) = words[wordIndex] and index.toMask() != 0

    private fun ensureCapacity(size: Int){
        if (size < words.size) return
        val expansionFactor = (size / words.size) * 2
        val expandedWords = IntArray(words.size * expansionFactor)
        words.forEachIndexed { i,e -> expandedWords[i] = e }
        words = expandedWords
    }
}

fun bitsOf(vararg bits: Int) : BitVector {
    return BitVector().apply { bits.forEach{ set(it, true) } }
}

fun main(args: Array<String>) {
    val v1 = bitsOf(0, 4, 589)
    v1[0] = true
    v1[33] = true

    println(v1[1])
}