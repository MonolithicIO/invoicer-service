package controller.viewmodel.beneficiary


import utils.exceptions.http.HttpError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateBeneficiaryViewModelTest {

    @Test
    fun `converts to CreateBeneficiaryModel when all fields are provided`() {
        val viewModel = CreateBeneficiaryViewModel(
            name = "John Doe",
            swift = "ABCDEF12",
            iban = "DE89370400440532013000",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street"
        )

        val result = viewModel.toViewModel()

        assertEquals("John Doe", result.name)
        assertEquals("ABCDEF12", result.swift)
        assertEquals("DE89370400440532013000", result.iban)
        assertEquals("Bank of Test", result.bankName)
        assertEquals("123 Test Street", result.bankAddress)
    }

    @Test
    fun `throws HttpError when name is missing`() {
        val viewModel = CreateBeneficiaryViewModel(
            swift = "ABCDEF12",
            iban = "DE89370400440532013000",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toViewModel() }
        assertEquals("Missing name field", exception.message)
    }

    @Test
    fun `throws HttpError when swift is missing`() {
        val viewModel = CreateBeneficiaryViewModel(
            name = "John Doe",
            iban = "DE89370400440532013000",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toViewModel() }
        assertEquals("Missing swift field", exception.message)
    }

    @Test
    fun `throws HttpError when iban is missing`() {
        val viewModel = CreateBeneficiaryViewModel(
            name = "John Doe",
            swift = "ABCDEF12",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toViewModel() }
        assertEquals("Missing iban field", exception.message)
    }

    @Test
    fun `throws HttpError when bank name is missing`() {
        val viewModel = CreateBeneficiaryViewModel(
            name = "John Doe",
            swift = "ABCDEF12",
            iban = "DE89370400440532013000",
            bankAddress = "123 Test Street"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toViewModel() }
        assertEquals("Missing bank name field", exception.message)
    }

    @Test
    fun `throws HttpError when bank address is missing`() {
        val viewModel = CreateBeneficiaryViewModel(
            name = "John Doe",
            swift = "ABCDEF12",
            iban = "DE89370400440532013000",
            bankName = "Bank of Test"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toViewModel() }
        assertEquals("Missing bank address field", exception.message)
    }
}