package com.zairian.thirtygame

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

const val EXTRA_SCORES_BY_CHOICE = "com.zairian.thirtygame.scores_by_choice"

// Activity that shows the result after game end
class ResultActivity : AppCompatActivity() {

    private lateinit var scoresTextByChoice : List<TextView>
    private lateinit var totalScore : TextView
    private lateinit var newGameButton : Button

    private var scoresByChoice = IntArray(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        scoresByChoice = intent.getIntArrayExtra(EXTRA_SCORES_BY_CHOICE)!!

        initScoreTextViews()

        totalScore = findViewById(R.id.total_score_text_view)
        val score : String = getText(R.string.total_score_text_view).toString() + scoresByChoice.sum()
        totalScore.text = score

        initNewGameButton()
    }

    // Initializes the textviews showing the scores of each round
    private fun initScoreTextViews() {
        scoresTextByChoice = listOf(
            findViewById(R.id.low_score_text_view),
            findViewById(R.id.sum4_score_text_view),
            findViewById(R.id.sum5_score_text_view),
            findViewById(R.id.sum6_score_text_view),
            findViewById(R.id.sum7_score_text_view),
            findViewById(R.id.sum8_score_text_view),
            findViewById(R.id.sum9_score_text_view),
            findViewById(R.id.sum10_score_text_view),
            findViewById(R.id.sum11_score_text_view),
            findViewById(R.id.sum12_score_text_view)
        )

        // Updates the textviews with the score that was sent as extra data from MainActivity
        for(i in scoresTextByChoice.indices) {
            scoresTextByChoice[i].text = scoresByChoice[i].toString()
        }
    }

    // Initializes the New Game Button used to create a new game
    private fun initNewGameButton() {
        newGameButton = findViewById(R.id.new_game_button)

        // When pressed the activity stack is cleared with the NEW_TASK and CLEAR_TASK flags and Main Activity is restarted
        newGameButton.setOnClickListener {
            val intent = MainActivity.newIntent(this@ResultActivity)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            //Used to skip the animation that plays when the application essentially restarts
            overridePendingTransition(0,0)
        }
    }

    // Companion object that returns an Intent used when transiting from MainActivity to ResultActivity
    companion object{
        fun newIntent(packageContext: Context, scoresByChoice: IntArray ): Intent {
            return Intent(packageContext, ResultActivity::class.java).apply {
                putExtra(EXTRA_SCORES_BY_CHOICE, scoresByChoice)
            }
        }
    }
}