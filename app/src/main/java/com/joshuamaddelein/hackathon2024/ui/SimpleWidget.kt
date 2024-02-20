package com.joshuamaddelein.hackathon2024.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import com.joshuamaddelein.hackathon2024.Greeting
import com.joshuamaddelein.hackathon2024.MainActivity
import com.joshuamaddelein.hackathon2024.R
import com.joshuamaddelein.hackathon2024.data.model.Les
import com.joshuamaddelein.hackathon2024.ui.theme.Hackathon2024Theme
import com.joshuamaddelein.hackathon2024.util.MockUser

class SimpleWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent { ContentView() }
    }

    companion object {
        private val SMALL_RECTANGLE = DpSize(50.dp, 100.dp)
        private val MEDIUM_RECTANGLE = DpSize(50.dp, 200.dp)
        private val LARGE_RECTANGLE = DpSize(50.dp, 300.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(
            SMALL_RECTANGLE,
            MEDIUM_RECTANGLE,
            LARGE_RECTANGLE
        )
    )

    @Composable
    private fun ContentView() {
        val size = LocalSize.current

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(day = Color.White, night = Color.White),
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.Top
        ) {
            if (size.height >= LARGE_RECTANGLE.height) {
                Large_Widget()
            } else if (size.height > SMALL_RECTANGLE.height && size.height <= MEDIUM_RECTANGLE.height) {
                Medium_Widget()
            } else {
                Small_Widget(size)
            }
        }
    }

    @Composable
    fun Large_Widget() {
        ItemListBig(list = MockUser.getUser().lessen)
    }

    @Composable
    fun Medium_Widget() {
        Column(modifier = GlanceModifier) {
            ItemListMedium(list = MockUser.getUser().lessen)

            Image(provider = ImageProvider(R.drawable.qr), contentDescription = null, modifier = GlanceModifier.width(20.dp))
        }
    }

    @Composable
    fun Small_Widget(size: DpSize) {
        ItemListSmall(list = MockUser.getUser().lessen, size = size)
    }

    @Composable
    fun ItemCard(les: Les, size: DpSize?)
    {
        var sizeW = size
        if (sizeW == null) {
            sizeW = DpSize(0.dp,100.dp)
        }
        var padding = 10.dp
        Box(modifier = GlanceModifier.background(Color.Red, Color.Gray).height(sizeW.height-padding).fillMaxWidth().cornerRadius(10.dp).padding(padding).clickable(
            actionStartActivity<MainActivity>())) {
            Column(modifier = GlanceModifier) {
                Text(text = les.naam)
                Text(text = les.lokaal)
                Text(text = les.datum.toString())
            }
        }


    }

    @Composable
    fun ItemListSmall(list: List<Les>, size: DpSize)
    {
        val lazyListState = rememberLazyListState()
        LazyColumn(
        )
        {
            items(list) { item ->
                ItemCard(les = item, size = size)
            }
        }
    }

    @Composable
    fun ItemListMedium(list: List<Les>)
    {
        LazyColumn(
            modifier = GlanceModifier.padding(top = 5.dp, bottom = 5.dp).padding(
                start = 10.dp,
                end = 10.dp
            )
        )
        {
            items(list) { item ->
                ItemCard(les = item, size = null)
            }
        }
    }

    @Composable
    fun ItemListBig(list: List<Les>)
    {
        LazyColumn(
            modifier = GlanceModifier.padding(top = 5.dp, bottom = 5.dp).padding(
                start = 10.dp,
                end = 10.dp,
                bottom = 5.dp,
                top = 10.dp
            )
        )
        {
            items(list) { item ->
                ItemCard(les = item, size = null)
            }
        }
    }
}