import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author Jan De Schryver <Jan.DeSchryver@bucephalus.be>
 */


class BitVectorTest : Spek({
    given("a bitvector") {
        var bitVector = BitVector()
        val bitsSet = intArrayOf(0, 4, 7, 32, 33, 698, 36584, 25, 1)

        beforeEachTest {
            bitVector = BitVector()
        }

        it("should return true when the bit is set") {
            bitsSet.forEach { bitVector[it] = true }
            bitsSet.forEach { assertTrue { bitVector[it] } }
        }

        it("should return false when the bit is not set") {
            bitsSet.forEach { assertFalse { bitVector[it] } }
        }

        it("should return a bitvector which is the sum of both given vector") {
            val bitsSet2 = intArrayOf(2, 8, 36584, 13, 45)
            val bitVector2 = bitsOf(*bitsSet)
            val plusVector = bitVector + bitVector2
            bitsSet.plus(bitsSet2).forEach { plusVector[it] }
        }
    }
})