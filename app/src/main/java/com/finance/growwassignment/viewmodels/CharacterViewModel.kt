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
    fun getCharactersSorted(sort: String? = null) =
        repository.getCharacters(sort).cachedIn(viewModelScope)

    fun getCharactersSortedByName(sort: String? = null) =
        repository.getCharactersSortedByName().cachedIn(viewModelScope)

    fun getCharactersSortedByHeight(sort: String? = null) =
        repository.getCharactersSortedByHeight().cachedIn(viewModelScope)

    fun getCharactersSortedByMass(sort: String? = null) =
        repository.getCharactersSortedByMass().cachedIn(viewModelScope)

    fun getCharactersSortedByDateCreated(sort: String? = null) =
        repository.getCharactersSortedByCreated().cachedIn(viewModelScope)

    fun getCharactersSortedByDateEdited(sort: String? = null) =
        repository.getCharactersSortedByEdited().cachedIn(viewModelScope)

    fun getCharactersFilterByHairColor(color: String) =
        repository.getCharactersFilteredByHairColor(color).cachedIn(viewModelScope)

    fun getCharactersFilterByHeight(minHeight: String, maxHeight: String) =
        repository.getCharactersFilteredByHeight(minHeight, maxHeight).cachedIn(viewModelScope)

    fun getCharactersFilterByMass(minMass: String, maxMass: String) =
        repository.getCharactersFilteredByMass(minMass, maxMass).cachedIn(viewModelScope)
}
