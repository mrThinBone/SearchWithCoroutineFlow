package com.example.searchwithcoroutineflow

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class MainViewModel : ViewModel() {

    val data: List<String> = listOf("Zeirur Shamir", "Mibud Khuho", "Vea Gorewater", "Guu Roughless",
        "Giral Niv", "Mer Boradz", "Frelvon Oatwhisper", "Am Wisetrack", "Thar-Kur-Ven Lazethaft",
        "Ber-Kuk Nundeft", "Soutvidjald Dugakorna", "Jesvac Grevuvro", "Eng Puan", "Zui Jaong",
        "Fruetudre Cahahil", "Durdar Gozucu")

    val liveSearchProducer = MutableLiveData<String>()
//    val liveSearchResult : Flow<List<String>> USING FLOW WILL NOT DEAL WITH OWNER LIFECYCLE
    val liveSearchResult : LiveData<List<String>>
    private var firstSearch = true

    init {
        liveSearchResult = liveSearchProducer.asFlow()
            .filter { (it.length > 1 && it.isNotBlank()) || firstSearch  }
            .debounce(1000)
            .distinctUntilChanged()
            .map {
                firstSearch = false
                Log.d("vinhtv", "process $it on ${Thread.currentThread().name}")
                data.filter { item -> item.contains(it, true) }
            }
//            .flowOn(Dispatchers.IO) // use this if not use asLiveData
            .asLiveData(Dispatchers.IO)
    }

    fun reset() {
        firstSearch = true
    }
}