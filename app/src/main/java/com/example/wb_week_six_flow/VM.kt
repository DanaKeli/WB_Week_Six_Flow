package com.example.wb_week_six_flow

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.math.BigDecimal
import kotlin.coroutines.CoroutineContext

class VM : ViewModel() {

    companion object {
        const val TIMER_INTERVAL = 1000L
        const val CALCULATOR_INTERVAL = 10L
    }

    val pi: MutableLiveData<String> = MutableLiveData("")
    val time: MutableLiveData<Long> = MutableLiveData(0L)
    private var result = BigDecimal(4)
    private var counter = 0.0
    private var string = ""
    private var divider = 1
    private var count = 0
    private lateinit var scope: Job

    fun onStart() {
        scope = viewModelScope.launch {
            launch { startTimer().collect { time.postValue(time.value?.plus(1000)) } }
            launch {
                startCalculator().collect {
                    pi.postValue(
                        pi.value?.plus((0..9).random()).toString()
                    )
                }
            }
        }
    }

    fun onPause() {
        stop()
    }

    fun onReset() {
        stop()
        time.value = 0L
        pi.value = ""
    }

    private fun stop() {
        scope.cancel()
    }

    private fun startTimer(): Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(TIMER_INTERVAL)
        }
    }

    private fun startCalculator(): Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(CALCULATOR_INTERVAL)
        }
    }

    private fun getPi() {
        counter += 1
        if (count % 2 == 0) {
            result -= (BigDecimal(4).divide(BigDecimal(divider + 2), 300, 0))
        } else {
            result += (BigDecimal(4).divide(BigDecimal(divider + 2), 300, 0))
        }
        string = result.toString().substring(6)
        Log.i("dana", string)
        divider += 2
        if (counter % 1000 == 0.0) {
            pi.postValue(pi.value.plus(string))
        }
        count++
    }
}
