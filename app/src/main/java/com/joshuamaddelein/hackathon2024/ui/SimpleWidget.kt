package com.joshuamaddelein.hackathon2024.ui

import android.content.Context
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.background
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import com.joshuamaddelein.hackathon2024.data.model.Les

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
                .background(day = Color.Black, night = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //ItemList(list = MockUser.getUser().lessen)
//            Spacer(modifier = GlanceModifier.height(10.dp))
//            Text(text = "VIVES PLUS WIDGET test",
//                style = TextStyle(color = ColorProvider(day=Color.White, night=Color.Black),
//                    fontSize = 16.sp)
//            )

            if (size.height >= LARGE_RECTANGLE.height) {
                Large_Widget()
            } else if (size.height > SMALL_RECTANGLE.height && size.height <= MEDIUM_RECTANGLE.height) {
                Medium_Widget()
            } else {
                Small_Widget()
            }
        }
    }

    @Composable
    fun Large_Widget() {
        Text(text = "Big widget")
    }

    @Composable
    fun Medium_Widget() {
        Text(text = "Medium widget")
    }

    @Composable
    fun Small_Widget() {
        Text(text = "Small widget")
    }

    @Composable
    fun ItemList(list: List<Les>) {
        LazyColumn()
        {
            items(list)
            { les ->
                ItemCard(les)
            }
        }
    }

    @Composable
    fun ItemCard(les: Les) {
        Card()
        {
            les.naam
            les.datum
            les.lokaal
        }
    }
}