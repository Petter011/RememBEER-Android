package com.petter.remembeer.helper

import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
 class Beer(
    var id: UUID = UUID.randomUUID(),
    var type: String,
    var name: String,
    var note: String,
    var rating: Int
): Parcelable

/*{
    override fun toString(): String {
        return "Beer(id=$id, type=$type, name=$name, note=$note, rating=$rating)"
    }
}*/


class BeerViewModel : ViewModel() {

    val beers: MutableState<List<Beer>> = mutableStateOf(emptyList())

    /*fun getBeerById(beerId: UUID): Beer? {
        return beers.value.find { it.id == beerId }
    }*/

    fun addBeer(beer: Beer) {
        beers.value += beer
    }

    fun removeBeer(beer: Beer) {
        beers.value = beers.value - beer
    }

    fun updateBeer(oldBeer: Beer, newBeer: Beer) {
        val index = beers.value.indexOf(oldBeer)
        if (index != -1) {
            val updatedBeers = beers.value.toMutableList()
            updatedBeers[index] = newBeer
            beers.value = updatedBeers
        }
    }
}

