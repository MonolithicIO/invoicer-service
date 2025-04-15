package controller.viewmodel.beneficiary

import utils.exceptions.http.HttpError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateBeneficiaryViewModelTest {

    @Test
    fun `converts to UpdateBeneficiaryModel when all fields are provided`() {
        val viewModel = UpdateBeneficiaryViewModel(
            name = "John Doe ",
            iban = " DE89370400440532013000 ",
            swift = " ABCDEF12 ",
            bankName = " Bank of Test ",
            bankAddress = " 123 Test Street "
        )

        val result = viewModel.toViewModel()

        assertEquals("John Doe", result.name)
        assertEquals("DE89370400440532013000", result.iban)
        assertEquals("ABCDEF12", result.swift)
        assertEquals("Bank of Test", result.bankName)
        assertEquals("123 Test Street", result.bankAddress)
    }

    @Test
    fun `throws HttpError when name is missing`() {
        val viewModel = UpdateBeneficiaryViewModel(
            iban = "DE89370400440532013000",
            swift = "ABCDEF12",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toViewModel() }
        assertEquals("Missing field name", exception.message)
    }

    @Test
    fun `throws HttpError when iban is missing`() {
        val viewModel = UpdateBeneficiaryViewModel(
            name = "John Doe",
            swift = "ABCDEF12",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toViewModel() }
        assertEquals("Missing field iban", exception.message)
    }

    @Test
    fun `throws HttpError when swift is missing`() {
        val viewModel = UpdateBeneficiaryViewModel(
            name = "John Doe",
            iban = "DE89370400440532013000",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toViewModel() }
        assertEquals("Missing field swift", exception.message)
    }

    @Test
    fun `throws HttpError when bank name is missing`() {
        val viewModel = UpdateBeneficiaryViewModel(
            name = "John Doe",
            iban = "DE89370400440532013000",
            swift = "ABCDEF12",
            bankAddress = "123 Test Street"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toViewModel() }
        assertEquals("Missing field bank name", exception.message)
    }

    @Test
    fun `throws HttpError when bank address is missing`() {
        val viewModel = UpdateBeneficiaryViewModel(
            name = "John Doe",
            iban = "DE89370400440532013000",
            swift = "ABCDEF12",
            bankName = "Bank of Test"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toViewModel() }
        assertEquals("Missing field bank address", exception.message)
    }
}