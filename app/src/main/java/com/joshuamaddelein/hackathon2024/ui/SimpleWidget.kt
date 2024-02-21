package com.joshuamaddelein.hackathon2024.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.ExperimentalGlanceRemoteViewsApi
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.joshuamaddelein.hackathon2024.MainActivity
import com.joshuamaddelein.hackathon2024.ParkingQrScreen
import com.joshuamaddelein.hackathon2024.R
import com.joshuamaddelein.hackathon2024.data.model.Les
import com.joshuamaddelein.hackathon2024.util.MockUser

import java.time.LocalDate

import java.time.format.DateTimeFormatter

class SimpleWidget() : GlanceAppWidget() {
    
    var lastDate = LocalDate.now().minusDays(1)

    //override val stateDefinition = WidgetStateDefinition


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

        var lessen = MockUser.getLessen()

        Log.d("Myapp", lessen.toString())

            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(day = Color.White, night = Color.White),
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.Top
            ) {
                if (size.height >= LARGE_RECTANGLE.height) {
                    Large_Widget(lessen)
                } else if (size.height > SMALL_RECTANGLE.height && size.height <= MEDIUM_RECTANGLE.height) {
                    Medium_Widget(lessen)
                } else {
                    Small_Widget(size, lessen)
                }
            }
        }


    /*@Composable
    fun Large_Widget() {
        ItemListMedium(list = MockUser.getUser().lessen)
    }*/

    @Composable
    fun Large_Widget(lessen: List<Les>) {
        Box(modifier = GlanceModifier.fillMaxSize(), Alignment(Alignment.End, Alignment.Bottom)) {
            ItemListMedium(list = lessen)
            Box (contentAlignment = Alignment(Alignment.Start, Alignment.Top), modifier = GlanceModifier.padding(0.dp, 0.dp, 10.dp, 10.dp)) {

                    Box(modifier = GlanceModifier
                        .clickable(actionStartActivity<ParkingQrScreen>())
                        .size(50.dp).padding(5.dp).background(Color.Red, Color.Red).cornerRadius(20.dp), Alignment(Alignment.CenterHorizontally, Alignment.CenterVertically)) {
                        Image(provider = ImageProvider(R.drawable.qr_white), contentDescription = null, modifier = GlanceModifier.width(20.dp))
                    }
            }
            Box (contentAlignment = Alignment(Alignment.Start, Alignment.Top), modifier = GlanceModifier.padding(0.dp, 0.dp, 70.dp, 10.dp)) {
                Box(modifier = GlanceModifier
                    .clickable(actionStartActivity<MainActivity>())
                    .size(50.dp).padding(5.dp).background(Color.Red, Color.Red).cornerRadius(20.dp), Alignment(Alignment.CenterHorizontally, Alignment.CenterVertically)) {
                    Image(provider = ImageProvider(R.drawable.baseline_person_outline_24), contentDescription = null, modifier = GlanceModifier.width(20.dp))
                }
            }
        }
    }

    @Composable
    fun Medium_Widget(lessen: List<Les>) {
        Box(modifier = GlanceModifier.fillMaxSize(), Alignment(Alignment.End, Alignment.Bottom)) {
            ItemListMedium(list = lessen)
            Box (contentAlignment = Alignment(Alignment.Start, Alignment.Top), modifier = GlanceModifier.padding(0.dp, 0.dp, 10.dp, 10.dp)) {
                Box(modifier = GlanceModifier
                    .clickable(actionStartActivity<MainActivity>())
                    .size(50.dp).padding(5.dp).background(Color.Red, Color.Red).cornerRadius(20.dp), Alignment(Alignment.CenterHorizontally, Alignment.CenterVertically)) {
                    Image(provider = ImageProvider(R.drawable.qr_white), contentDescription = null, modifier = GlanceModifier.width(20.dp))
                }
            }
        }
    }

    @Composable
    fun Small_Widget(size: DpSize, lessen: List<Les>) {
        ItemListSmall(list = lessen, size = size)
    }

    @Composable
    fun ItemCardSmall(les: Les, size: DpSize?)
    {
        var sizeW = size
        if (sizeW == null) {
            sizeW = DpSize(0.dp,100.dp)
        }
        var padding = 10.dp
        Box(modifier = GlanceModifier.background(Color.Red, Color.Gray).height(sizeW.height).fillMaxWidth().cornerRadius(10.dp).padding(padding)/*.clickable(
            actionStartActivity<MainActivity>())*/, contentAlignment = Alignment(Alignment.Start, Alignment.CenterVertically)
        ) {
            Column {
                Text(text = les.naam, style = TextStyle(color = ColorProvider(Color.White), fontWeight = FontWeight.Bold))
                Row {
                    Column {
                        Text(text = les.lokaal, style = TextStyle(color = ColorProvider(Color.White)))
                        Text(text = les.datum.toString(), style = TextStyle(color = ColorProvider(Color.White)))
                    }
                    Row (modifier = GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Image(provider = ImageProvider(R.drawable.time_stamp_circles_white), contentDescription = "", modifier = GlanceModifier.size(30.dp))
                        Column {
                            Text(text = les.startTijd.toString(), style = TextStyle(color = ColorProvider(Color.White)))
                            Text(text = les.eindTijd.toString(), style = TextStyle(color = ColorProvider(Color.White)))
                        }
                    }
                }

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
                    Column {
                        Text(text = les.naam, style = TextStyle(fontWeight = FontWeight.Bold))
                        Row {
                            Column(modifier = GlanceModifier) {
                                Text(text = les.lokaal)
                                Text(text = les.datum.toString())
                            }
                            Row (modifier = GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Image(provider = ImageProvider(R.drawable.time_stamp_circles), contentDescription = "", modifier = GlanceModifier.size(30.dp))
                                Column {
                                    Text(text = les.startTijd.toString())
                                    Text(text = les.eindTijd.toString())
                                }
                            }
                    }

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
                Column {
                    if (!item.datum.equals(lastDate)) {
                        DateItem(date = item.datum)
                        lastDate = item.datum
                    }
                    ItemCardNormal(les = item, size = null)
                }

            }
        }
    }
    @Composable
    fun DateItem(date: LocalDate) {
        Box (modifier = GlanceModifier.padding(5.dp)) {
            Column {
                Box (modifier = GlanceModifier.padding(bottom = 2.dp, top = 2.dp)) {
                    if (date.equals(LocalDate.now())){
                        Text(text = "Vandaag:", style = TextStyle(fontWeight = FontWeight.Bold))
                    } else if (date.equals(LocalDate.now().plusDays(1))) {
                        Text(text = "Morgen:", style = TextStyle(fontWeight = FontWeight.Bold))
                    } else {
                        Text(text = date.format(DateTimeFormatter.ofPattern("dd MMM")), style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                }
                Box (modifier = GlanceModifier.height(1.dp).fillMaxWidth().background(Color.Gray, Color.White)) {}
            }

        }

    }

}