package com.yuoyama12.decidepickingorderapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yuoyama12.decidepickingorderapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.actionBar)

        // edge-to-edge対応のため、画面全体にpaddingを設ける。
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            insets
        }
    }

    companion object {
        fun getActionBar(activity: Activity): Toolbar =
            activity.findViewById(R.id.action_bar)

        fun createNavigationIcon(toolbar: Toolbar, onClick: () -> Unit) {
            toolbar.apply {
                setNavigationIcon(R.drawable.ic_arrow_back)
                setNavigationContentDescription(R.string.action_bar_navigation_icon_back_desc)
                setNavigationOnClickListener { onClick.invoke() }
            }
        }

    }

}