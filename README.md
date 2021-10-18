# ThirtyGame
A mobile (android) version of the game Thirty programmed in Kotlin. A dice game where you throw dice 30 times and collect points in different categories. Points are automatically calculated with a custom made algorithm found in app/src/main/java/com/zairian/thirtygame/ScoreCalc.kt  

Rules of the game can be found below:  
Thirty is a dice game not entirely different from Yatzy where you roll six dice in rounds.  

At each turn, the player can choose which dice he wants to keep and re-roll the dice he wants two times. After the three rolls, the points for the dice are calculated.  

The player has 10 choices on how he can calculate the current round's points.  
LOW - Counts the dots of all the current dice that has a value of 3 or lower.  
Sum of 4 - Counts all the dice which combined gives a sum of 4.  
Sum of 5 - Counts all the dice which combined gives a sum of 5 etc.  
After a choice has been made it can't be chosen again for the rest of the game.  
A game then consists of 10 such rounds. We will thus make a total of a maximum of 30 throws, hence the name of the game.

![bild](https://user-images.githubusercontent.com/78047648/137811106-d5a78280-e75c-4130-8670-9a0ce2cc6230.png)
