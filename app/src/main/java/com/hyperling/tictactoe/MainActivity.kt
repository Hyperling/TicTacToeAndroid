package com.hyperling.tictactoe

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.hyperling.tictactoe.ui.theme.TicTacToeTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                Surface {
                    Game()
                }
            }
        }
    }
}

@Composable
fun Game() {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val activity = (LocalContext.current as? Activity)

    // Shown on the screen.
    val grid = remember { mutableStateListOf<String>() }
    //var grid by remember { mutableStateOf(mutableListOf<String>()) }
    var turn by remember { mutableStateOf("X") }
    var lastTurn by remember { mutableStateOf("O") }
    var msg by remember { mutableStateOf("") }

    // Hidden flags for determining where we're at.
    var status by remember{ mutableIntStateOf(0) }
    var newGame by remember { mutableStateOf(true) }
    var gameOver by remember{ mutableStateOf(false) }
    var showClear by remember { mutableStateOf(true) }
    var clearText by remember { mutableStateOf("") }
    
    // AI choices.
    var player by remember { mutableStateOf("X") }
    var opponent by remember { mutableStateOf("") }
    var opponentHuman by remember { mutableStateOf(true) }
    var opponentRandom by remember { mutableStateOf(false) }
    var opponentHard by remember { mutableStateOf(false) }
    var opponentEasy by remember { mutableStateOf(false) }
    var opponentAnnoying by remember { mutableStateOf(false) }

    opponent = "O"
    if (player == "O") {
        opponent = "X"
    }

    fun setRadiosFalse(): Boolean {
        opponentHuman = false
        opponentRandom = false
        opponentHard = false
        opponentEasy = false
        opponentAnnoying = false
        return true
    }

    // Being a goofball.
    var mainText by remember { mutableStateOf("") }
    var mainClicks by remember { mutableIntStateOf(0) }

    if (status != 0) {
        if (status == 1) {
            msg = "Congratulations, $lastTurn won!"
        } else if (status == 2) {
            msg = "Tie, better luck next time!"
        }
        status = 0
        showClear = true
        gameOver = true
    }

    if (lastTurn != turn && !gameOver) {
        lastTurn = turn
        msg = "Player $turn, your turn!"
    }

    if (newGame) {
        grid.clear()
        //grid = mutableListOf<String>()
        for (i in 1..9) {
            //grid.add("")
            grid += ""
        }
        showClear = false
        gameOver = false
        newGame = false
        turn = "X"
        lastTurn = "O"
    }

    /* AI logic. * /
    if (turn == opponent) {
        var play = -1
        if (opponentRandom) {
            play = playRandomMove(grid)
        } else if (opponentHard) {
            play = playWeightedMove(grid, 1, opponent)
        } else if (opponentEasy) {
            play = playWeightedMove(grid, 0, opponent)
        } else if (opponentAnnoying) {
            play = playWeightedMove(grid, 2, opponent)
        }
        if (!opponentHuman) {
            grid[play] = opponent
        }
    }
    // */

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
        , verticalArrangement = Arrangement.Center
        , modifier = Modifier
            .fillMaxSize()
    ) {

        Spacer(modifier = Modifier.weight(0.05f))

        /* Header text. */
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            when (mainClicks) {
                0 -> mainText = "Let's play Tic-Tac-Toe!"
                1 -> mainText = "Ouch! Why'd you hit me!?"
                2 -> mainText = "Hey! Stop that!"
                3 -> mainText = "Alright wise guy..."
                4 -> mainText = "Play the game!"
                5 -> mainText = "I'm done with you!"
                6 -> mainText = "Last chance, jerk!!"
                100 -> mainText = "You're determined, fella."
                7 -> {
                    mainText = "OK, take this!"
                    opponentEasy = true
                    lastTurn = opponent
                    status = 1
                }

                else -> mainText = "We're done here, play!"
            }
            Text(
                text = mainText,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier.clickable {
                    mainClicks++
                }
            )
            Text(
                text = msg,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(10.dp)
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        // */

        /* The Grid */
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // OK, this is awesome, thanks Jetbrains Compose.
            var count = 0
            for (row in 1..3) {
                Row {
                    for (col in 1..3) {
                        val index = count
                        Button(
                            onClick = {
                                if (!gameOver && grid[index].isEmpty()) {
                                    grid[index] = turn
                                    status = checkGrid(grid)
                                    turn = changeTurn(turn)
                                }
                            }, modifier = Modifier
                                .size(69.dp)
                        ) {
                            Text(
                                text = grid[index],
                                fontSize = 28.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        count++
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(0.05f))
        // */


        /* Clear, restart, start new game button. */
        clearText = "Restart"
        if (showClear) {
            clearText = "Start New Game"
        }
        Row {
            FilledTonalButton(onClick = { newGame = true }) {
                Text(
                    text = clearText, fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.05f))
        // */

        /* Allow player to go 2nd (play as O) */
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Primary user playing as '$player'.",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.size(5.dp))
            Switch(
                checked = (player == "X"),
                onCheckedChange = {
                    when (player) {
                        "X" -> player = "O"
                        "O" -> player = "X"
                    }

                    if (!gameOver) {
                        Toast.makeText(
                            context,
                            "Game has been cleared.",
                            Toast.LENGTH_LONG
                        ).show()
                        gameOver = true
                        newGame = true
                    }

                    Toast.makeText(
                        context,
                        "AI is now playing as $opponent.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
        Spacer(modifier = Modifier.weight(0.05f))
        // */

        /* Opponent difficulty radio buttons. */
        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom
        ){
            Text(
                text = stringResource(id = R.string.opponent_header),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.size(5.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    opponentHuman = setRadiosFalse()
                }
            ){
                RadioButton(
                    selected = opponentHuman,
                    onClick = { opponentHuman = setRadiosFalse() },
                )
                Text(
                    text = stringResource(id = R.string.opponent_human),
                    fontSize = 16.sp
                )
            }
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                RadioButton(
                    selected = opponentRandom,
                    onClick = { opponentRandom = setRadiosFalse() },
                )
                Text(
                    text = stringResource(id = R.string.opponent_random),
                    fontSize = 16.sp
                )
            }
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                RadioButton(
                    selected = opponentHard,
                    onClick = { opponentHard = setRadiosFalse() },
                )
                Text(
                    text = stringResource(id = R.string.opponent_hard),
                    fontSize = 16.sp
                )
            }
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                RadioButton(
                    selected = opponentEasy,
                    onClick = { opponentEasy = setRadiosFalse() },
                )
                Text(
                    text = stringResource(id = R.string.opponent_easy),
                    fontSize = 16.sp
                )
            }
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                RadioButton(
                    selected = opponentAnnoying,
                    onClick = { opponentAnnoying = setRadiosFalse() },
                )
                Text(
                    text = stringResource(id = R.string.opponent_annoying),
                    fontSize = 16.sp
                )
            }
        }
        // */

        Spacer(modifier = Modifier.weight(.05f))

        // Media Links TBD
        Column(
            verticalArrangement = Arrangement.Bottom
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val website = stringResource(id = R.string.uri_website)
                OutlinedButton(
                    onClick = {
                        uriHandler.openUri(website)
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(0.4f)
                ) {
                    Text(
                        text = "Website"
                    )
                }

                Spacer(modifier = Modifier.weight(0.05f))

                IconButton(
                    onClick = { activity?.finish() },
                    modifier = Modifier.weight(0.1f)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close
                        , contentDescription = "Exit"
                        , modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(0.05f))

                val github = stringResource(id = R.string.uri_github)
                OutlinedButton(
                    onClick = {
                        uriHandler.openUri(github)
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(0.4f)
                ) {
                    Text(
                        text = "GitHub"
                    )
                }
            }

        }
    }
}

/* */
@Preview(
    name = "Normal"
    , showBackground = true
    , uiMode = Configuration.UI_MODE_NIGHT_NO
    , device = Devices.PIXEL_3A
    //, showSystemUi = true
)
// */
/* */
@Preview(
    name = "Dark"
    , showBackground = true
    , uiMode = Configuration.UI_MODE_NIGHT_YES
    , device = Devices.PIXEL_3A
    , showSystemUi = true
)
// */
@Composable
fun PreviewGame() {
    TicTacToeTheme {
        Surface {
            Game()
        }
    }
}

// Check whether any winning conditions have been met.
// Return values:
//   0: False, no victor yet.
//   1: Victory!
//   2: Game ended in a tie.
private fun checkGrid(grid: List<String>): Int {

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
private fun changeTurn(turn: String): String {
    if (turn == "X") {
        return "O"
    }
    return "X"
}

/* AI Players */
private fun playRandomMove(grid: MutableList<String>): Int {
    val random = Random(9)
    var choice = random.nextInt()
    while (grid[choice].isNotBlank() && choice < 9) {
        choice = random.nextInt()
    }
    return choice
}

// behavior determines if the AI is:
//   0 - Plays to lose
//   1 - Plays to win
//   2 - Plays to tie
// Returns the grid number which should get the AI's mark.
fun playWeightedMove(grid: MutableList<String>, behavior: Int, turn: String) : Int {
    if (behavior != -1 && turn == "O") {
        return playRandomMove(grid)
    }
    return -1
}

