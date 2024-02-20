package com.joshuamaddelein.hackathon2024.ui

import android.content.Context
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.background
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
            .background(day= Color.Black, night=Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemList(list = MockUser.getUser().lessen)
            Spacer(modifier = GlanceModifier.height(10.dp))
            Text(text = "VIVES PLUS WIDGET test",
                style = TextStyle(color = ColorProvider(day=Color.White, night=Color.Black),
                    fontSize = 16.sp)
            )
        }
    }

    @Composable
    fun ItemList(list: List<Les>)
    {
        LazyColumn()
        {
            items(list)
            {
                les ->
                ItemCard(les)
            }
        }
    }

    @Composable
    fun ItemCard(les :Les)
    {
        Card()
        {
            les.naam
            les.datum
            les.lokaal
        }
    }
}