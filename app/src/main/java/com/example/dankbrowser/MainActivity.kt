package com.example.dankbrowser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dankbrowser.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSessionSettings
import org.mozilla.geckoview.GeckoView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}