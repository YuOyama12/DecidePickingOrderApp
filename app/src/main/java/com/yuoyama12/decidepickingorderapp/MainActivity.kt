package com.yuoyama12.decidepickingorderapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.yuoyama12.decidepickingorderapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.actionBar)
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