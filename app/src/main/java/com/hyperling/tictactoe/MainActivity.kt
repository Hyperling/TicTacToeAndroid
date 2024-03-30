package com.hyperling.tictactoe

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hyperling.tictactoe.ui.theme.TicTacToeTheme
import org.w3c.dom.Text
import java.lang.reflect.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                // A surface container using the 'background' color from the theme
                /*Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }*/
                Game()
            }
        }
    }
}
/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TicTacToeTheme {
        Greeting("Android")
    }
}
*/

@Composable
fun Game() {
    Text(text = "Let's play Tic-tac-Toe!")

    // Create 9 buttons for the grid. I wish this was XML...???!!!
    lateinit var buttons: Array<Button>
    for (i in 0..8) {
        val x = i % 3
        val y = i / 3
        buttons.set(i, Button(
            text = "Slot $x,$y"
        )
    }
}

@Preview
@Composable
fun PreviewGame() {
    Game("Yippee!")
}
