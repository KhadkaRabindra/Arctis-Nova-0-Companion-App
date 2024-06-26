package com.steelseries.arctisnova0companionapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.arranllc.arctisnova0companionapp.R
import com.arranllc.arctisnova0companionapp.databinding.ActivityMainBinding
import com.steelseries.arctisnova0companionapp.devices.ArctisNova0

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val arctisNovaViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            volumeSeekbar.max = 10
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                volumeSeekbar.min = 0
            }
        }

        binding.volumeSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    arctisNovaViewModel.setMicVolume(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //observers
        arctisNovaViewModel.firmwareVersion.observe(this) { fwVersion ->
            binding.firmwareVersionTextView.text = getString(R.string.headset_firmware_version) + fwVersion
        }

        arctisNovaViewModel.micVolume.observe(this) { volume ->
            binding.volumeSeekbar.progress = volume
        }
    }
}