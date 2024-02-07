package com.petter.remembeer.helper

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID


//@Parcelize
data class Beer(
    var id: UUID = UUID.randomUUID(),
    var type: String = "",
    var name: String = "",
    var note: String = "",
    var rating: Int = 0

)//: Parcelable

{
    override fun toString(): String {
        return "Beer(id=$id, type=$type, name=$name, note=$note, rating=$rating)"
    }
}

class BeerViewModel : ViewModel() {

    private val _beers = MutableStateFlow<List<Beer>>(emptyList())
    val beers: StateFlow<List<Beer>> = _beers

    fun addBeer(beer: Beer) {
        // Generate a new UUID for the beer
        val beerWithId = beer.copy(id = UUID.randomUUID())
        _beers.value = _beers.value + beerWithId
    }

    fun removeBeer(beer: Beer) {
        _beers.value = _beers.value - beer
    }

    fun updateBeer(oldBeer: Beer, newBeer: Beer) {
        val updatedBeers = _beers.value.map { if (it.id == oldBeer.id) newBeer else it }
        _beers.value = updatedBeers
    }

    fun getBeerById(beerId: UUID): Beer? {
        return beers.value.find { it.id == beerId }
    }

}

/*{
    override fun toString(): String {
        return "Beer(id=$id, type=$type, name=$name, note=$note, rating=$rating)"
    }
}*/
