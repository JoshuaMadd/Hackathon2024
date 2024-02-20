package com.joshuamaddelein.hackathon2024

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
                    ItemList(MockUser.getUser().lessen)
                }
            }
        }
    }
}

@Composable
fun ItemCard(les: Les)
{
    Card(modifier = Modifier.padding(top = 20.dp)) {
        Column {
            Text(text = les.naam)
            Text(text = les.lokaal)
        }
    }
}

@Composable
fun ItemList(list: List<Les>)
{
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier.padding(
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