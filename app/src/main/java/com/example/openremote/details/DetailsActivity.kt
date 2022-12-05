package com.example.openremote.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.openremote.R
import com.example.openremote.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}