package com.example.act17_uf1



import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var activePlayer = 1 // 1 -> Jugador X, 2 -> Jugador O
    private val board = Array(3) { IntArray(3) { 0 } } // Taulell inicialitzat a 0
    private lateinit var statusText: TextView
    private lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.textViewStatus)
        resetButton = findViewById(R.id.resetButton)

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)


        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            val row = i / 3
            val col = i % 3
            button.setOnClickListener {
                onCellClick(button, row, col)
            }
        }

        // Reiniciar el joc
        resetButton.setOnClickListener { resetGame(gridLayout) }
    }

    private fun onCellClick(button: Button, row: Int, col: Int) {
        if (board[row][col] != 0) return // Si la casella ja està ocupada, no fer res

        // Actualitzar la casella amb el símbol del jugador
        if (activePlayer == 1) {
            button.text = "X"
            board[row][col] = 1
            activePlayer = 2
            statusText.text = "Jugador 2 (O) és el torn!"
        } else {
            button.text = "O"
            board[row][col] = 2
            activePlayer = 1
            statusText.text = "Jugador 1 (X) és el torn!"
        }

        // Comprovar si hi ha un guanyador
        val winner = checkWinner()
        if (winner != 0) {
            statusText.text = "Jugador $winner ha guanyat!"
            disableBoard()
        } else if (isBoardFull()) {
            statusText.text = "Empat!"
        }
    }

    private fun checkWinner(): Int {
        // Comprovar files i columnes
        for (i in 0..2) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2])
                return board[i][0]
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i])
                return board[0][i]
        }
        // Comprovar diagonals
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            return board[0][0]
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0])
            return board[0][2]

        return 0 // No hi ha guanyador
    }

    private fun isBoardFull(): Boolean {
        for (row in board) {
            if (row.contains(0)) return false
        }
        return true
    }

    private fun disableBoard() {
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            button.isEnabled = false
        }
    }

    private fun resetGame(gridLayout: GridLayout) {
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            button.text = ""
            button.isEnabled = true
        }
        for (i in board.indices) {
            for (j in board[i].indices) {
                board[i][j] = 0
            }
        }
        activePlayer = 1
        statusText.text = "Jugador 1 (X) comença!"
    }
}
