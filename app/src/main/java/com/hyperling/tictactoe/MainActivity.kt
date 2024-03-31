package com.hyperling.tictactoe

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.hyperling.tictactoe.ui.theme.TicTacToeTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                // A surface container using the 'background' color from the theme
                //Surface( modifier = Modifier.fillMaxSize()
                //    , color = MaterialTheme.colorScheme.background
                //) {
                    Game()
                //}
            }
        }
    }
}

// https://stackoverflow.com/questions/68882402/how-to-change-text-value-when-a-button-is-clicked-in-jetpack-compose
// https://jetpackcomposeworld.com/buttons-in-jetpack-compose/

// Compose crash course.
// https://www.youtube.com/watch?v=6_wK_Ud8--0
// Gets into states and using a button to change things.
// https://youtu.be/6_wK_Ud8--0?feature=shared&t=1770

// How to use lists in state.
// https://tigeroakes.com/posts/mutablestateof-list-vs-mutablestatelistof/

@Composable
fun Game() {

    // Shown on the screen.
    //val grid = remember { mutableStateListOf<String>() }
    var grid by remember { mutableStateOf(mutableListOf<String>()) }
    var turn by remember { mutableStateOf("X") }
    var lastTurn by remember { mutableStateOf("O") }
    var msg by remember { mutableStateOf("") }

    // Hidden flags for determining where we're at.
    var status by remember{ mutableIntStateOf(0) }
    var gameOver by remember{ mutableStateOf(false) }
    var showClear by remember { mutableStateOf(true) }
    var newGame by remember { mutableStateOf(true) }

    if (status != 0) {
        if (status == 1) {
            msg = "Congratulations, $lastTurn won!"
        } else if (status == 2) {
            msg = "Tie, better luck next time!"
        }
        turn = "X"
        lastTurn = "O"
        status = 0
        showClear = true
        gameOver = true
    }

    if (lastTurn != turn && !gameOver) {
        lastTurn = turn
        msg = "Player $turn, your turn!"
    }

    if (newGame) {
        //grid.clear()
        grid = mutableListOf<String>()
        for (i in 1..9) {
            //grid.add("")
            grid += ""
        }
        showClear = false
        gameOver = false
        newGame = false
    }

    // https://developer.android.com/develop/ui/compose/layouts/constraintlayout
    // Need to switch to a Constraint layout to prevent the
    //   Play New Game button from screwing everything up.
    /* * /
    ConstraintLayout (
        contentAlignment = Alignment.Center
        , modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
    // */
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
            , verticalArrangement = Arrangement.Center
            , modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {

            Text(
                text = "Let's play Tic-Tac-Toe!"
                , textAlign = TextAlign.Center
                , fontSize = 32.sp
            )
            Text(
                text = msg
                , textAlign = TextAlign.Center
                , fontSize = 32.sp
                , modifier = Modifier
                    .padding(10.dp)
            )
            Column(
                //modifier = Modifier
                //    .background(Color.Black, RectangleShape)
            ) {
                Row {
                    Button(
                        onClick = {
                            if (!gameOver && grid[0].isEmpty()) {
                                //grid[0] = turn
                                grid[0] = turn
                                status = checkGrid(grid)
                                turn = changeTurn(turn)
                            }
                        }
                        , modifier = Modifier
                            .size(69.dp)
                    ) {
                        Text(
                            text = grid[0]
                            , fontSize = 24.sp
                            , textAlign = TextAlign.Center
                        )
                    }
                    Button(
                        onClick = {
                            if (!gameOver && grid[1].isEmpty()) {
                                grid[1] = turn
                                status = checkGrid(grid)
                                turn = changeTurn(turn)
                            }
                        }
                        , modifier = Modifier
                            .size(69.dp)
                    ) {
                        Text(
                            text = grid[1]
                            , fontSize = 24.sp
                            , textAlign = TextAlign.Center
                        )
                    }
                    Button(
                        onClick = {
                            if (!gameOver && grid[2].isEmpty()) {
                                grid[2] = turn
                                status = checkGrid(grid)
                                turn = changeTurn(turn)
                            }
                        }
                        , modifier = Modifier
                            .size(69.dp)
                    ) {
                        Text(
                            text = grid[2]
                            , fontSize = 24.sp
                            , textAlign = TextAlign.Center
                        )
                    }
                }
                Row {
                    Button(
                        onClick = {
                            if (!gameOver && grid[3].isEmpty()) {
                                grid[3] = turn
                                status = checkGrid(grid)
                                turn = changeTurn(turn)
                            }
                        }
                        , modifier = Modifier
                            .size(69.dp)
                    ) {
                        Text(
                            text = grid[3]
                            , fontSize = 24.sp
                            , textAlign = TextAlign.Center
                        )
                    }
                    Button(
                        onClick = {
                            if (!gameOver && grid[4].isEmpty()) {
                                grid[4] = turn
                                status = checkGrid(grid)
                                turn = changeTurn(turn)
                            }
                        }
                        , modifier = Modifier
                            .size(69.dp)
                    ) {
                        Text(
                            text = grid[4]
                            , fontSize = 24.sp
                            , textAlign = TextAlign.Center
                        )
                    }
                    Button(
                        onClick = {
                            if (!gameOver && grid[5].isEmpty()) {
                                grid[5] = turn
                                status = checkGrid(grid)
                                turn = changeTurn(turn)
                            }
                        }
                        , modifier = Modifier
                            .size(69.dp)
                    ) {
                        Text(
                            text = grid[5]
                            , fontSize = 24.sp
                            , textAlign = TextAlign.Center
                        )
                    }
                }
                Row {
                    Button(
                        onClick = {
                            if (!gameOver && grid[6].isEmpty()) {
                                grid[6] = turn
                                status = checkGrid(grid)
                                turn = changeTurn(turn)
                            }
                        }
                        , modifier = Modifier
                            .size(69.dp)
                    ) {
                        Text(
                            text = grid[6]
                            , fontSize = 24.sp
                            , textAlign = TextAlign.Center
                        )
                    }
                    Button(
                        onClick = {
                            if (!gameOver && grid[7].isEmpty()) {
                                grid[7] = turn
                                status = checkGrid(grid)
                                turn = changeTurn(turn)
                            }
                        }
                        , modifier = Modifier
                            .size(69.dp)
                    ) {
                        Text(
                            text = grid[7]
                            , fontSize = 24.sp
                            , textAlign = TextAlign.Center
                        )
                    }
                    Button(
                        onClick = {
                            if (!gameOver && grid[8].isEmpty()) {
                                grid[8] = turn
                                status = checkGrid(grid)
                                turn = changeTurn(turn)
                            }
                        }, modifier = Modifier.size(69.dp)
                    ) {
                        Text(
                            text = grid[8]
                            , fontSize = 24.sp
                            , textAlign = TextAlign.Center
                        )
                    }
                }
            }
            // TBD how to make this appear without moving all the
            //   other items up then back down when it leaves?
            if (showClear) {
                Spacer(modifier = Modifier.size(8.dp))
                Row {
                    Button(onClick = { newGame = true }) {
                        Text(
                            text = "Start New Game"
                            , fontSize = 18.sp
                        )
                    }
                }
            }

            // Radio list for opponent difficulty.
            /* * /
            Column {
                RadioGroup (
                    RadioButton(selected = true, onClick = { /* NOTHING */ })
                )

            }
            // */
        }
    /* * /
    }
    // */
}

/* * /
@Preview(
    showBackground = true
    , uiMode = Configuration.UI_MODE_NIGHT_NO
)
// */
/* */
@Preview(
    showBackground = true
    , uiMode = Configuration.UI_MODE_NIGHT_YES
)
// */
@Composable
fun PreviewGame() {
    Game()
}

// Check whether any winning conditions have been met.
// Return values:
//   0: False, no victor yet.
//   1: Victory!
//   2: Game ended in a tie.
fun checkGrid(grid: List<String>): Int {

    // Left column
    if (grid[0].isNotBlank() && grid[0] === grid[3] && grid[3] === grid[6]) {
        return 1
    }
    // Center column
    if (grid[1].isNotBlank() && grid[1] == grid[4] && grid[4] === grid[7]) {
        return 1
    }
    // Right column
    if (grid[2].isNotBlank() && grid[2] === grid[5] && grid[5] === grid[8]) {
        return 1
    }

    // Top row
    if (grid[0].isNotBlank() && grid[0] === grid[1] && grid[1] === grid[2]) {
        return 1
    }
    // Middle row
    if (grid[3].isNotBlank() && grid[3] === grid[4] && grid[4] === grid[5]) {
        return 1
    }
    // Bottom row
    if (grid[6].isNotBlank() && grid[6] === grid[7] && grid[7] === grid[8]) {
        return 1
    }

    // Top left to bottom right diagonal
    if (grid[0].isNotBlank() && grid[0] === grid[4] && grid[4] === grid[8]) {
        return 1
    }
    // Top right to bottom left diagonal
    if (grid[2].isNotBlank() && grid[2] === grid[4] && grid[4] === grid[6]) {
        return 1
    }

    var tie = true
    for (item in grid) {
        if (item.isBlank()) {
            tie = false
            break
        }
    }
    if (tie) { return 2 }

    // No end game status yet.
    return 0
}

// Change which player's turn it is.
fun changeTurn(turn: String): String {
    if (turn == "X") {
        return "O"
    }
    return "X"
}

/* AI Players */
fun playRandomMove(grid: MutableList<String>) : Int {
    return -1
}

fun playWinningMove(grid: MutableList<String>) : Int {
    return -1
}

fun playAnnoyingMove(grid: MutableList<String>) : Int {
    return -1
}
