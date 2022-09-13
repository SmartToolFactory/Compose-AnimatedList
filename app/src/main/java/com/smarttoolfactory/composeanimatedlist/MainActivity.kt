@file:OptIn(ExperimentalPagerApi::class)

package com.smarttoolfactory.composeanimatedlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.pager.ExperimentalPagerApi
import com.smarttoolfactory.composeanimatedlist.demo.AnimatedInfiniteListDemo
import com.smarttoolfactory.composeanimatedlist.demo.AnimatedInfiniteListDemo2
import com.smarttoolfactory.composeanimatedlist.ui.theme.ComposeAnimatedListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAnimatedListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.DarkGray)
                    ) {
                        PagerContent(
                            content = mapOf<String, @Composable () -> Unit>(
                                "InfiniteList" to { AnimatedInfiniteListDemo() },
                                "InfiniteList Params" to { AnimatedInfiniteListDemo2() }
                            )
                        )
                    }
                }
            }
        }
    }
}
