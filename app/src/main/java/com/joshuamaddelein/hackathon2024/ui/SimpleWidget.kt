package com.joshuamaddelein.hackathon2024.ui

import android.content.Context
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
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
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.Text
import com.joshuamaddelein.hackathon2024.ParkingQrScreen
import com.joshuamaddelein.hackathon2024.R
import com.joshuamaddelein.hackathon2024.data.model.Les
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
        ItemListNormal(list = MockUser.getUser().lessen)
    }

    @Composable
    fun Medium_Widget() {
        Box(modifier = GlanceModifier.fillMaxSize(), Alignment(Alignment.End, Alignment.Bottom)) {
            ItemListNormal(list = MockUser.getUser().lessen)
            Box (contentAlignment = Alignment(Alignment.Start, Alignment.Top), modifier = GlanceModifier.padding(0.dp, 0.dp, 10.dp, 10.dp)) {
                Box(modifier = GlanceModifier.clickable(actionStartActivity<ParkingQrScreen>()).size(50.dp).padding(5.dp).background(Color.Red, Color.Red).cornerRadius(20.dp), Alignment(Alignment.CenterHorizontally, Alignment.CenterVertically)) {
                    Image(provider = ImageProvider(R.drawable.qr), contentDescription = null, modifier = GlanceModifier.width(20.dp))
                }
            }
        }
    }

    @Composable
    fun Small_Widget(size: DpSize) {
        ItemListSmall(list = MockUser.getUser().lessen, size = size)
    }

    @Composable
    fun ItemCardSmall(les: Les, size: DpSize?)
    {
        var sizeW = size
        if (sizeW == null) {
            sizeW = DpSize(0.dp,100.dp)
        }
        var padding = 10.dp
        Box(modifier = GlanceModifier.background(Color.Red, Color.Gray).height(sizeW.height-padding).fillMaxWidth().cornerRadius(10.dp).padding(padding)/*.clickable(
            actionStartActivity<MainActivity>())*/) {
            Column(modifier = GlanceModifier) {
                Text(text = les.naam)
                Text(text = les.lokaal)
                Text(text = les.datum.toString())
            }
        }
    }

    @Composable
    fun ItemCardNormal(les: Les, size: DpSize?)
    {
        var sizeW = size
        if (sizeW == null) {
            sizeW = DpSize(0.dp,100.dp)
        }
        var padding = 10.dp
        Box (modifier = GlanceModifier.padding(0.dp,5.dp)) {
            Box (modifier = GlanceModifier.background(Color.Gray, Color.Gray).fillMaxWidth().padding(3.dp).cornerRadius(13.dp), contentAlignment = Alignment(Alignment.CenterHorizontally, Alignment.CenterVertically)) {
                Box(modifier = GlanceModifier.background(Color.White, Color.DarkGray).fillMaxWidth().height(sizeW.height-padding).cornerRadius(10.dp).padding(padding)/*.clickable(
            actionStartActivity<MainActivity>())*/) {
                    Column(modifier = GlanceModifier) {
                        Text(text = les.naam)
                        Text(text = les.lokaal)
                        Text(text = les.datum.toString())
                    }
                }
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
                ItemCardSmall(les = item, size = size)
            }
        }
    }

    @Composable
    fun ItemListNormal(list: List<Les>)
    {
        LazyColumn(
            modifier = GlanceModifier.padding(top = 5.dp, bottom = 5.dp).padding(
                start = 10.dp,
                end = 10.dp
            )
        )
        {
            items(list) { item ->
                ItemCardNormal(les = item, size = null)
            }
        }
    }
}

