package com.pasukanlangit.id.ui_herolist.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.codingwithmitch.hero_domain.HeroAttribute
import com.pasukanlangit.id.core.FilterOrder
import com.pasukanlangit.id.hero_domain.HeroFilter
import com.pasukanlangit.id.ui_herolist.R
import com.pasukanlangit.id.ui_herolist.ui.test.*

@ExperimentalAnimationApi
@Composable
fun HeroListFilter(
    onCloseDialog: () -> Unit,
    onUpdateHeroFilter: (HeroFilter) -> Unit,
    onUpdateAttributeFilter: (HeroAttribute) -> Unit,
    heroFilter: HeroFilter,
    attributeDefault: HeroAttribute
) {
    AlertDialog(
        modifier = Modifier
            .padding(16.dp)
            .testTag(TAG_HERO_FILTER_DIALOG),
        onDismissRequest = {
            onCloseDialog()
        },
        title = {
            Text(
                text = "Filter",
                style = MaterialTheme.typography.h2
            )
        },
        text = {
            LazyColumn {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))

                        HeroFilterSelector(
                            isEnabled = heroFilter is HeroFilter.Hero,
                            filterOnHero = {
                                onUpdateHeroFilter(HeroFilter.Hero())
                            },
                            orderDesc = {
                                onUpdateHeroFilter(
                                    HeroFilter.Hero(order = FilterOrder.Descending)
                                )
                            },
                            orderAsc = {
                                onUpdateHeroFilter(
                                    HeroFilter.Hero(order = FilterOrder.Ascending)
                                )
                            },
                            order = if(heroFilter is HeroFilter.Hero) heroFilter.order else null
                        )

                        ProWinsFilterSelector(
                            isEnabled = heroFilter is HeroFilter.ProWins,
                            filterOnProWins = {
                                onUpdateHeroFilter(HeroFilter.ProWins())
                            },
                            orderDesc = {
                                onUpdateHeroFilter(
                                    HeroFilter.ProWins(order = FilterOrder.Descending)
                                )
                            },
                            orderAsc = {
                                onUpdateHeroFilter(
                                    HeroFilter.ProWins(order = FilterOrder.Ascending)
                                )
                            },
                            order = if(heroFilter is HeroFilter.ProWins) heroFilter.order else null
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                            thickness = 1.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Primary Attribute Filter
                        PrimaryAttrFilterSelector(
                            removeFilterOnPrimaryAttr = {
                                onUpdateAttributeFilter(
                                    HeroAttribute.Unknown
                                )
                            },
                            attribute = attributeDefault,
                            onFilterStr = {
                                onUpdateAttributeFilter(
                                    HeroAttribute.Strength
                                )
                            },
                            onFilterAgi = {
                                onUpdateAttributeFilter(
                                    HeroAttribute.Agility
                                )
                            },
                            onFilterInt = {
                                onUpdateAttributeFilter(
                                    HeroAttribute.Intelligence
                                )
                            },
                        )
                    }
                }
            }
        },
        buttons = {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Row( // make the icon larger so it's easier to click
                    modifier = Modifier
                        .align(Alignment.End)
                        .testTag(TAG_HERO_FILTER_DIALOG_DONE)
                        .clickable {
                            onCloseDialog()
                        }
                    ,
                ){
                    Icon(
                        modifier = Modifier
                            .padding(10.dp)
                        ,
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                        tint = Color(0xFF009a34)
                    )
                }

            }
        }
        //Todo Complete this steps
    )
}


/**
 * @param filterOnHero: Set the HeroFilter to 'Hero'
 * @param isEnabled: Is the Hero filter the selected 'HeroFilter'
 * @param order: Ascending or Descending?
 * @param orderDesc: Set the order to descending.
 * @param orderAsc: Set the order to ascending.
 */

@ExperimentalAnimationApi
@Composable
fun HeroFilterSelector(
    isEnabled: Boolean,
    order: FilterOrder?= null,
    filterOnHero: () -> Unit,
    orderDesc: () -> Unit,
    orderAsc: () -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag(TAG_HERO_FILTER_HERO_CHECKBOX)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,// disable the highlight
                    enabled = true,
                    onClick = {
                        filterOnHero()
                    }
                )
        ){
            Checkbox(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically),
                checked = isEnabled,
                onCheckedChange = {
                    filterOnHero()
                },
                colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
            )
            Text(
                text = HeroFilter.Hero().uiValue,
                style = MaterialTheme.typography.h3
            )
        }
        OrderSelector(
            isEnabled = isEnabled,
            isDescSelected = isEnabled && order is FilterOrder.Descending,
            isAscSelected = isEnabled && order is FilterOrder.Ascending,
            descString = "z -> a",
            ascString = "a -> z",
            onUpdateHeroFilterDesc = {
                orderDesc()
            },
            onUpdateHeroFilterAsc = {
                orderAsc()
            }
        )
        
    }

}

/**
 * @param filterOnProWins: Set the HeroFilter to 'ProWins'
 * @param isEnabled: Is the ProWins filter the selected 'HeroFilter'
 * @param order: Ascending or Descending?
 * @param orderDesc: Set the order to descending.
 * @param orderAsc: Set the order to ascending.
 */
@ExperimentalAnimationApi
@Composable
fun ProWinsFilterSelector(
    filterOnProWins: () -> Unit,
    orderDesc: () -> Unit,
    orderAsc: () -> Unit,
    isEnabled: Boolean,
    order: FilterOrder?
){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag(TAG_HERO_FILTER_HERO_CHECKBOX)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,// disable the highlight
                    enabled = true,
                    onClick = {
                        filterOnProWins()
                    }
                )
        ) {
            Checkbox(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically),
                checked = isEnabled,
                onCheckedChange = {
                    filterOnProWins()
                },
                colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
            )
            Text(
                text = HeroFilter.ProWins().uiValue,
                style = MaterialTheme.typography.h3
            )
        }
        OrderSelector(
            descString = "100% - 0%",
            ascString = "0% - 100%",
            isEnabled = isEnabled,
            isDescSelected = isEnabled && order is FilterOrder.Descending,
            isAscSelected = isEnabled && order is FilterOrder.Ascending,
            onUpdateHeroFilterDesc = {
                orderDesc()
            },
            onUpdateHeroFilterAsc = {
                orderAsc()
            },
        )
    }
}

/**
 * @param removeFilterOnPrimaryAttr: remove hero filter
 * @param attribute: Is the current attribute Strength, Agility or Intelligence?
 * @param onFilterStr: Set the order to Strength.
 * @param onFilterAgi: Set the order to Agility.
 * @param onFilterInt: Set the order to Intelligence.
 */
@ExperimentalAnimationApi
@Composable
fun PrimaryAttrFilterSelector(
    removeFilterOnPrimaryAttr: () -> Unit,
    attribute: HeroAttribute,
    onFilterStr: () -> Unit,
    onFilterAgi: () -> Unit,
    onFilterInt: () -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth()
            ,
        ){
            Text(
                text = stringResource(R.string.primary_attribute),
                style = MaterialTheme.typography.h3,
            )
        }

        PrimaryAttrSelector(
            isStr = attribute is HeroAttribute.Strength,
            isAgi = attribute is HeroAttribute.Agility,
            isInt = attribute is HeroAttribute.Intelligence,
            onUpdateHeroFilterStr = {
                onFilterStr()
            },
            onUpdateHeroFilterAgi = {
                onFilterAgi()
            },
            onUpdateHeroFilterInt = {
                onFilterInt()
            },
            onRemoveAttributeFilter = {
                removeFilterOnPrimaryAttr()
            }
        )
    }
}

/**
 * @param isStr: Is the selected attribute strength?
 * @param isAgi: Is the selected attribute Agility?
 * @param isInt: Is the selected attribute Intelligence?
 * @param onUpdateHeroFilterStr: Update the filter to Strength
 * @param onUpdateHeroFilterAgi: Update the filter to Agility
 * @param onUpdateHeroFilterInt: Update the filter to Intelligence
 */
@ExperimentalAnimationApi
@Composable
fun PrimaryAttrSelector(
    isStr: Boolean = false,
    isAgi: Boolean = false,
    isInt: Boolean = false,
    onUpdateHeroFilterStr: () -> Unit,
    onUpdateHeroFilterAgi: () -> Unit,
    onUpdateHeroFilterInt: () -> Unit,
    onRemoveAttributeFilter: () -> Unit,
){
    // Strength
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, bottom = 8.dp)
            .testTag(TAG_HERO_FILTER_STENGTH_CHECKBOX)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null, // disable the highlight
                onClick = {
                    onUpdateHeroFilterStr()
                },
            )
        ,
    ){
        Checkbox(
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically)
            ,
            checked = isStr,
            onCheckedChange = {
                onUpdateHeroFilterStr()
            },
            colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
        )
        Text(
            text = HeroAttribute.Strength.uiValue,
            style = MaterialTheme.typography.body1,
        )
    }

    // Agility
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, bottom = 8.dp)
            .testTag(TAG_HERO_FILTER_AGILITY_CHECKBOX)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null, // disable the highlight
                onClick = {
                    onUpdateHeroFilterAgi()
                },
            )
        ,
    ){
        Checkbox(
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically)
            ,
            checked = isAgi,
            onCheckedChange = {
                onUpdateHeroFilterAgi()
            },
            colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
        )
        Text(
            text = HeroAttribute.Agility.uiValue,
            style = MaterialTheme.typography.body1,
        )
    }

    // Intelligence
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, bottom = 8.dp)
            .testTag(TAG_HERO_FILTER_INT_CHECKBOX)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null, // disable the highlight
                onClick = {
                    onUpdateHeroFilterInt()
                },
            )
        ,
    ){
        Checkbox(
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically)
            ,
            checked = isInt,
            onCheckedChange = {
                onUpdateHeroFilterInt()
            },
            colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
        )
        Text(
            text = HeroAttribute.Intelligence.uiValue,
            style = MaterialTheme.typography.body1,
        )
    }

    // No Filter on Attribute
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, bottom = 8.dp)
            .testTag(TAG_HERO_FILTER_UNKNOWN_CHECKBOX)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null, // disable the highlight
                onClick = {
                    onRemoveAttributeFilter()
                },
            )
        ,
    ){
        Checkbox(
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically)
            ,
            checked = !isStr && !isAgi && !isInt,
            onCheckedChange = {
                onRemoveAttributeFilter()
            },
            colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
        )
        Text(
            text = stringResource(R.string.none),
            style = MaterialTheme.typography.body1,
        )
    }
}