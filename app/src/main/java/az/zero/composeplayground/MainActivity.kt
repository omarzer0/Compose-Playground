package az.zero.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import az.zero.composeplayground.ui.common_composables.TextWithIconTab
import az.zero.composeplayground.ui.theme.ComposePlayGroundTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@ExperimentalPagerApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tabData = listOf(
            "MUSIC" to Icons.Filled.Home,
            "MARKET" to Icons.Filled.ShoppingCart,
            "FILMS" to Icons.Filled.AccountBox,
            "BOOKS" to Icons.Filled.Settings,
        )
        val listOfIcons = listOf(
            Icons.Filled.Home,
            Icons.Filled.ShoppingCart,
            Icons.Filled.AccountBox,
            Icons.Filled.Settings,
        )

        val textList = listOf("1", "2", "3")

        setContent {
            ComposePlayGroundTheme {
                Surface {
                    TextWithIconTab(
                        listOfPairOfTabNamesWithIcons = tabData,
                        modifier = Modifier.background(Color.Green),
                        tabHostModifier = Modifier.height(120.dp),
                        tabHostBackgroundColor = Color.Blue,
                        tabSelectorHeight = 5.dp,
                        tabSelectorColor = Color.Red,
                        selectedContentColor = Color.Red,
                        unSelectedContentColor = Color.Yellow,
                        indicatorContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(20.dp)
                                        .background(Color.Black)
                                )
                            }
                        }
                    ) {
                        AnimationHorizontalPager()
                    }

                    
                }
            }
        }
    }
}


@ExperimentalPagerApi
@Composable
fun AnimationHorizontalPager(
    modifier: Modifier = Modifier
) {
    HorizontalPager(count = 4) { page ->
        Box(
            modifier
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                    // We animate the scaleX + scaleY, between 85% and 100%
                    lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
//                LazyColumn(modifier = Modifier.padding(20.dp)) {
//                    items(20) {
//                        Text(
//                            "Text", modifier = Modifier
//                                .fillMaxWidth()
//                                .background(Color.Green)
//                                .padding(24.dp),
//                            color = Color.White
//                        )
//                        Spacer(modifier = Modifier.height(10.dp))
//                    }
//                }

            }


        }
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs() {
    val coroutineScope = rememberCoroutineScope()

    val tabTitles = listOf("Hello", "There", "World")
    val pagerState = rememberPagerState()


    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = index == pagerState.currentPage, // 4.
                    text = { Text(text = title) },
                    selectedContentColor = Color.Red,
                    unselectedContentColor = Color.Black,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ) // 5.
            }
        }

        // Display 10 items
        HorizontalPager(
            count = tabTitles.size,
            state = pagerState
        ) { page ->
            // Our page content
            Text(
                text = "Page: $page",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun NestedScroll() {
    val gradient = Brush.verticalGradient(0f to Color.Gray, 1000f to Color.White)
    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .verticalScroll(rememberScrollState())
            .padding(32.dp)
    ) {
        Column {
            repeat(6) {
                Box(
                    modifier = Modifier
                        .height(128.dp)
                ) {
                    Text(
                        "Scroll here",
                        modifier = Modifier
                            .border(12.dp, Color.DarkGray)
                            .background(brush = gradient)
                            .padding(24.dp)
                            .height(150.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ScrollableSample() {
    // actual composable state
    var offset by remember { mutableStateOf(0f) }
    Box(
        Modifier
            .size(150.dp)
            .scrollable(
                orientation = Orientation.Vertical,
                // Scrollable state: describes how to consume
                // scrolling delta and update offset
                state = rememberScrollableState { delta ->
                    offset += delta
                    delta
                }
            )
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(offset.toString())
    }
}

@Composable
fun ScrollBoxes() {
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .size(250.dp)
            .verticalScroll(rememberScrollState())
    ) {
        repeat(30) {
            Text("Item $it", modifier = Modifier.padding(2.dp))
        }
    }
}


@Composable
private fun ScrollBoxesSmooth() {

    // Smoothly scroll 100px on first composition
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(100) }

    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .size(250.dp)
            .padding(horizontal = 8.dp)
            .verticalScroll(state)
    ) {
        repeat(30) {
            Text("Item $it", modifier = Modifier.padding(2.dp))
        }
    }
}


@Composable
fun SimpleCard() {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        elevation = 10.dp,
        border = BorderStroke(width = 10.dp, color = Color.Red)
    ) {

    }
}