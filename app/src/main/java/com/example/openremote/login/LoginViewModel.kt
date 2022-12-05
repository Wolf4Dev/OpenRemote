package com.example.openremote.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class LoginViewModel: ViewModel() {
    private val loginRepository = LoginRepository()

    private val _loginUIState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val loginState: StateFlow<LoginUiState> = _loginUIState

    sealed class LoginUiState {
        class Success(val successMessage: String): LoginUiState()
        class Error(val errorMessage: String): LoginUiState()
        object Loading: LoginUiState()
        object Empty: LoginUiState()
    }

    fun login(
        username: String,
        password: String
    ) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            _loginUIState.value = LoginUiState.Loading
            delay(2000L)
            loginRepository.login(username, password, {
                _loginUIState.value = LoginUiState.Success(it)
            }, {
                _loginUIState.value = LoginUiState.Error(it)
            })
        }
    }
}