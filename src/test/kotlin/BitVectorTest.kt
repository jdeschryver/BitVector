import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals
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
            bitVector = bitsOf(*bitsSet)
        }

        it("should return 1 when the bit is set") {
            bitsSet.forEach { bitVector[it] = true }
            bitsSet.forEach { assertEquals(1, bitVector[it]) }
        }

        it("should return 0 when the bit is not set") {
            val bitsUnset = intArrayOf(2, 8, 36584, 13, 45)
            bitsUnset.forEach { bitVector[it] = false }
            bitsUnset.forEach { assertEquals(0, bitVector[it]) }
        }

        it("should return a bitvector which is the sum of both given vector") {
            val bitsSet2 = intArrayOf(2, 8, 36584, 13, 45)
            val bitVector2 = bitsOf(*bitsSet2)
            val plusVector = bitVector + bitVector2
            bitsSet.plus(bitsSet2).forEach { assertEquals(1, plusVector[it]) }
        }

        it("should return a 0 for all bits that are subtracted and 1 for the non subtracted") {
            val bitsSet2 = intArrayOf(36584, 25, 1)
            val bitVector2 = bitsOf(*bitsSet2)
            val minusVector = bitVector - bitVector2
            bitsSet.plus(bitsSet2).forEach {
                when (it) {
                    in bitsSet2 -> assertEquals(0, minusVector[it])
                    else -> assertEquals(1, minusVector[it])
                }
            }
        }

        it("should return 1 for all bits that are in the intersection and 0 for those not in the intersection") {
            val bitsSet2 = intArrayOf(36584, 25, 1)
            val bitVector2 = bitsOf(*bitsSet2)
            val intersectVector = bitVector intersect bitVector2
            bitsSet.plus(bitsSet2).forEach {
                when (it) {
                    in bitsSet2.intersect(bitsSet.asIterable()) -> assertEquals(1, intersectVector[it])
                    else -> assertEquals(0, intersectVector[it])
                }
            }
        }
    }
})