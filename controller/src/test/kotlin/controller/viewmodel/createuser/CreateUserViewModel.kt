package controller.viewmodel.createuser

import utils.exceptions.http.HttpError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateUserRequestViewModelTest {

    @Test
    fun `converts to CreateUserModel when all fields are provided`() {
        val viewModel = CreateUserRequestViewModel(
            email = "test@example.com ",
            confirmEmail = " test@example.com ",
            password = "password123 "
        )

        val result = viewModel.toDomainModel()

        assertEquals("test@example.com", result.email)
        assertEquals("test@example.com", result.confirmEmail)
        assertEquals("password123", result.password)
    }

    @Test
    fun `throws HttpError when email is missing`() {
        val viewModel = CreateUserRequestViewModel(
            confirmEmail = "test@example.com",
            password = "password123"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toDomainModel() }
        assertEquals("E-mail field is required", exception.message)
    }

    @Test
    fun `throws HttpError when confirmEmail is missing`() {
        val viewModel = CreateUserRequestViewModel(
            email = "test@example.com",
            password = "password123"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toDomainModel() }
        assertEquals("confirm e-mail field is required", exception.message)
    }

    @Test
    fun `throws HttpError when password is missing`() {
        val viewModel = CreateUserRequestViewModel(
            email = "test@example.com",
            confirmEmail = "test@example.com"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toDomainModel() }
        assertEquals("password field is required", exception.message)
    }
}