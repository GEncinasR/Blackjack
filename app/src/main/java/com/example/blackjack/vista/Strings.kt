package com.example.blackjack.vista

object Strings {
    const val appTitle = "FunkJack"
    const val menuJugar = "JUGAR"
    const val menuScores = "SCORES"
    const val menuHelp = "?"
    const val menuCdPlay = "Jugar"


    const val gameDealerLabel = "Croupier"
    const val gameScorePrefix = "Puntos: "
    const val gameUnknownScore = "?"
    const val gamePlayerLabel = "Tus cartas"
    const val gameHit = "Pedir"
    const val gameStand = "Plantarse"
    const val gamePlayAgain = "Jugar de Nuevo"
    const val gameBackToMenu = "Volver al Menu"


    const val configTitle = "Configuracion"
    const val configTargetScore = "Puntaje objetivo (ej. 11, 21, 25):"
    const val configHighContrast = "Cartas de alto contraste"
    const val configCardSounds = "Sonidos de cartas"
    const val configSfxVolume = "Volumen de efectos"
    const val configMusic = "Música"
    const val configMusicVolume = "Volumen de música"
    const val configLegacyMode = "Modo clásico"
    const val configSound = "Sonido"
    const val configColors = "Colores"

    const val soundTitle = "Configuracion de Sonido"

    const val colorTitle = "Configuracion de Colores"
    const val colorSchemeLabel = "Tema de colores"
    val colorSchemes = listOf("Verde", "Azul", "Rojo", "Morado", "Naranja", "Gris")

    const val scoreboardTitle = "Puntajes"
    const val instructionsTitle = "Instrucciones"

    const val instructionsText =
        "- El objetivo es acercarte lo mas posible a 21 sin pasarte, superando la mano del crupier.\n\n" +
                "- Las cartas numericas valen su numero. Las figuras (J, Q, K) valen 10. El As vale 1 u 11, lo que te convenga.\n\n" +
                "- Recibes dos cartas al inicio. El crupier tambien recibe dos, pero una queda boca abajo hasta el final.\n\n" +
                "- Tus opciones son 'Pedir' y 'Plantarse'. 'Pedir' es tomar otra carta. 'Plantarse' es quedarte con tu mano actual.\n\n" +
                "- Si tu mano supera 21, te pasas y pierdes de inmediato, sin importar lo que tenga el crupier.\n\n" +
                "- Blackjack natural: un As mas una figura o 10 con las dos primeras cartas. Gana sobre cualquier otro 21.\n\n" +
                "- El crupier esta obligado a pedir carta con 16 o menos, y a plantarse con 17 o mas.\n\n" +
                "- Gana quien tenga la mano mas alta sin pasarse. Si empatan, se recupera lo apostado.\n\n" +
                "-ok, Que la suerte te acompañe."

    const val msgPlayerBust = "Te pasaste."
    const val msgDealerBust = "Se paso el Croupier!"
    const val msgPlayerWin = "Ganaste!"
    const val msgDealerWin = "Gana el Croupier."
    const val msgTie = "Empate"
}
