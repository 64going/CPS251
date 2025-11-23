package com.example.test

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import androidx.lifecycle.viewmodel.compose.viewModel
/*
1. How does the drag gesture system determine when a tile should move?
2. Why represent the empty space as 0 instead of null or a separate empty state? How does this choice affect the rest of the code?
3. Why use LazyVerticalGrid instead of a regular Column with Row composables? What are the performance and code organization benefits?
4. When a tile moves, which parts of the UI recompose? How does Compose know what to update when the puzzle state changes?
5. What prevents invalid moves (e.g., moving a tile that isn't adjacent to the empty space)? Where should this validation logic live?
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumbersScreen(viewModel: NumbersViewModel = viewModel()) {

    val numbers = viewModel.randomizeNumbers()
    var tiles by remember { mutableStateOf(viewModel.randomizeNumbers()) }
    var moves by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val gridSize = 3

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sliding Number Puzzle") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isGameOver) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Puzzle Solved!", style = MaterialTheme.typography.headlineMedium)
                        Text(
                            "You solved it in $moves moves!",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier.width(120.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text("Moves", style = MaterialTheme.typography.labelMedium)
                        Text(
                            "$moves",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Card(
                    modifier = Modifier.width(120.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text("Status", style = MaterialTheme.typography.labelMedium)
                        Text(
                            if (isGameOver) {
                                "Solved"
                            } else {
                                "Playing"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(0.6f),
                shape = RoundedCornerShape(8.dp),
                onClick = { isGameOver = false;moves = 0;tiles = viewModel.randomizeNumbers() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("New Puzzle", style = MaterialTheme.typography.labelLarge)
            }
            // 3x3 Grid layout
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.size(320.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(numbers) { tile ->
                    val index = numbers.indexOf(tile)
                    val tileValue = tiles[index] // Get the value at this position
                    val isDragging = draggedIndex == index // Is this tile being dragged?

                    val animatedOffset by animateOffsetAsState(
                        targetValue = if (isDragging) dragOffset else Offset.Zero,
                        animationSpec = if (isDragging) {
                            tween(durationMillis = 0)
                        } else {
                            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                        }, label = "tileOffset"
                    )

                    val scale by animateFloatAsState(
                        targetValue = if (isDragging) 1.1f else 1f,
                        animationSpec = tween(durationMillis = 200), label = "tileScale"
                    )

                    val alpha by animateFloatAsState(
                        targetValue = if (isDragging) 0.8f else 1f,
                        animationSpec = tween(durationMillis = 200), label = "tileAlpha"
                    )

                    if (tileValue == 0) {
                        Card(
                            modifier = Modifier
                                .size(90.dp),
                            colors = CardDefaults.cardColors(
                                contentColor = Color.Transparent,
                                containerColor = Color.Transparent
                            ),
                        ) {
                            Box(
                            ) {}
                        }
                    } else {
                        Card(
                            modifier = Modifier
                                .size(90.dp)
                                .offset {
                                    IntOffset(
                                        animatedOffset.x.roundToInt(),
                                        animatedOffset.y.roundToInt()
                                    )
                                }
                                .scale(scale)
                                .alpha(alpha)
                                .shadow(
                                    elevation = if (isDragging) 2.dp else 6.dp,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .pointerInput(index) {
                                    detectDragGestures(
                                        onDragStart = {
                                            draggedIndex = index
                                            dragOffset = Offset.Zero
                                        },
                                        onDragEnd = {
                                            // Find where the empty space (0) is located
                                            val emptyIndex = tiles.indexOf(0)

                                            // Only swap if the dragged tile is next to the empty space
                                            if (viewModel.isAdjacent(index, emptyIndex, gridSize)) {
                                                // Swap: move tile to empty space, make old position empty
                                                val newTiles = tiles.toMutableList()
                                                newTiles[emptyIndex] =
                                                    tiles[index] // Move tile to empty space
                                                newTiles[index] =
                                                    0 // Make old position empty
                                                tiles = newTiles
                                                moves++
                                                isGameOver = viewModel.isGameWon(tiles)
                                            }

                                            // Reset drag state
                                            draggedIndex = null
                                            dragOffset = Offset.Zero
                                        },
                                        onDrag ={ change, dragAmount ->
                                            change.consume()
                                            dragOffset += Offset(dragAmount.x, dragAmount.y)
                                        }
                                    )
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE3F2FD),
                                contentColor = Color(0xFF1565C0)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tileValue.toString(),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}