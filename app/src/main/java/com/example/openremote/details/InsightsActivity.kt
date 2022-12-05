package com.example.openremote.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openremote.R
import com.example.openremote.adapter.InsightAdapter
import com.example.openremote.databinding.ActivityInsightsBinding
import com.example.openremote.model.InsightModel

class InsightsActivity : AppCompatActivity() {

    private var _binding : ActivityInsightsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInsightsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val data = ArrayList<InsightModel>()
        data.add(InsightModel("CHARGER 1 MARKTHAL - POWER", R.drawable.img_5, R.drawable.ic_baseline_local_gas_station_24, "17 kW", "-8"))
        data.add(InsightModel("CHARGER 2 MARKTHAL - POWER", R.drawable.img_6, R.drawable.ic_baseline_local_gas_station_24, "10 kW", "-15"))
        data.add(InsightModel("WEATHER - TEMPERATURE", R.drawable.img_7, R.drawable.ic_baseline_cloud_24, "32.12", "-20"))
        data.add(InsightModel("WEATHER - TEMPERATURE 2",R.drawable.img_8, R.drawable.ic_baseline_cloud_24, "25.64", "-18"))
        val adapter = InsightAdapter(data)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}