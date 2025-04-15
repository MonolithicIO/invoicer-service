package controller.viewmodel.login

import models.login.AuthTokenModel
import utils.exceptions.http.HttpError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoginViewModelTest {

    @Test
    fun `converts LoginViewModel to LoginModel when all fields are valid`() {
        val viewModel = LoginViewModel(
            email = "test@example.com",
            password = "password123"
        )

        val result = viewModel.toDomainModel()

        assertEquals("test@example.com", result.email)
        assertEquals("password123", result.password)
    }

    @Test
    fun `throws HttpError when email is missing`() {
        val viewModel = LoginViewModel(
            email = null,
            password = "password123"
        )

        val exception = assertFailsWith<HttpError> { viewModel.toDomainModel() }
        assertEquals("Missing e-mail field", exception.message)
    }

    @Test
    fun `throws HttpError when password is missing`() {
        val viewModel = LoginViewModel(
            email = "test@example.com",
            password = null
        )

        val exception = assertFailsWith<HttpError> { viewModel.toDomainModel() }
        assertEquals("Missing password field", exception.message)
    }

    @Test
    fun `converts AuthTokenModel to LoginResponseViewModel`() {
        val authTokenModel = AuthTokenModel(
            accessToken = "access-token-123",
            refreshToken = "refresh-token-456"
        )

        val result = authTokenModel.toViewModel()

        assertEquals("access-token-123", result.token)
        assertEquals("refresh-token-456", result.refreshToken)
    }
}