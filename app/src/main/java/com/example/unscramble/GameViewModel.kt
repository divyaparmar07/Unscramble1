package com.example.unscramble

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DataViewState())
    val uiState = _uiState.asStateFlow()

    var wordCount: Int by mutableStateOf(1)
        private set

    var score: Int by mutableStateOf(0)
        private set

    var currentWord: String by mutableStateOf("")
        private set

    var _editTextWord1 by mutableStateOf("")
        private set

    fun textChanged(text: String) {
        _editTextWord1 = text
    }

    init {
        Game()
    }

    fun Game() {
        score = 0
        wordCount = 1
        _uiState.value = DataViewState(scrambleWord = takeStringRandomly())
    }

    fun takeStringRandomly(): String {
        currentWord = allWords.random()
        return takeStringRandomly1(currentWord)
    }

    fun takeStringRandomly1(word: String): String {
        val charArray = word.toCharArray()
        charArray.sortDescending()
        val resultingString = charArray.joinToString(separator = "")
        _uiState.value = _uiState.value.copy(scrambleWord = resultingString)
        return resultingString
    }

    fun updateUserGuess(word: String) {
        _editTextWord1 = word
    }

    fun submitButtonClicked() {
        if (currentWord == _editTextWord1) {
            if (wordCount < 11) {
                wordCount += 1
                score += 20
                _uiState.update { currentState ->
                    currentState.copy(userGuessWrong = false)
                }
                takeStringRandomly()
            }
        } else if (currentWord != _editTextWord1) {
            _uiState.update { currentState ->
                currentState.copy(userGuessWrong = true)
            }
        }
        updateUserGuess("")
    }

    fun skipButtonClicked() {
        if (wordCount < 11) {
            wordCount += 1
            _uiState.update { currentState ->
                currentState.copy(userGuessWrong = false)
            }
            takeStringRandomly()
        }
        updateUserGuess("")
    }

}