package controller.viewmodel.intermediary

import utils.exceptions.http.HttpError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateIntermediaryViewModelTest {

    @Test
    fun `converts to CreateIntermediaryModel when all fields are provided`() {
        val viewModel = CreateIntermediaryViewModel(
            name = "Intermediary Name ",
            swift = " ABCDEF12 ",
            iban = " DE89370400440532013000 ",
            bankName = " Bank of Test ",
            bankAddress = " 123 Test Street "
        )

        val result = viewModel.toModel()

        assertEquals("Intermediary Name", result.name)
        assertEquals("ABCDEF12", result.swift)
        assertEquals("DE89370400440532013000", result.iban)
        assertEquals("Bank of Test", result.bankName)
        assertEquals("123 Test Street", result.bankAddress)
    }

    @Test
    fun `throws HttpError when name is missing`() {
        val viewModel = CreateIntermediaryViewModel(
            swift = "ABCDEF12",
            iban = "DE89370400440532013000",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toModel() }
        assertEquals("Missing name field", exception.message)
    }

    @Test
    fun `throws HttpError when swift is missing`() {
        val viewModel = CreateIntermediaryViewModel(
            name = "Intermediary Name",
            iban = "DE89370400440532013000",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toModel() }
        assertEquals("Missing swift field", exception.message)
    }

    @Test
    fun `throws HttpError when iban is missing`() {
        val viewModel = CreateIntermediaryViewModel(
            name = "Intermediary Name",
            swift = "ABCDEF12",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toModel() }
        assertEquals("Missing iban field", exception.message)
    }

    @Test
    fun `throws HttpError when bank name is missing`() {
        val viewModel = CreateIntermediaryViewModel(
            name = "Intermediary Name",
            swift = "ABCDEF12",
            iban = "DE89370400440532013000",
            bankAddress = "123 Test Street"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toModel() }
        assertEquals("Missing bank name field", exception.message)
    }

    @Test
    fun `throws HttpError when bank address is missing`() {
        val viewModel = CreateIntermediaryViewModel(
            name = "Intermediary Name",
            swift = "ABCDEF12",
            iban = "DE89370400440532013000",
            bankName = "Bank of Test"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toModel() }
        assertEquals("Missing bank address field", exception.message)
    }
}