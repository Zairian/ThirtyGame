package com.zairian.thirtygame

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val KEY_SPINNER_SELECTIONS = "spinner_selections"
private const val KEY_DICE_STATE = "dice_state"
private const val KEY_ROUND_STATE = "round_state"
private const val KEY_THROW_STATE = "throw_state"
private const val KEY_SCORE_STATE = "score_state"
private const val KEY_DICE_COLOR_STATE = "selected_dice_state"
private const val KEY_SCORE_BY_SELECTION_STATE = "score_by_selection_state"

class ThirtyViewModel(private val handle: SavedStateHandle) : ViewModel() {

    private var thirtyGame = ThirtyGame()

    var currentSpinnerIndex = 0
    var currentThrow = 0
    var currentRound = 0
    var score = 0
    var scoreBySelection = IntArray(10)

    var spinnerSelections = mutableListOf(
        "LOW",
        "Sum of 4",
        "Sum of 5",
        "Sum of 6",
        "Sum of 7",
        "Sum of 8",
        "Sum of 9",
        "Sum of 10",
        "Sum of 11",
        "Sum of 12"
    )

    var diceColor = mutableListOf(
        "white",
        "white",
        "white",
        "white",
        "white",
        "white"
    )

    fun endRound(selectionIndex:Int, selectionString: String) {
        thirtyGame.endRound(selectionIndex, selectionString)
        handle.set(KEY_ROUND_STATE, thirtyGame.currentRound)
        handle.set(KEY_THROW_STATE, thirtyGame.currentThrow)
        handle.set(KEY_SCORE_STATE, thirtyGame.score)
        handle.set(KEY_SCORE_BY_SELECTION_STATE, thirtyGame.scoreBySelection)
        handle.set(KEY_SPINNER_SELECTIONS, ArrayList(thirtyGame.availableSelections))
        spinnerSelections = thirtyGame.availableSelections

        for (i in diceColor.indices) {
            diceColor[i] = thirtyGame.diceSet.getDiceToString(i).filter { !it.isDigit() }
        }
        handle.set(KEY_DICE_COLOR_STATE, ArrayList(diceColor))

        updateViewModelValues()
    }

    fun throwDice() {
        handle.set(KEY_DICE_STATE, thirtyGame.throwDice())
        handle.set(KEY_THROW_STATE, thirtyGame.currentThrow)
        updateViewModelValues()
    }

    fun toggleSelectedDice(index: Int) {
        thirtyGame.toggleSelectedDice(index)
        diceColor[index] = thirtyGame.diceSet.getDiceToString(index).filter { !it.isDigit() }
        handle.set(KEY_DICE_COLOR_STATE, ArrayList(diceColor))
    }

    fun getDiceImageName(index: Int) : String {
        return thirtyGame.diceSet.getDiceToString(index)
    }

    private fun updateViewModelValues() {
        spinnerSelections = thirtyGame.availableSelections
        currentRound = thirtyGame.currentRound
        currentThrow = thirtyGame.currentThrow
        score = thirtyGame.score
        scoreBySelection = thirtyGame.scoreBySelection
    }

    init {
        if(handle.contains(KEY_SPINNER_SELECTIONS)) {
            val prevSpinnerSelections = handle.get<ArrayList<String>>(KEY_SPINNER_SELECTIONS)
            if (prevSpinnerSelections != null) {
                thirtyGame.availableSelections = prevSpinnerSelections

            }
        }
        if(handle.contains(KEY_DICE_STATE)) {
            val prevDiceNumbers = handle.get<IntArray>(KEY_DICE_STATE)
            if (prevDiceNumbers != null) {
                thirtyGame.diceSet.setAllDiceNumbers(prevDiceNumbers)
            }
        }
        if(handle.contains(KEY_ROUND_STATE)) {
            val prevRoundNumber = handle.get<Int>(KEY_ROUND_STATE)
            if (prevRoundNumber != null) {
                thirtyGame.currentRound = prevRoundNumber
            }
        }
        if(handle.contains(KEY_THROW_STATE)) {
            val prevThrowNumber = handle.get<Int>(KEY_THROW_STATE)
            if (prevThrowNumber != null) {
                thirtyGame.currentThrow = prevThrowNumber
            }
        }
        if(handle.contains(KEY_SCORE_STATE)) {
            val prevScoreNumber = handle.get<Int>(KEY_SCORE_STATE)
            if (prevScoreNumber != null) {
                thirtyGame.score = prevScoreNumber
            }
        }
        if(handle.contains(KEY_DICE_COLOR_STATE)) {
            val prevDiceColors = handle.get<ArrayList<String>>(KEY_DICE_COLOR_STATE)
            if (prevDiceColors != null) {
                thirtyGame.diceSet.setAllDiceColors(prevDiceColors)
            }
        }
        if(handle.contains(KEY_SCORE_BY_SELECTION_STATE)) {
            val prevScoreBySelection = handle.get<IntArray>(KEY_SCORE_BY_SELECTION_STATE)
            if (prevScoreBySelection != null) {
                thirtyGame.scoreBySelection = prevScoreBySelection
            }
        }
        updateViewModelValues()
    }
}