package com.zairian.thirtygame

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var diceButtons: List<ImageView>
    private lateinit var endRoundButton: Button
    private lateinit var throwButton: Button
    private lateinit var scoringSpinner: Spinner

    private lateinit var throwTextView: TextView
    private lateinit var roundTextView: TextView
    private lateinit var scoreTextView: TextView

    private lateinit var arrAdapter: ArrayAdapter<String>

    private lateinit var thirtyViewModel : ThirtyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Sets a toolbar for the activity
        setSupportActionBar(findViewById(R.id.main_activity_toolbar))

        // Initializes ViewModel
        thirtyViewModel = ViewModelProvider(this).get(ThirtyViewModel::class.java)

        throwTextView = findViewById(R.id.throw_text_view)
        roundTextView = findViewById(R.id.round_text_view)
        scoreTextView = findViewById(R.id.score_text_view)

        initDiceButtons()
        initScoringSpinner()
        initThrowButton()
        initEndRoundButton()

        updateGUI()
    }

    //Inflates the custom made menu resource when creating the Options Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return true
    }

    //Called when an option in the toolbar/options menu is selected. Handles on click events for menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_information -> {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle(R.string.alert_dialog_title)
            builder.setMessage(R.string.alert_dialog_content)
            builder.setPositiveButton(R.string.alert_dialog_positive_button) { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    // Initializes the dice ImageViews and binds listeners to them
    private fun initDiceButtons(){
        diceButtons = listOf(
            findViewById(R.id.dice_button1),
            findViewById(R.id.dice_button2),
            findViewById(R.id.dice_button3),
            findViewById(R.id.dice_button4),
            findViewById(R.id.dice_button5),
            findViewById(R.id.dice_button6)
        )

        diceButtons.forEach {
                button ->
            button.setOnClickListener {
                thirtyViewModel.toggleSelectedDice(Integer.parseInt(button.tag.toString()))
                updateDice()
            }
        }
    }

    // Initializes the Spinner that handles the scoring options for the game.
    private fun initScoringSpinner(){
        scoringSpinner = findViewById(R.id.scoring_spinner)
        arrAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, thirtyViewModel.spinnerSelections)
        arrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        scoringSpinner.adapter = arrAdapter

        scoringSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                thirtyViewModel.currentSpinnerIndex = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                thirtyViewModel.spinnerSelections.add("")
            }
        }
    }

    // Initializes the button that will throw the dice when pressed
    private fun initThrowButton(){
        throwButton = findViewById(R.id.throw_button)

        throwButton.setOnClickListener {
            thirtyViewModel.throwDice()
            updateGUI()
        }
    }

    // Initializes the End Round Button. Also binds listener that handles logic regarding if the
    // button should call the End Game event or not depending on rounds played
    private fun initEndRoundButton(){
        endRoundButton = findViewById(R.id.end_round_button)

        endRoundButton.setOnClickListener {
            when (thirtyViewModel.currentRound) {
                10 -> {
                    thirtyViewModel.endRound(thirtyViewModel.currentSpinnerIndex, scoringSpinner.selectedItem.toString())
                    scoringSpinner.setSelection(0)
                    endRoundButton.text = getString(R.string.end_game_button)
                    updateGUI()
                }
                11 -> {
                    endRoundButton.text = getString(R.string.end_game_button)
                    val intent = ResultActivity.newIntent(this@MainActivity, thirtyViewModel.scoreBySelection)
                    startActivity(intent)
                    updateGUI()
                }
                else -> {
                    thirtyViewModel.endRound(thirtyViewModel.currentSpinnerIndex, scoringSpinner.selectedItem.toString())
                    scoringSpinner.setSelection(0)
                    updateGUI()
                }
            }
        }
    }

    // Updates the image resource of all Dice ImageViews
    private fun updateDice(){
        diceButtons.forEach {
            val imageName = thirtyViewModel.getDiceImageName(Integer.parseInt(it.tag.toString()))
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            it.setImageResource(resID)
        }
    }

    // Updates all graphical elements and disables buttons/elements depending on throws/rounds
    private fun updateGUI() {
        val curThrow = "Throw: " + thirtyViewModel.currentThrow + " out of 3"
        throwTextView.text = curThrow

        if(thirtyViewModel.currentRound <= 10) {
            val curRound = "Round: " + thirtyViewModel.currentRound + " out of 10"
            roundTextView.text = curRound
        }

        val curScore ="Score: " + thirtyViewModel.score
        scoreTextView.text = curScore

        if(thirtyViewModel.currentRound > 10) {
            throwButton.isEnabled = false
            endRoundButton.isEnabled = true
            scoringSpinner.isEnabled = false
            diceButtons.forEach {
                it.isEnabled = false
            }
        }

        if(thirtyViewModel.currentThrow == 0 && thirtyViewModel.currentRound <= 10){
            endRoundButton.isEnabled = false
            scoringSpinner.isEnabled = false
            diceButtons.forEach {
                it.isEnabled = false
            }
        } else {
            endRoundButton.isEnabled = true
            scoringSpinner.isEnabled = true
            diceButtons.forEach {
                it.isEnabled = true
            }
        }

        arrAdapter.notifyDataSetChanged()

        updateDice()
    }

    // Companion object that returns the intent used when creating a new game (essentially restarting the MainActivity)
    companion object{
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, MainActivity::class.java)
        }
    }
}