package ru.yandex.loginapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var loginViewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginViewModel = LoginViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with empty fields sets EmptyFieldsError`() = runTest {
        loginViewModel.login("", "")
        assertEquals(
            LoginScreenState.EmptyFieldsError,
            loginViewModel.state.value
        )
    }

    @Test
    fun `login with invalid email sets EmailValidationError`() = runTest {
        loginViewModel.login("testya.ru", "pass")
        assertEquals(
            LoginScreenState.EmailValidationError,
            loginViewModel.state.value
        )
    }

    @Test
    fun `login with valid data sets Loading`() = runTest {
        loginViewModel.login("test@ya.ru", "pass")
        testDispatcher.scheduler.runCurrent()
        assertEquals(
            LoginScreenState.Loading,
            loginViewModel.state.value
        )
    }

    @Test
    fun `login with valid data sets Loading then Success`() = runTest {
        loginViewModel.login("test@ya.ru", "pass")
        testDispatcher.scheduler.runCurrent()
        assertEquals(
            LoginScreenState.Loading,
            loginViewModel.state.value
        )

        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(
            LoginScreenState.Success,
            loginViewModel.state.value
        )
    }
}