package com.pasukanlangit.id.ui_herolist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.pasukanlangit.id.hero_domain.Hero
import com.pasukanlangit.id.ui_herolist.ui.test.TAG_HERO_NAME
import com.pasukanlangit.id.ui_herolist.ui.test.TAG_HERO_PRIMARY_ATTRIBUTE
import kotlin.math.round
import com.pasukanlangit.id.ui_herolist.R

@ExperimentalCoilApi
@Composable
fun HeroItem(
    hero: Hero,
    onSelectHero: (Int) -> Unit,
    imageLoader: ImageLoader
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(MaterialTheme.colors.surface)
            .clickable {
                onSelectHero(hero.id)
            },
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            val painter = rememberImagePainter(
                data = hero.img,
                imageLoader = imageLoader,
                builder = {
                    placeholder(if(isSystemInDarkTheme()) R.drawable.black_background else R.drawable.white_background)
                }
            )
            Image(
                modifier = Modifier
                    .width(120.dp)
                    .height(70.dp)
                    .background(Color.LightGray),
                painter = painter,
                contentDescription = hero.localizedName,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(.8f) //fill 80% of remaining width
                    .padding(start = 12.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .testTag(TAG_HERO_NAME),
                    text = hero.localizedName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .testTag(TAG_HERO_PRIMARY_ATTRIBUTE),
                    text = hero.primaryAttribute.uiValue,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth() // fill the rest of width (100%-80% = 20%)
                    .padding(end = 12.dp),
                horizontalAlignment = Alignment.End
            ) {
                val proWR: Int = round(hero.proWins.toDouble()/ hero.proPick.toDouble() * 100).toInt()
                Text(
                    text = "${proWR}%",
                    style = MaterialTheme.typography.caption,
                    color = if(proWR > 50) Color(0xFF009a34) else MaterialTheme.colors.error,
                )
            }
        }
    }
}