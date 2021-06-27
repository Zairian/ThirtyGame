package com.zairian.thirtygame

// Class that represents the game itself
class ThirtyGame {

    var diceSet = DiceSet()

    var score = 0

    var scoreBySelection = IntArray(10)

    var currentRound = 1

    var currentThrow = 0

    var availableSelections = mutableListOf(
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

    // Handles the event when a dice is selected and toggled between it's saved(grey) state to it's not saved(white) state
    fun toggleSelectedDice(index : Int) {
        diceSet.toggleSelectedDiceColor(index)
    }

    // Handles the Throw Dice event also returns an IntArray with the generated dice that can be used to store the state of the game
    fun throwDice() : IntArray{
        return if(currentThrow < 3){
            currentThrow++
            diceSet.rollDice()
        } else {
            intArrayOf()
        }

    }

    // Handles the end round event
    fun endRound(selectionIndex: Int, selectionString: String) {
        val selectedNumber = convSelectionToInt(selectionString)
        var roundScore = 0

        if(currentRound <= 10){
            val selectedDice = ScoreCalc.getDices(diceSet.getDicesToList(), selectedNumber)
            selectedDice.forEach {
                roundScore += it.sum()
            }

            scoreBySelection[selectedNumber - 3] = roundScore
            score += roundScore

            makeSelection(selectionString)
            availableSelections.removeAt(selectionIndex)

            currentThrow = 0
        }
        currentRound++
    }

    // Helper method used when a selection is made when ending a round
    private fun makeSelection(selectionString: String){
        val selectedNumber = convSelectionToInt(selectionString)

        if(currentThrow > 0) {
            diceSet.toggleBestDiceColor(ScoreCalc.getDices(diceSet.getDicesToList(), selectedNumber))
        }
    }

    // Converts the selection string to Integer values matching the numbers that the user wants summed
    private fun convSelectionToInt(s: String) : Int {
        return if(s == "LOW"){
            3
        } else{
            s.filter { it.isDigit() }.toInt()
        }
    }
}