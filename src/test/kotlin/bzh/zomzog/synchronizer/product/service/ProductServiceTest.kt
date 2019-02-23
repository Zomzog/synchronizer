package bzh.zomzog.synchronizer.product.service

import bzh.zomzog.synchronizer.product.domain.DiffResult
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ProductServiceTest {

    @Test
    fun `Two empty`() = runBlocking {
        val etsy = emptyList<String>()
        val ugm = emptyList<String>()
        val leftEtsy = emptyList<String>()
        val leftugm = emptyList<String>()

        assertThat(algo(etsy, ugm)).isEqualTo(DiffResult(etsy, ugm, leftEtsy, leftugm))

        Unit
    }

    @Test
    fun `All product diff`() = runBlocking {
        val etsy = listOf("prod1", "prod2")
        val ugm = listOf("uprod1", "uprod2")
        val leftEtsy = listOf("prod1", "prod2")
        val leftugm = listOf("uprod1", "uprod2")

        assertThat(algo(etsy, ugm)).isEqualTo(DiffResult(etsy, ugm, leftEtsy, leftugm))

        Unit
    }

    @Test
    fun `One common`() = runBlocking {
        val etsy = listOf("prod1", "prod2")
        val ugm = listOf("prod1", "uprod2")
        val leftEtsy = listOf("prod2")
        val leftugm = listOf("uprod2")

        assertThat(algo(etsy, ugm)).isEqualTo(DiffResult(etsy, ugm, leftEtsy, leftugm))

        Unit
    }

    @Test
    fun `One common twice in only one list`() = runBlocking {
        val etsy = listOf("prod1", "prod1", "prod2")
        val ugm = listOf("prod1", "uprod2")
        val leftEtsy = listOf("prod1", "prod2")
        val leftugm = listOf("uprod2")

        assertThat(algo(etsy, ugm)).isEqualTo(DiffResult(etsy, ugm, leftEtsy, leftugm))

        Unit
    }

    fun algo(etsy: List<String>, ugm: List<String>): DiffResult {
        val leftEtsy = etsy.toMutableList()
        val leftugm = ugm.toMutableList()

        for (e in leftEtsy.toMutableList()) {
            if (leftugm.contains(e)) {
                leftEtsy.remove(e)
                leftugm.remove(e)
            }
        }

        return DiffResult(etsy, ugm, leftEtsy, leftugm)
    }
}