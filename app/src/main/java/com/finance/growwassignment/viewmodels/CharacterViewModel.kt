package com.finance.growwassignment.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.finance.growwassignment.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(val repository: CharacterRepository) :
    ViewModel() {

    fun getCharacters() =
        repository.getCharacters().cachedIn(viewModelScope)

    fun getCharactersSortedByName() =
        repository.getCharactersSortedByName().cachedIn(viewModelScope)

    fun getCharactersSortedByHeight() =
        repository.getCharactersSortedByHeight().cachedIn(viewModelScope)

    fun getCharactersSortedByMass() =
        repository.getCharactersSortedByMass().cachedIn(viewModelScope)

    fun getCharactersSortedByDateCreated() =
        repository.getCharactersSortedByCreated().cachedIn(viewModelScope)

    fun getCharactersSortedByDateEdited() =
        repository.getCharactersSortedByEdited().cachedIn(viewModelScope)

    fun getCharactersFilterByHairColor(color: String) =
        repository.getCharactersFilteredByHairColor(color).cachedIn(viewModelScope)

    fun getCharactersFilterByHeight(minHeight: String, maxHeight: String) =
        repository.getCharactersFilteredByHeight(minHeight, maxHeight).cachedIn(viewModelScope)

    fun getCharactersFilterByMass(minMass: String, maxMass: String) =
        repository.getCharactersFilteredByMass(minMass, maxMass).cachedIn(viewModelScope)
}
