package com.zcf.drawgame.model


data class Quiz(val drawer: String, val question: String, val options: List<String>) {
    companion object {
        val ROLE_DRAWER = "drawer"
        val ROLE_GUESSER = "guesser"
    }

}
