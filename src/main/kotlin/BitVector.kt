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

    operator fun minus(vector: BitVector): BitVector{
        val newVector = BitVector()
        vector.words.forEachIndexed { i, e -> if ( i > words.size) return newVector else newVector.words[i] = minus(i, e) }
        return newVector
    }

    private fun minus(index: Int, word: Int): Int {
        return words[index] and word.inv()
    }

    private fun isSet(wordIndex: Int, index: Int) = words[wordIndex] and index.toMask() != 0

    private fun ensureCapacity(size: Int){
        if (size < words.size) return
        val expansionFactor = (size / words.size) * 2
        val expandedWords = IntArray(words.size * expansionFactor)
        words.forEachIndexed { i,e -> expandedWords[i] = e }
        words = expandedWords
    }

    fun size() =  words.size * WORD_SIZE
}

fun bitsOf(vararg bits: Int) = BitVector().also {
    vector -> bits.forEach{ vector[it] = true } }

fun main(args: Array<String>) {
    var v1 = bitsOf(0, 4, 8, 5)
    println(v1[5])
    val v2 = bitsOf(1, 2, 5)
    v1 -= v2

    println("${v1[5]}, ${v1[2]}, ${v1[0]}")

}