package com.arccorp.recyclerview

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arccorp.recyclerview.model.SuperHero
import kotlinx.coroutines.launch

@Composable
fun SimpleRecyclerView() {
    val myList = listOf("Leonardo", "Luiza", "Carla", "Yu-Gi-Oh!", "Kotlin", "Flutter", "Ktor")
    LazyColumn {
        items(myList) {
            Text(text = "O nome é: $it")
        }
    }
}

@Composable
fun SuperHeroView() {
    val context = LocalContext.current
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(getSuperHero()) {
            ItemHero(superHero = it) {
                Toast.makeText(context, it.superHeroName, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true)
@Composable
fun SuperHeroStickyView() {
    val context = LocalContext.current
    val superHero: Map<String, List<SuperHero>> = getSuperHero().groupBy { it.publisher }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        superHero.forEach{
            (publisher, mySuperHero) ->
            stickyHeader {
                Text(text = publisher)
            }
            items(mySuperHero) {
                ItemHero(superHero = it) {
                    Toast.makeText(context, it.superHeroName, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


@Composable
fun SuperHeroSpecialControlsView() {
    val context = LocalContext.current
    val rvState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn(
            state = rvState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(getSuperHero()) {
                ItemHero(superHero = it) {
                    Toast.makeText(context, it.superHeroName, Toast.LENGTH_SHORT).show()
                }
            }
        }
        val showButton by remember{ derivedStateOf { rvState.firstVisibleItemIndex > 0 } }
        if(showButton){
            Button(onClick = { coroutineScope.launch {
                rvState.animateScrollToItem(0)
            } }, Modifier.padding(8.dp)) {
                Text(text = "Sou um botão legal")
            }
        }
    }
}

@Composable
fun SuperHeroGridView() {
    val context = LocalContext.current
    LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
        items(getSuperHero()) {
            ItemHero(superHero = it) {
                Toast.makeText(context, it.superHeroName, Toast.LENGTH_SHORT).show()
            }
        }
    }, contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp))

}

@Composable
fun ItemHero(superHero: SuperHero, onItemSelected: (SuperHero) -> Unit) {
    Card(
        border = BorderStroke(2.dp, color = Color.Red),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemSelected(superHero) }
    ) {
        Column {
            Image(
                painter = painterResource(id = superHero.photo),
                contentDescription = "Hero avatar",
                Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = superHero.superHeroName,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = superHero.realName,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 12.sp
            )
            Text(
                text = superHero.publisher,
                fontSize = 10.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp)
            )
        }
    }
}

fun getSuperHero(): List<SuperHero> {
    return listOf(
        SuperHero(
            "Homem Aranha", "Peter Paker", "Marvel",
            R.drawable.spiderman
        ),
        SuperHero("Wolverine", "logan", "Marvel", R.drawable.logan),
        SuperHero("Batman", "Bruce Wane", "DC", R.drawable.batman),
        SuperHero("Thor", "Thor", "Marvel", R.drawable.thor),
        SuperHero("flash", "Barry", "DC", R.drawable.flash),
        SuperHero("Green Lantern", "Allan ", "DC", R.drawable.green_lantern),
        SuperHero("Wonder Woman", "Diana", "DC", R.drawable.wonder_woman),
    )
}