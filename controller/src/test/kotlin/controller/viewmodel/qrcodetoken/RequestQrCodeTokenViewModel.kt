package controller.viewmodel.qrcodetoken

import utils.exceptions.http.HttpError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RequestQrCodeTokenViewModelTest {

    @Test
    fun `converts RequestQrCodeTokenViewModel to RequestLoginCodeModel when size is provided`() {
        val viewModel = RequestQrCodeTokenViewModel(size = 256)
        val ip = "192.168.1.1"
        val agent = "Test Agent"

        val result = viewModel.toDomainModel(ip, agent)

        assertEquals(ip, result.ipAddress)
        assertEquals(agent, result.agent)
        assertEquals(256, result.size)
    }

    @Test
    fun `throws HttpError when size is missing`() {
        val viewModel = RequestQrCodeTokenViewModel(size = null)
        val ip = "192.168.1.1"
        val agent = "Test Agent"

        val exception = assertFailsWith<HttpError> { viewModel.toDomainModel(ip, agent) }
        assertEquals("Missing required field: size", exception.message)
    }
}