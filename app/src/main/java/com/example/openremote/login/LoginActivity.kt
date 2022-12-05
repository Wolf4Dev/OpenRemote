package com.example.openremote.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.openremote.R
import com.example.openremote.databinding.ActivityLoginBinding
import com.example.openremote.home.HomeActivity
import com.example.openremote.request.RequestActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    @OptIn(ExperimentalCoroutinesApi::class)
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        login()
        collectFlow()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun login() {
        binding.signUp.setOnClickListener {
            viewModel.login(binding.userName.text.toString(), binding.password.text.toString())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { result->
                when(result) {
                    is LoginViewModel.LoginUiState.Success -> {
                        Toast.makeText(this@LoginActivity, result.successMessage, Toast.LENGTH_SHORT).show()
                        binding.progressBar.isVisible = false
                        startActivity(Intent(this@LoginActivity, RequestActivity::class.java))
                        finish()
                    }
                    is LoginViewModel.LoginUiState.Error -> {
                        Toast.makeText(this@LoginActivity, result.errorMessage, Toast.LENGTH_SHORT).show()
                        binding.progressBar.isVisible = false
                    }
                    is LoginViewModel.LoginUiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}