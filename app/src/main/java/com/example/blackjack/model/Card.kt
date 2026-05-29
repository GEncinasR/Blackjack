package com.example.blackjack.model

import com.example.blackjack.R
enum class Suit {
    CLOVERS, DIAMONDS, HEARTS, SPADES
}

enum class Rank(val value: Int) {
    TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
    JACK(10), QUEEN(10), KING(10), ACE(11)
}
data class Card(
    val rank: Rank,
    val suit: Suit,
    val isFaceUp: Boolean = true
) {
    val drawableRes: Int
        get() = when (suit) {
            Suit.CLOVERS -> when (rank) {
                Rank.TWO -> R.drawable.twoofclovers
                Rank.THREE -> R.drawable.threeofclovers
                Rank.FOUR -> R.drawable.fourofclovers
                Rank.FIVE -> R.drawable.fiveofclovers
                Rank.SIX -> R.drawable.sixofclovers
                Rank.SEVEN -> R.drawable.sevenofclovers
                Rank.EIGHT -> R.drawable.eightofclovers
                Rank.NINE -> R.drawable.nineofclovers
                Rank.TEN -> R.drawable.tenofclovers
                Rank.JACK -> R.drawable.jackofclovers
                Rank.QUEEN -> R.drawable.queenofclovers
                Rank.KING -> R.drawable.kingofclovers
                Rank.ACE -> R.drawable.aceofclovers
            }
            Suit.DIAMONDS -> when (rank) {
                Rank.TWO -> R.drawable.twoofdiamonds
                Rank.THREE -> R.drawable.threeofdiamonds
                Rank.FOUR -> R.drawable.fourofdiamonds
                Rank.FIVE -> R.drawable.fiveofdiamonds
                Rank.SIX -> R.drawable.sixofdiamonds
                Rank.SEVEN -> R.drawable.sevenofdiamonds
                Rank.EIGHT -> R.drawable.eightofdiamonds
                Rank.NINE -> R.drawable.nineofdiamonds
                Rank.TEN -> R.drawable.tenofdiamonds
                Rank.JACK -> R.drawable.jackofdiamonds
                Rank.QUEEN -> R.drawable.queenofdiamonds
                Rank.KING -> R.drawable.kingofdiamonds
                Rank.ACE -> R.drawable.aceofdiamonds
            }
            Suit.HEARTS -> when (rank) {
                Rank.TWO -> R.drawable.twoofhearts
                Rank.THREE -> R.drawable.threeofhearts
                Rank.FOUR -> R.drawable.fourofhearts
                Rank.FIVE -> R.drawable.fiveofhearts
                Rank.SIX -> R.drawable.sixofhearts
                Rank.SEVEN -> R.drawable.sevenofhearts
                Rank.EIGHT -> R.drawable.eightofhearts
                Rank.NINE -> R.drawable.nineofhearts
                Rank.TEN -> R.drawable.tenofhearts
                Rank.JACK -> R.drawable.jackofhearts
                Rank.QUEEN -> R.drawable.queenofhearts
                Rank.KING -> R.drawable.kingofhearts
                Rank.ACE -> R.drawable.aceofhearts
            }
            Suit.SPADES -> when (rank) {
                Rank.TWO -> R.drawable.twoofspades
                Rank.THREE -> R.drawable.threeofspades
                Rank.FOUR -> R.drawable.fourofspades
                Rank.FIVE -> R.drawable.fiveofspades
                Rank.SIX -> R.drawable.sixofspades
                Rank.SEVEN -> R.drawable.sevenofspades
                Rank.EIGHT -> R.drawable.eightofspades
                Rank.NINE -> R.drawable.nineofspades
                Rank.TEN -> R.drawable.tenofspades
                Rank.JACK -> R.drawable.jackofspades
                Rank.QUEEN -> R.drawable.queenofspades
                Rank.KING -> R.drawable.kingofspades
                Rank.ACE -> R.drawable.aceofspades
            }
        }

    val highContrastDrawableRes: Int
        get() = when (suit) {
            Suit.CLOVERS -> when (rank) {
                Rank.TWO -> R.drawable.twoofclovershicontrast
                Rank.THREE -> R.drawable.threeofclovershicontrast
                Rank.FOUR -> R.drawable.fourofclovershicontrast
                Rank.FIVE -> R.drawable.fiveofclovershicontrast
                Rank.SIX -> R.drawable.sixofclovershicontrast
                Rank.SEVEN -> R.drawable.sevenofclovershicontrast
                Rank.EIGHT -> R.drawable.eightofclovershicontrast
                Rank.NINE -> R.drawable.nineofclovershicontrast
                Rank.TEN -> R.drawable.tenofclovershicontrast
                Rank.JACK -> R.drawable.jackofclovershicontrast
                Rank.QUEEN -> R.drawable.queenofclovershicontrast
                Rank.KING -> R.drawable.kingofclovershicontrast
                Rank.ACE -> R.drawable.aceofclovershicontrast
            }
            Suit.DIAMONDS -> when (rank) {
                Rank.TWO -> R.drawable.twoofdiamondshicontrast
                Rank.THREE -> R.drawable.threeofdiamondshicontrast
                Rank.FOUR -> R.drawable.fourofdiamondshicontrast
                Rank.FIVE -> R.drawable.fiveofdiamondshicontrast
                Rank.SIX -> R.drawable.sixofdiamondshicontrast
                Rank.SEVEN -> R.drawable.sevenofdiamondshicontrast
                Rank.EIGHT -> R.drawable.eightofdiamondshicontrast
                Rank.NINE -> R.drawable.nineofdiamondshicontrast
                Rank.TEN -> R.drawable.tenofdiamondshicontrast
                Rank.JACK -> R.drawable.jackofdiamondshicontrast
                Rank.QUEEN -> R.drawable.queenofdiamondshicontrast
                Rank.KING -> R.drawable.kingofdiamondshicontrast
                Rank.ACE -> R.drawable.aceofdiamondshicontrast
            }
            else -> drawableRes
        }
}
