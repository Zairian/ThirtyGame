package com.zairian.thirtygame

// Class that calculates score and the best combination of a round
class ScoreCalc {
    companion object {

        /**
         * Public function. Returns a [MutableList] containing the best combination of dices based on [selectedSum]
         */
        fun getDices(dices: List<Int>, selectedSum: Int): MutableList<MutableList<Int>> {
            return when {
                selectedSum == 3 -> getLowCombination(dices)
                selectedSum > 3 -> getBestCombinations(dices, selectedSum)
                else -> mutableListOf()
            }
        }

        /**
         * Returns a [MutableList] containing the best combinations of [dices] based on the [selectedSum]
         */
        private fun getBestCombinations(dices:List<Int>, selectedSum: Int) : MutableList<MutableList<Int>>{
            val combinations = calcScoreCombinations(dices, selectedSum)
            var bestResult = mutableListOf<MutableList<Int>>()

            for (i in 0 until combinations.size){
                val tempDiceList = dices.toMutableList()
                val tempResult = mutableListOf<MutableList<Int>>()

                if(checkListsFreqSensitive(combinations[i], tempDiceList)){
                    tempResult.add(combinations[i])
                    combinations[i].forEach {
                        tempDiceList.remove(it)
                    }
                }

                for (k in i+1 until combinations.size){
                    if(checkListsFreqSensitive(combinations[k], tempDiceList)){
                        tempResult.add(combinations[k])
                        combinations[k].forEach {
                            tempDiceList.remove(it)
                        }
                    }
                }

                if(tempResult.size > bestResult.size){
                    bestResult = tempResult.toMutableList()
                }
            }
            return bestResult
        }

        /**
         * Returns true if the [MutableList] of [dices] contains all of the content of [combination] sensitive of frequency.
         */
        private fun checkListsFreqSensitive(combination: MutableList<Int>, dices: MutableList<Int>) : Boolean{
            var checker = true
            combination.forEach { d ->
                if(dices.count {it == d} < combination.count {it == d}){
                    checker = false
                }
            }
            return checker
        }

        /**
         * Returns a MutableList containing [MutableList] objects representing all the combination of dice that equals the [selectedSum]
         */
        private fun calcScoreCombinations(
            dices: List<Int>,
            selectedSum: Int
        ): MutableList<MutableList<Int>> {
            return calcScoreCombinations(dices, mutableListOf(), mutableListOf(), 0, selectedSum)
        }

        /**
         * Recursive helper function for [calcScoreCombinations]
         */
        private fun calcScoreCombinations(
            allDices: List<Int>,
            dices: MutableList<Int>,
            matchingSums: MutableList<MutableList<Int>>,
            index: Int,
            selectedSum: Int
        ): MutableList<MutableList<Int>> {
            when {
                dices.sum() == selectedSum -> {
                    val tempList = mutableListOf<Int>()
                    for (i in 0 until dices.size) {
                        tempList.add(dices[i])
                    }
                    matchingSums.add(tempList)
                    if(dices.isNotEmpty()){
                        dices.removeLast()
                    }
                    return matchingSums
                }
                dices.sum() > selectedSum -> {
                    if(dices.isNotEmpty()){
                        dices.removeLast()
                    }
                    return matchingSums
                }
                else -> {
                    var curIndex = index
                    allDices.forEach { _ ->
                        if (curIndex < allDices.size) {
                            dices.add(allDices[curIndex])
                            curIndex++
                            calcScoreCombinations(
                                allDices,
                                dices,
                                matchingSums,
                                curIndex,
                                selectedSum
                            )
                        }
                    }
                    if (dices.isNotEmpty()) {
                        dices.removeLast()
                    }
                    return matchingSums
                }
            }
        }

        /**
         * Return a [MutableList] containing combination of numbers 3 or lower
         */
        private fun getLowCombination(dices: List<Int>): MutableList<MutableList<Int>> {
            val result = mutableListOf<MutableList<Int>>()
            dices.forEach {
                if (it <= 3) {
                    result.add(mutableListOf(it))
                }
            }
            return result
        }
    }

}