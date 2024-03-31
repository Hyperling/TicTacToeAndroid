package com.hyperling.tictactoe

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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

    // Preview does not handle toasts well, easily turn them off and on.
    val toastsEnabled = true

    /* State-aware variables. */

    // Shown on the screen.
    var msg by remember { mutableStateOf("") }
    var clearText by remember { mutableStateOf("") }

    // Board pieces.
    val grid = remember { mutableStateListOf<String>() }
    if (grid.isEmpty()) {
        for (i in 0..8) {
            grid.add("")
        }
    }
    var turn by remember { mutableStateOf("X") }
    var lastTurn by remember { mutableStateOf("O") }

    // Hidden flags for determining where we're at.
    var status by remember{ mutableIntStateOf(0) }
    var gameOver by remember{ mutableStateOf(false) }
    var showClear by remember { mutableStateOf(true) }

    // Character pieces.
    var player by rememberSaveable { mutableStateOf("X") }
    var opponent by rememberSaveable { mutableStateOf("O") }

    // AI choices.
    var opponentHuman by rememberSaveable { mutableStateOf(false) }
    var opponentRandom by rememberSaveable { mutableStateOf(true) }
    var opponentHard by rememberSaveable { mutableStateOf(false) }
    var opponentEasy by rememberSaveable { mutableStateOf(false) }
    var opponentAnnoying by rememberSaveable { mutableStateOf(false) }
    var opponentShy by rememberSaveable { mutableStateOf(false) }

    // Being a goofball.
    var mainText by remember { mutableStateOf("") }
    var mainClicks by remember { mutableIntStateOf(0) }
    // */


    /* Helper functions to change global variables in bulk. */

    // Check if the game has ended.
    fun checkStatus() {
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
    }

    // Place a piece on the board, check for end game, and move to the next player's piece.
    fun takeTurn(location: Int) {
        lastTurn = turn

        if (!gameOver) {
            grid[location] = turn
            status = checkGrid(grid)
            checkStatus()
            turn = changeTurn(turn)
        }
    }

    // Set all the variables for starting the app near-fresh.
    fun createGame() {
        for (i in 0..8) {
            grid[i] = ""
        }
        showClear = false
        gameOver = false
        turn = "X"
        lastTurn = "O"
    }

    // Toggle whether the primary player goes first or second.
    fun togglePlayer() {
        when (player) {
            "X" -> { player = "O"; opponent = "X" }
            "O" -> { player = "X"; opponent = "O" }
        }

        if (!gameOver) {
            createGame()
            if (toastsEnabled) {
                Toast.makeText(
                    context,
                    "Game has been cleared.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        if (toastsEnabled) {
            Toast.makeText(
                context,
                "Opponent is now $opponent.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Sets all the radio buttons to false and returns true for the one being changed.
    fun setRadiosFalse(): Boolean {
        opponentHuman = false
        opponentRandom = false
        opponentHard = false
        opponentEasy = false
        opponentAnnoying = false
        opponentShy = false
        return true
    }
    // */


    /* AI logic. */
    if (turn == opponent && !gameOver) {
        var play = -1
        if (opponentRandom) {
            play = playWeightedMove(grid, -1, opponent)
        } else if (opponentEasy) {
            play = playWeightedMove(grid, 0, opponent)
        } else if (opponentHard) {
            play = playWeightedMove(grid, 1, opponent)
        } else if (opponentAnnoying) {
            play = playWeightedMove(grid, 2, opponent)
        } else if (opponentShy) {
            play = playWeightedMove(grid, 3, opponent)
        }
        if (!opponentHuman) {
            takeTurn(play)
        }
    }
    // */


    // Main layout object.
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
        , verticalArrangement = Arrangement.Center
        , modifier = Modifier
            .fillMaxSize()
    ) {

        Spacer(modifier = Modifier.weight(0.1f))

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
                5 -> mainText = "I'm getting tired of you!"
                6 -> mainText = "Last chance, jerk!!"
                7 -> {
                    createGame()
                    opponentHard = setRadiosFalse()
                    while (!gameOver) {
                        takeTurn(playWeightedMove(grid, 0, player))
                    }
                    opponentEasy = setRadiosFalse()
                    mainClicks += 1
                }
                8 -> mainText = "OK, take this! I hope easy bot won!"
                9 -> mainText = "We're done here, play the game!"
                10 -> mainText = "What are you, deaf?"
                11 -> mainText = "You're determined, fella."
                12 -> mainText = "Not much more here, I'm boring."
                13 -> mainText = "Seriously though, bye!"
                14 -> mainText = "Fine, keep clicking, I'll play for you."
                15 -> {
                    if (gameOver) { createGame() }
                    takeTurn(playWeightedMove(grid, -1, player))
                    mainClicks = 14
                }
                else -> mainClicks = 14
            }
            Text(
                text = mainText,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier.clickable {
                    mainClicks++
                }
            )

            if (lastTurn != turn && !gameOver) {
                msg = "Player $turn, your turn!"
            }
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
                                    takeTurn(index)
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
        Spacer(modifier = Modifier.size(10.dp))
        // */


        /* Clear, restart, start new game button. */
        clearText = "Restart"
        if (showClear) {
            clearText = "Start New Game"
        }
        Row {
            FilledTonalButton(onClick = { createGame() }) {
                Text(
                    text = clearText, fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.05f))
        // */


        /* Allow player to go 2nd (play as O) */
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { togglePlayer() }
        ) {
            Text(
                text = "Primary user playing as $player.",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.size(5.dp))
            Switch(
                checked = (player == "X"),
                onCheckedChange = { togglePlayer() }
            )
        }
        Spacer(modifier = Modifier.weight(0.05f))
        // */


        /* Opponent difficulty radio buttons. */
        // TBD: Make an array and build the radio buttons dynamically.
        /*
            MyRadioChoice {
                selected: Boolean,
                text: String
            }
        */
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    opponentRandom = setRadiosFalse()
                }
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    opponentHard = setRadiosFalse()
                }
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    opponentEasy = setRadiosFalse()
                }
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    opponentAnnoying = setRadiosFalse()
                }
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
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    opponentShy = setRadiosFalse()
                }
            ){
                RadioButton(
                    selected = opponentShy,
                    onClick = { opponentShy = setRadiosFalse() },
                )
                Text(
                    text = stringResource(id = R.string.opponent_shy),
                    fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(.05f))
        // */


        /* Media links. */
        Column(
            verticalArrangement = Arrangement.Bottom
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text1 = stringResource(id = R.string.link1_text)
                val uri1 = stringResource(id = R.string.link1_uri)
                OutlinedButton(
                    onClick = {
                        uriHandler.openUri(uri1)
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(0.4f)
                ) {
                    Text(
                        text = text1,
                        fontSize = 14.sp
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

                val text2 = stringResource(id = R.string.link2_text)
                val uri2 = stringResource(id = R.string.link2_uri)
                OutlinedButton(
                    onClick = {
                        uriHandler.openUri(uri2)
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(0.4f)
                ) {
                    Text(
                        text = text2,
                        fontSize = 14.sp
                    )
                }
            }

        }
        // */
    }
}

/* Light Theme * /
@Preview(
    name = "Normal"
    , showBackground = true
    , uiMode = Configuration.UI_MODE_NIGHT_NO
    , device = Devices.PIXEL_3A
    //, showSystemUi = true
)
// */
/* Dark Theme */
@Preview(
    name = "Night"
    , showBackground = true
    , uiMode = Configuration.UI_MODE_NIGHT_YES
    , device = Devices.PIXEL_3A
    //, showSystemUi = true
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

/* Random Player * /
private fun playRandomMove(grid: MutableList<String>): Int {
    /*
    var choice: Int
    do {
        choice = (0..8).random()
    }
    while (grid[choice].isNotEmpty())
    return choice
    */
    return playWeightedMove(grid, -1, "X")
}
// */


/* AI Players */
// Behavior determines if the AI is:
//   0 - Plays to lose
//   1 - Plays to win
//   2 - Plays to tie
//   * - Random, every open spot has the same weight.
// Returns the grid number which should get the AI's mark.
fun playWeightedMove(grid: MutableList<String>, behavior: Int, piece: String) : Int {
    val human: String = ( if (piece == "X") "O" else "X" )

    // Custom weights depending on the AI's desire.
    val played: Int = -100
    val side : Int
    val corner: Int
    val middle: Int
    val causeWin: Int
    val preventLoss: Int
    when (behavior) {
        // Easy, does not prevent human from winning and may try to win.
        0 -> {
            causeWin = 100
            preventLoss = -1
            middle = 1
            corner = 1
            side = 1
        }
        // Hard, will try to win for itself and prevent the  human from winning.
        1 -> {
            causeWin = 100
            preventLoss = 99
            middle = 4
            corner = 3
            side = 2
        }
        // Annoying / stubborn, will not let you win but also will not try to win.
        2 -> {
            causeWin = -1
            preventLoss = 100
            middle = 4
            corner = 3
            side = 2
        }
        // Shy, refuses to complete the game unless it's the only move left.
        3 -> {
            causeWin = -1
            preventLoss = -1
            middle = 1
            corner = 1
            side = 1
        }
        // Random! No need for the other function anymore. ;)
        else -> {
            causeWin = 0
            preventLoss = 0
            middle = 0
            corner = 0
            side = 0
        }
    }

    // Initialize a blank board's weights.
    val weights: IntArray = intArrayOf(
        corner, side, corner, side, middle, side, corner, side, corner
    )

    /* Check if any conditions are currently available. */

    // Top Row, Win Conditions
    if (grid[0] == piece  && grid[1] == piece  && grid[2].isBlank())
        weights[2] = causeWin
    if (grid[0] == piece  && grid[1].isBlank() && grid[2] == piece)
        weights[1] = causeWin
    if (grid[0].isBlank() && grid[1] == piece  && grid[2] == piece)
        weights[0] = causeWin

    // Top Row, Lose Conditions
    if (grid[0] == human  && grid[1] == human  && grid[2].isBlank())
        weights[2] = preventLoss
    if (grid[0] == human  && grid[1].isBlank() && grid[2] == human)
        weights[1] = preventLoss
    if (grid[0].isBlank() && grid[1] == human  && grid[2] == human)
        weights[0] = preventLoss

    // Middle Row, Win Conditions
    if (grid[3] == piece  && grid[4] == piece  && grid[5].isBlank())
        weights[5] = causeWin
    if (grid[3] == piece  && grid[4].isBlank() && grid[5] == piece)
        weights[4] = causeWin
    if (grid[3].isBlank() && grid[4] == piece  && grid[5] == piece)
        weights[3] = causeWin

    // Middle Row, Lose Conditions
    if (grid[3] == human  && grid[4] == human  && grid[5].isBlank())
        weights[5] = preventLoss
    if (grid[3] == human  && grid[4].isBlank() && grid[5] == human)
        weights[4] = preventLoss
    if (grid[3].isBlank() && grid[4] == human  && grid[5] == human)
        weights[3] = preventLoss

    // Bottom Row, Win Conditions
    if (grid[6] == piece  && grid[7] == piece  && grid[8].isBlank())
        weights[8] = causeWin
    if (grid[6] == piece  && grid[7].isBlank() && grid[8] == piece)
        weights[7] = causeWin
    if (grid[6].isBlank() && grid[7] == piece  && grid[8] == piece)
        weights[6] = causeWin

    // Bottom Row, Lose Conditions
    if (grid[6] == human  && grid[7] == human  && grid[8].isBlank())
        weights[8] = preventLoss
    if (grid[6] == human  && grid[7].isBlank() && grid[8] == human)
        weights[7] = preventLoss
    if (grid[6].isBlank() && grid[7] == human  && grid[8] == human)
        weights[6] = preventLoss

    // Left Column, Win Conditions
    if (grid[0] == piece  && grid[3] == piece  && grid[6].isBlank())
        weights[6] = causeWin
    if (grid[0] == piece  && grid[3].isBlank() && grid[6] == piece)
        weights[3] = causeWin
    if (grid[0].isBlank() && grid[3] == piece  && grid[6] == piece)
        weights[0] = causeWin

    // Left Column, Lose Conditions
    if (grid[0] == human  && grid[3] == human  && grid[6].isBlank())
        weights[6] = preventLoss
    if (grid[0] == human  && grid[3].isBlank() && grid[6] == human)
        weights[3] = preventLoss
    if (grid[0].isBlank() && grid[3] == human  && grid[6] == human)
        weights[0] = preventLoss

    // Middle Column, Win Conditions
    if (grid[1] == piece  && grid[4] == piece  && grid[7].isBlank())
        weights[7] = causeWin
    if (grid[1] == piece  && grid[4].isBlank() && grid[7] == piece)
        weights[4] = causeWin
    if (grid[1].isBlank() && grid[4] == piece  && grid[7] == piece)
        weights[1] = causeWin

    // Middle Column, Lose Conditions
    if (grid[1] == human  && grid[4] == human  && grid[7].isBlank())
        weights[7] = preventLoss
    if (grid[1] == human  && grid[4].isBlank() && grid[7] == human)
        weights[4] = preventLoss
    if (grid[1].isBlank() && grid[4] == human  && grid[7] == human)
        weights[1] = preventLoss

    // Right Column, Win Conditions
    if (grid[2] == piece  && grid[5] == piece  && grid[8].isBlank())
        weights[8] = causeWin
    if (grid[2] == piece  && grid[5].isBlank() && grid[8] == piece)
        weights[5] = causeWin
    if (grid[2].isBlank() && grid[5] == piece  && grid[8] == piece)
        weights[2] = causeWin

    // Right Column, Lose Conditions
    if (grid[2] == human  && grid[5] == human  && grid[8].isBlank())
        weights[8] = preventLoss
    if (grid[2] == human  && grid[5].isBlank() && grid[8] == human)
        weights[5] = preventLoss
    if (grid[2].isBlank() && grid[5] == human  && grid[8] == human)
        weights[2] = preventLoss

    // Top Left Diagonal, Win Conditions
    if (grid[0] == piece  && grid[4] == piece  && grid[8].isBlank())
        weights[8] = causeWin
    if (grid[0] == piece  && grid[4].isBlank() && grid[8] == piece)
        weights[4] = causeWin
    if (grid[0].isBlank() && grid[4] == piece  && grid[8] == piece)
        weights[0] = causeWin

    // Top Left Diagonal, Lose Conditions
    if (grid[0] == human  && grid[4] == human  && grid[8].isBlank())
        weights[8] = preventLoss
    if (grid[0] == human  && grid[4].isBlank() && grid[8] == human)
        weights[4] = preventLoss
    if (grid[0].isBlank() && grid[4] == human  && grid[8] == human)
        weights[0] = preventLoss

    // Top Right Diagonal, Win Conditions
    if (grid[2] == piece  && grid[4] == piece  && grid[6].isBlank())
        weights[6] = causeWin
    if (grid[2] == piece  && grid[4].isBlank() && grid[6] == piece)
        weights[4] = causeWin
    if (grid[2].isBlank() && grid[4] == piece  && grid[6] == piece)
        weights[2] = causeWin

    // Top Right Diagonal, Lose Conditions
    if (grid[2] == human  && grid[4] == human  && grid[6].isBlank())
        weights[6] = preventLoss
    if (grid[2] == human  && grid[4].isBlank() && grid[6] == human)
        weights[4] = preventLoss
    if (grid[2].isBlank() && grid[4] == human  && grid[6] == human)
        weights[2] = preventLoss

    // */

    // Set all the played pieces to a very low number.
    for (i in 0..8) {
        if (grid[i].isNotBlank()) {
            weights[i] = played
        }
    }

    // Go through the weights, and put the indexes of the highest in a new array.
    var highestIndexes: IntArray = intArrayOf()
    var heaviest = played + 1
    for ((count, weight) in weights.withIndex()) {
        if (weight > heaviest) {
            heaviest = weight
            highestIndexes = intArrayOf(count)
        } else if (weight == heaviest) {
            highestIndexes += count
        }
    }

    // In case there are multiple indexes with a tied weight, choose a random one.
    return highestIndexes[highestIndexes.indices.random()]
}
// */

