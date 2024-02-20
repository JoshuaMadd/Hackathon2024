package com.joshuamaddelein.hackathon2024.ui

import android.app.AlertDialog
import android.content.Context
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.color.ColorProvider
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.joshuamaddelein.hackathon2024.data.model.Les
import com.joshuamaddelein.hackathon2024.util.MockUser

class SimpleWidget: GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent { ContentView() }
    }

    @Composable
    private fun ContentView()
    {

        Column(modifier = GlanceModifier
            .fillMaxSize()
            .background(day= Color.White, night=Color.White),
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "VIVES PLUS WIDGET test",
                style = TextStyle(color = ColorProvider(day=Color.Black, night=Color.Black),
                    fontSize = 16.sp)
            )
            ItemList(list = MockUser.getUser().lessen)
        }
    }

    @Composable
    fun ItemCard(les: Les)
    {
        Column(modifier = GlanceModifier.padding(bottom = 10.dp)) {
            Column(modifier = GlanceModifier.background(Color.Red, Color.Red).height(60.dp).fillMaxWidth().cornerRadius(10.dp)) {
                Text(text = les.naam)
                Text(text = les.lokaal)
                Text(text = les.datum.toString())
            }
        }


    }

    @Composable
    fun ItemList(list: List<Les>)
    {
        LazyColumn(
            modifier = GlanceModifier.padding(top = 5.dp, bottom = 5.dp).height(200.dp).padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp,
                top = 20.dp
            )
        )
        {
            items(list) { item ->
//                Text(text = item.naam, style = TextStyle(color = androidx.glance.unit.ColorProvider(Color.White)))
                ItemCard(les = item)
                Spacer(modifier = GlanceModifier.height(10.dp))
            }
        }
    }
}