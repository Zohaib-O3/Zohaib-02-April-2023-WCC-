package com.zohaib.coding.challenge

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zohaib.coding.challenge.ui.theme.CodingChallengeTheme
import com.zohaib.coding.challenge.ui.theme.primary
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodingChallengeTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier) {
    var screen by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val activity = (LocalContext.current as Activity)

    Surface(modifier.fillMaxSize()) {
        when (screen) {
            0 -> {
                TaskList {
                    when (it) {
                        "Slide to Unlock" -> {
                            screen = 1
                        }
                        else -> {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            1 -> Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SwipeButtonScreen {
                    screen = 2
                }
            }

            2 -> DialogUnlockedSuccessfully {
                screen = 0
            }
        }
    }
    BackHandler {
        if (screen == 0) activity.finish()
        else screen = 0
    }
}

@Composable
fun TaskList(
    names: List<String> = arrayListOf("Slide to Unlock", "Task 1", "Task 2", "Task 3"),
    onClick: (name: String) -> Unit
) {
    Surface {
        LazyColumn(Modifier.padding(vertical = 4.dp)) {
            items(items = names) { name ->
                TaskItem(name = name, onClick)
            }
        }
    }
}

@Composable
fun TaskItem(name: String, onClick: (name: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Spacer(Modifier.size(8.dp))
        Button(
            onClick = { onClick(name) },
            contentPadding = PaddingValues(all = 20.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = primary
            )
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeButton(
    modifier: Modifier = Modifier,
    text: String,
    onSwipe: () -> Unit,
) {
    val width = 250.dp
    val widthInPx = with(LocalDensity.current) {
        width.toPx()
    }
    val anchors = mapOf(
        0F to 0,
        widthInPx to 1,
    )
    val swipeableState = rememberSwipeableState(0)

    LaunchedEffect(
        key1 = swipeableState.currentValue,
    ) {
        if (swipeableState.currentValue == 1) {
            onSwipe()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(
                horizontal = 48.dp,
                vertical = 16.dp,
            )
            .clip(CircleShape)
            .background(primary)
            .fillMaxWidth()
            .requiredHeight(64.dp),
    ) {
        SwipeIndicator(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset {
                    IntOffset(
                        swipeableState.offset.value.roundToInt(),
                        0
                    )
                }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ ->
                        FractionalThreshold(0.8F)
                    },
                    orientation = Orientation.Horizontal,
                ),
            backgroundColor = primary,
        )
        Text(
            text = text,
            color = White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 80.dp)
        )
    }
}

@Composable
fun SwipeIndicator(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight()
            .padding(2.dp)
            .clip(CircleShape)
            .aspectRatio(
                ratio = 1.0F,
                matchHeightConstraintsFirst = true,
            )
            .background(White),
    ) {
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = backgroundColor,
            modifier = Modifier.size(36.dp),
        )
    }
}

@Composable
fun SwipeButtonScreen(onSwipe: () -> Unit) {
    SwipeButton(
        text = "Swipe to unlock",
        onSwipe = onSwipe
    )
}

@Composable
fun DialogUnlockedSuccessfully(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Unlocked Successfully")
        },
        text = {
            Text("Screen unlocked successfully")
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary
                )

            ) {
                Text("OK")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    CodingChallengeTheme {
        TaskList(onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SwipePreview() {
    CodingChallengeTheme {
        SwipeButtonScreen(onSwipe = {})
    }
}

@Preview(showBackground = true)
@Composable
fun Dialog() {
    CodingChallengeTheme {
        DialogUnlockedSuccessfully(onDismiss = {})
    }
}