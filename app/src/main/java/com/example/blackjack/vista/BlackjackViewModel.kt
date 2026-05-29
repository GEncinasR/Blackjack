package com.example.blackjack.vista

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.blackjack.R
import com.example.blackjack.data.AppDatabase
import com.example.blackjack.data.GameResult
import com.example.blackjack.model.Card
import com.example.blackjack.model.Rank
import com.example.blackjack.model.Suit
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BlackjackViewModel(application: Application) : AndroidViewModel(application) {
    private val gameDao = AppDatabase.getDatabase(application).gameDao()
    private val prefs = application.getSharedPreferences("blackjack_settings", Context.MODE_PRIVATE)
    private var musicPlayer: MediaPlayer? = null
    private var oldMusicPlayer: MediaPlayer? = null
    private var fadeJob: Job? = null
    private var musicPosition: Int = 0
    private var currentMusicTrack: Int? = null
    private var currentMusicScreen: String? = null
    private var soundPlayer: MediaPlayer? = null
    private var lastNavTime = 0L
    private var isResetting = false

    companion object {
        private const val NAV_DEBOUNCE_MS = 400L
    }

    var playerHand by mutableStateOf<List<Card>>(emptyList())
        private set
    var dealerHand by mutableStateOf<List<Card>>(emptyList())
        private set
    var deck by mutableStateOf<List<Card>>(emptyList())
        private set
    var isGameOver by mutableStateOf(false)
        private set
    var gameStatusMessage by mutableStateOf("")
        private set
    var hasHit by mutableStateOf(false)
        private set
    var dealingPhase by mutableStateOf(0)
        private set
    var hitAnimating by mutableStateOf(false)
        private set
    var dealerAnimating by mutableStateOf(false)
        private set
    val isDealing get() = dealingPhase in 1..2 || hitAnimating || dealerAnimating

    var targetValue: Int
        get() = _targetValue.value
        set(value) {
            _targetValue.value = value
            prefs.edit().putInt("target_value", value).apply()
        }
    private val _targetValue = mutableStateOf(prefs.getInt("target_value", 21))

    var highContrast: Boolean
        get() = _highContrast.value
        set(value) {
            _highContrast.value = value
            prefs.edit().putBoolean("high_contrast", value).apply()
        }
    private val _highContrast = mutableStateOf(prefs.getBoolean("high_contrast", false))

    var cardSounds: Boolean
        get() = _cardSounds.value
        set(value) {
            _cardSounds.value = value
            prefs.edit().putBoolean("card_sounds", value).apply()
        }
    private val _cardSounds = mutableStateOf(prefs.getBoolean("card_sounds", true))

    var music: Boolean
        get() = _music.value
        set(value) {
            _music.value = value
            prefs.edit().putBoolean("music", value).apply()
            if (value) {
                updateMusicTrack()
            } else {
                stopMusic()
            }
        }
    private val _music = mutableStateOf(prefs.getBoolean("music", false))

    var isFirstRun: Boolean
        get() = _isFirstRun.value
        set(value) {
            _isFirstRun.value = value
            prefs.edit().putBoolean("first_run", value).apply()
        }
    private val _isFirstRun = mutableStateOf(prefs.getBoolean("first_run", true))

    var legacyMode: Boolean
        get() = _legacyMode.value
        set(value) {
            _legacyMode.value = value
            prefs.edit().putBoolean("legacy_mode", value).apply()
        }
    private val _legacyMode = mutableStateOf(prefs.getBoolean("legacy_mode", false))

    var colorScheme: String
        get() = _colorScheme.value
        set(value) {
            _colorScheme.value = value
            prefs.edit().putString("color_scheme", value).apply()
        }
    private val _colorScheme = mutableStateOf(prefs.getString("color_scheme", "Verde") ?: "Verde")

    var musicVolume: Float
        get() = _musicVolume.value
        set(value) {
            _musicVolume.value = value
            prefs.edit().putFloat("music_volume", value).apply()
            musicPlayer?.setVolume(value, value)
        }
    private val _musicVolume = mutableStateOf(prefs.getFloat("music_volume", 0.8f))

    var sfxVolume: Float
        get() = _sfxVolume.value
        set(value) {
            _sfxVolume.value = value
            prefs.edit().putFloat("sfx_volume", value).apply()
        }
    private val _sfxVolume = mutableStateOf(prefs.getFloat("sfx_volume", 0.8f))

    fun onScreenChanged(route: String) {
        currentMusicScreen = route
        if (music) updateMusicTrack()
    }

    fun navigateSafe(navController: NavController, route: String) {
        val now = System.currentTimeMillis()
        if (now - lastNavTime < NAV_DEBOUNCE_MS) return
        lastNavTime = now
        navController.navigate(route) { launchSingleTop = true }
    }

    fun popBackSafe(navController: NavController) {
        val now = System.currentTimeMillis()
        if (now - lastNavTime < NAV_DEBOUNCE_MS) return
        lastNavTime = now
        navController.popBackStack()
    }

    private fun getTrackForScreen(route: String): Int? = when (route) {
        "menu", "game" -> R.raw.main
        "instructions", "config", "sound_config", "color_config", "scoreboard" -> R.raw.settings
        else -> null
    }

    /*
     * Versión original para referencia
     *
    private fun updateMusicTrack() {
        val screen = currentMusicScreen ?: return
        val trackRes = getTrackForScreen(screen) ?: return stopMusic()

        if (currentMusicTrack == trackRes) return

        musicPlayer?.let { player ->
            musicPosition = player.currentPosition
            player.release()
        }
        currentMusicTrack = trackRes
        musicPlayer = try {
            MediaPlayer.create(getApplication(), trackRes)?.apply {
                seekTo(musicPosition)
                setVolume(musicVolume, musicVolume)
                isLooping = true
                start()
            }
        } catch (_: Exception) { null }
    }
    */

    private fun updateMusicTrack() {
        val screen = currentMusicScreen ?: return
        val trackRes = getTrackForScreen(screen) ?: return stopMusic()
        if (currentMusicTrack == trackRes) return

        fadeJob?.cancel()
        oldMusicPlayer?.release()
        oldMusicPlayer = null

        musicPosition = musicPlayer?.currentPosition ?: 0
        oldMusicPlayer = musicPlayer
        currentMusicTrack = trackRes
        musicPlayer = try {
            MediaPlayer.create(getApplication(), trackRes)?.apply {
                seekTo(musicPosition)
                setVolume(0f, 0f)
                isLooping = true
                start()
            }
        } catch (_: Exception) { null }

        if (oldMusicPlayer != null || musicPlayer != null) {
            fadeJob = viewModelScope.launch {
                val steps = 8
                for (i in 1..steps) {
                    val progress = i.toFloat() / steps
                    musicPlayer?.setVolume(progress * musicVolume, progress * musicVolume)
                    oldMusicPlayer?.setVolume((1f - progress) * musicVolume, (1f - progress) * musicVolume)
                    delay(50)
                }
                oldMusicPlayer?.release()
                oldMusicPlayer = null
                musicPlayer?.setVolume(musicVolume, musicVolume)
            }
        }
    }

    /*
     * Versión original para referencia
     *
    private fun stopMusic() {
        musicPlayer?.let { player ->
            musicPosition = player.currentPosition
            player.release()
        }
        musicPlayer = null
        currentMusicTrack = null
    }
    */

    private fun stopMusic() {
        fadeJob?.cancel()
        fadeJob = null
        oldMusicPlayer?.release()
        oldMusicPlayer = null
        musicPlayer?.let { player ->
            musicPosition = player.currentPosition
            player.release()
        }
        musicPlayer = null
        currentMusicTrack = null
    }

    /*
     * Versión original para referencia
     *
    fun pauseMusic() {
        musicPlayer?.let { player ->
            if (player.isPlaying) {
                musicPosition = player.currentPosition
                player.pause()
            }
        }
    }
    */

    fun pauseMusic() {
        musicPlayer?.let { player ->
            if (player.isPlaying) {
                musicPosition = player.currentPosition
                player.pause()
            }
        }
        oldMusicPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
            }
        }
    }

    fun resumeMusic() {
        if (music && currentMusicTrack != null) {
            updateMusicTrack()
        }
    }

    override fun onCleared() {
        super.onCleared()
        fadeJob?.cancel()
        oldMusicPlayer?.release()
        oldMusicPlayer = null
        musicPlayer?.release()
        musicPlayer = null
        soundPlayer?.release()
        soundPlayer = null
    }

    private fun playSound(resId: Int) {
        if (!cardSounds) return
        soundPlayer?.release()
        soundPlayer = try {
            MediaPlayer.create(getApplication(), resId)?.apply {
                setVolume(sfxVolume, sfxVolume)
                setOnCompletionListener { release() }
                start()
            }
        } catch (_: Exception) { null }
    }

    fun playButtonClick() {
        if (!cardSounds) return
        soundPlayer?.release()
        soundPlayer = try {
            MediaPlayer.create(getApplication(), R.raw.buttonclick)?.apply {
                setVolume(sfxVolume, sfxVolume)
                setOnCompletionListener { release() }
                start()
            }
        } catch (_: Exception) { null }
    }
    private fun playFirstDealSounds() {
        if (!cardSounds) return
        soundPlayer?.release()
        soundPlayer = try {
            MediaPlayer.create(getApplication(), R.raw.firstcard)?.apply {
                setVolume(sfxVolume, sfxVolume)
                setOnCompletionListener {
                    it.release()
                    MediaPlayer.create(getApplication(), R.raw.secondcard)?.apply {
                        setVolume(sfxVolume, sfxVolume)
                        setOnCompletionListener { release() }
                        start()
                    }
                }
                start()
            }
        } catch (_: Exception) { null }
    }


    fun resetGame() {
        if (isResetting) return
        isResetting = true

        val newDeck = mutableListOf<Card>()
        for (suit in Suit.entries) {
            for (rank in Rank.entries) {
                newDeck.add(Card(rank, suit))
            }
        }
        newDeck.shuffle()
        deck = newDeck
        isGameOver = false
        gameStatusMessage = ""
        hasHit = false
        dealingPhase = 0
        hitAnimating = false
        dealerAnimating = false

        val pc1 = drawCard()
        val pc2 = drawCard()
        val dc1 = drawCard()
        val dc2 = drawCard()

        playerHand = listOf(pc1.copy(isFaceUp = false))
        dealerHand = listOf(dc1.copy(isFaceUp = false))
        dealingPhase = 1

        playFirstDealSounds()

        viewModelScope.launch {
            try {
                delay(400)
                playerHand = listOf(pc1.copy(isFaceUp = false), pc2.copy(isFaceUp = false))
                dealerHand = listOf(dc1.copy(isFaceUp = false), dc2.copy(isFaceUp = false))
                dealingPhase = 2

                delay(400)
                playerHand = listOf(pc1, pc2)
                dealerHand = listOf(dc1.copy(isFaceUp = false), dc2)
                dealingPhase = 3

                if (calculateScore(playerHand) == targetValue) {
                    dealerHand = dealerHand.map { it.copy(isFaceUp = true) }
                    determineWinner()
                }
            } finally {
                isResetting = false
            }
        }
    }


    private fun drawCard(): Card {
        if (deck.isEmpty()) return Card(Rank.ACE, Suit.SPADES)
        val card = deck.first()
        deck = deck.drop(1)
        return card
    }

    fun hit() {
        if (isGameOver || hitAnimating) return
        hasHit = true
        val card = drawCard()
        playerHand = playerHand + card.copy(isFaceUp = false)
        playSound(R.raw.hit)
        hitAnimating = true

        viewModelScope.launch {
            delay(200)
            if (hitAnimating) {
                playerHand = playerHand.toMutableList().also { it[it.lastIndex] = card }
                hitAnimating = false
                if (calculateScore(playerHand) > targetValue) {
                    didPlayerWin = false
                    endGame(Strings.msgPlayerBust)
                }
            }
        }
    }

    fun stand() {
        if (isGameOver || hitAnimating || dealerAnimating) return


        dealerHand = dealerHand.mapIndexed { index, card ->
            if (index == 0) card.copy(isFaceUp = true) else card
        }

        viewModelScope.launch {
            dealerAnimating = true
            while (calculateScore(dealerHand) < targetValue - 4) {
                val card = drawCard()
                dealerHand = dealerHand + card.copy(isFaceUp = false)
                delay(300)
                dealerHand = dealerHand.toMutableList().also { it[it.lastIndex] = card }
            }
            dealerAnimating = false
            determineWinner()
        }
    }

    private fun determineWinner() {
        val playerScore = calculateScore(playerHand)
        val dealerScore = calculateScore(dealerHand)

        val message = when {
            dealerScore > targetValue -> {
                didPlayerWin = true
                Strings.msgDealerBust
            }
            playerScore > dealerScore -> {
                didPlayerWin = true
                Strings.msgPlayerWin
            }
            dealerScore > playerScore -> {
                didPlayerWin = false
                Strings.msgDealerWin
            }
            else -> {
                didPlayerWin = null
                Strings.msgTie
            }
        }
        endGame(message)
    }

    private var didPlayerWin: Boolean? = null


    private fun endGame(message: String) {
        dealerHand = dealerHand.map { it.copy(isFaceUp = true) }
        isGameOver = true
        gameStatusMessage = message
        saveResult()
        if (!cardSounds || didPlayerWin == null) return
        playSound(if (didPlayerWin!!) R.raw.win else R.raw.lose)
    }


    fun calculateScore(hand: List<Card>): Int {
        var score = 0
        var aces = 0
        for (card in hand) {
            score += card.rank.value
            if (card.rank == Rank.ACE) aces++
        }
        while (score > targetValue && aces > 0) {
            score -= 10
            aces--
        }
        return score
    }


    private fun saveResult() {
        //nombres: pal siguiente sprint
        val winner = if (didPlayerWin == true) "Player" else "Dealer"
        val loser = if (winner == "Player") "Dealer" else "Player"
        val winnerHand = if (didPlayerWin == true) playerHand else dealerHand
        val cardsString = winnerHand.joinToString { "${it.rank} of ${it.suit}" }

        viewModelScope.launch {
            gameDao.insertResult(
                GameResult(
                    winnerName = winner,
                    loserName = loser,
                    date = System.currentTimeMillis(),
                    winningCards = cardsString,
                    playerScore = calculateScore(playerHand),
                    dealerScore = calculateScore(dealerHand)
                )
            )
        }
    }
}

data class ColorPalette(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
)

fun paletteForScheme(name: String): ColorPalette = when (name) {
    "Azul" -> ColorPalette(
        primary = Color(0xFF42A5F5),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFBBDEFB),
        onPrimaryContainer = Color(0xFF0D47A1),
        secondary = Color(0xFF64B5F6),
        onSecondary = Color.White,
        background = Color(0xFF0D47A1),
        onBackground = Color.White,
        surface = Color(0xFF1565C0),
        onSurface = Color.White,
        surfaceVariant = Color(0xFF1976D2),
        onSurfaceVariant = Color.White,
    )
    "Rojo" -> ColorPalette(
        primary = Color(0xFFEF5350),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFFFCDD2),
        onPrimaryContainer = Color(0xFFB71C1C),
        secondary = Color(0xFFE57373),
        onSecondary = Color.White,
        background = Color(0xFFB71C1C),
        onBackground = Color.White,
        surface = Color(0xFFC62828),
        onSurface = Color.White,
        surfaceVariant = Color(0xFFD32F2F),
        onSurfaceVariant = Color.White,
    )
    "Morado" -> ColorPalette(
        primary = Color(0xFFAB47BC),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFE1BEE7),
        onPrimaryContainer = Color(0xFF4A148C),
        secondary = Color(0xFFCE93D8),
        onSecondary = Color.White,
        background = Color(0xFF4A148C),
        onBackground = Color.White,
        surface = Color(0xFF6A1B9A),
        onSurface = Color.White,
        surfaceVariant = Color(0xFF7B1FA2),
        onSurfaceVariant = Color.White,
    )
    "Naranja" -> ColorPalette(
        primary = Color(0xFFFF7043),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFFFCCBC),
        onPrimaryContainer = Color(0xFFBF360C),
        secondary = Color(0xFFFF8A65),
        onSecondary = Color.White,
        background = Color(0xFFBF360C),
        onBackground = Color.White,
        surface = Color(0xFFD84315),
        onSurface = Color.White,
        surfaceVariant = Color(0xFFE64A19),
        onSurfaceVariant = Color.White,
    )
    "Gris" -> ColorPalette(
        primary = Color(0xFF78909C),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFCFD8DC),
        onPrimaryContainer = Color(0xFF37474F),
        secondary = Color(0xFF90A4AE),
        onSecondary = Color.White,
        background = Color(0xFF37474F),
        onBackground = Color.White,
        surface = Color(0xFF455A64),
        onSurface = Color.White,
        surfaceVariant = Color(0xFF546E7A),
        onSurfaceVariant = Color.White,
    )
    else -> ColorPalette(
        primary = Color(0xFF4CAF50),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFA5D6A7),
        onPrimaryContainer = Color(0xFF1B5E20),
        secondary = Color(0xFF66BB6A),
        onSecondary = Color.White,
        background = Color(0xFF1B5E20),
        onBackground = Color.White,
        surface = Color(0xFF2E7D32),
        onSurface = Color.White,
        surfaceVariant = Color(0xFF388E3C),
        onSurfaceVariant = Color.White,
    )
}
