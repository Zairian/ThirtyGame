package com.zairian.thirtygame

//Class that represents a set of 6 dice
class DiceSet {

    private var dices = listOf(
        Dice(),
        Dice(),
        Dice(),
        Dice(),
        Dice(),
        Dice()
    )

    // Rolls the dice and resets colors if grey or red to white
    fun rollDice() : IntArray {
        var counter = 0
        dices.forEach {
            if (it.color == "white" || it.color == "red"){
                throwDice(counter)
            }
            if(it.color == "red") {
                it.color = "white"
            }
            counter++
        }
        return getDicesToIntArray()
    }

    // Sets all dice values to the values specified in the Int Array. Used when recovering from a destroyed activity or state changes.
    fun setAllDiceNumbers(dices : IntArray) {
        for(i in dices.indices) {
            this.dices[i].number = dices[i]
        }
    }

    fun setAllDiceColors(colors : ArrayList<String>) {
        for(i in colors.indices) {
            dices[i].color = colors[i]
        }
    }

    // Toggles the color of a selected/unselected dice.
    fun toggleSelectedDiceColor(index : Int){
        when (dices[index].color) {
            "grey" -> dices[index].color = "white"
            else -> dices[index].color = "grey"
        }
    }

    // Toggles the colors of the dices that have the best combination to red.
    fun toggleBestDiceColor(bestDices : MutableList<MutableList<Int>>){
        dices.forEach {
            it.color = "white"
        }

        bestDices.forEach {
            it.forEach {
                val d = dices.find { n -> n.number == it && n.color != "red"}
                if (d != null) {
                    d.color = "red"
                }
            }
        }
    }

    // Returns the dices as a list of integers used for calculations of score etc.
    fun getDicesToList() : List<Int> {
        val diceList = mutableListOf<Int>()
        dices.forEach {
            diceList.add(it.number)
        }
        return diceList.toList()
    }

    // Returns a dice as a string. Used when determining what imager resource should bne used for
    // the ImageView representing the dice
    fun getDiceToString(index: Int) : String{
        return dices[index].color + dices[index].number
    }

    // Helper method that throws a dice.
    private fun throwDice(index : Int) {
        dices[index].number = (Math.random()*6+1).toInt()
    }

    // Helper method that returns all dices as an IntArray
    private fun getDicesToIntArray() : IntArray {
        val diceArray = IntArray(6)
        for (i in diceArray.indices) {
            diceArray[i] = dices[i].number
        }
        return diceArray
    }
}