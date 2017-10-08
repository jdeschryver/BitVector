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

    constructor()

    constructor(vector: BitVector) {
        this.words = vector.words
    }

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

    operator fun get(index: Int): Int {
        val wordIndex = getWordIndex(index)
        val newIndex = index % WORD_SIZE
        if (wordIndex > words.size) return 0
        return if (isSet(wordIndex, newIndex)) 1 else 0
    }

    operator fun plus(vector: BitVector) = BitVector(this).also { newVector ->
        vector.words.forEachIndexed { index, word ->
            newVector.ensureCapacity(index); newVector.words[index] = newVector.words[index] or word
        }
    }

    operator fun minus(vector: BitVector) = BitVector(this).also { newVector ->
        vector.words.forEachIndexed { index, word -> newVector.words[index] = words[index] and word.inv() }
    }

    private fun isSet(wordIndex: Int, index: Int) = words[wordIndex] and index.toMask() != 0

    private fun ensureCapacity(size: Int) {
        if (size < words.size) return
        var expandedWords = IntArray(size * 2)
        words.forEachIndexed { i, e -> expandedWords[i] = e }
        words = expandedWords
    }

    infix fun intersect(vector: BitVector) = BitVector().also { newVector ->
        vector.words.forEachIndexed { i, e ->
            newVector.ensureCapacity(i); newVector.words[i] = words[i] and e
        }
    }

    override fun toString() = mutableListOf<String>().also { s ->
        (0 until words.size).forEach { wordIndex ->
            (0..WORD_SIZE).forEach { i -> if (isSet(wordIndex, i)) s.add("${wordIndex * WORD_SIZE + i}") }
        }
    }.toString()
}


fun bitsOf(vararg bits: Int) = BitVector().also { vector -> bits.forEach { vector[it] = true } }