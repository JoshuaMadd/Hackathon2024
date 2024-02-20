package com.joshuamaddelein.hackathon2024

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.layout.padding
import com.joshuamaddelein.hackathon2024.data.model.Les
import com.joshuamaddelein.hackathon2024.ui.theme.Hackathon2024Theme
import com.joshuamaddelein.hackathon2024.util.MockUser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hackathon2024Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ItemList(MockUser.getLessen())
                }
            }
        }
    }
}

@Composable
fun ItemList(list: List<Les>)
{
    LazyColumn(
        modifier = GlanceModifier.padding(
            start = 20.dp,
            end = 20.dp,
            bottom = 60.dp,
            top = 20.dp
        )
    )
    {
        items(list) { item ->
            ItemCard(item)
        }
    }
}

@Composable
fun ItemCard(les : Les)
{
    Card(modifier = Modifier.padding(top = 10.dp))
    {
        les.naam
        les.datum
        les.lokaal
    }
}