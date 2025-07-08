package com.yuoyama12.decidepickingorderapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
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

        // edge-to-edge対応のため、toolBarがあるtop以外の画面にpaddingを設ける。
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        // toolBarに色がつけられないのでtoolBar代替用のViewを入れて
        // スペースとして設ける。
        addToolBarPadding()
    }

    private fun addToolBarPadding() {
        val spacePadding = getToolBarSpacePaddingView(this)
        ViewCompat.setOnApplyWindowInsetsListener(spacePadding) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams {
                height = systemBars.top
            }
            insets
        }
    }

    companion object {
        fun getActionBar(activity: Activity): Toolbar =
            activity.findViewById(R.id.action_bar)

        fun getToolBarSpacePaddingView(activity: Activity): View =
            activity.findViewById(R.id.space_padding)

        fun createNavigationIcon(toolbar: Toolbar, onClick: () -> Unit) {
            toolbar.apply {
                setNavigationIcon(R.drawable.ic_arrow_back)
                setNavigationContentDescription(R.string.action_bar_navigation_icon_back_desc)
                setNavigationOnClickListener { onClick.invoke() }
            }
        }

    }

}