package com.nikita.demoapplication.app.presentation

import android.R
import android.app.Application
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.*
import com.nikita.demoapplication.app.framework.datasource.api.http.ApiResponse
import com.nikita.demoapplication.app.framework.datasource.api.http.ResponseCode
import com.nikita.demoapplication.app.framework.datasource.api.http.exception.BasicException
import com.nikita.demoapplication.app.framework.utils.Resource
import com.nikita.demoapplication.core.domain.CryptoExchange
import com.nikita.demoapplication.core.domain.CryptoName
import com.nikita.demoapplication.core.domain.CryptoTrade
import com.nikita.demoapplication.core.usecases.GetCryptoCandle
import com.nikita.demoapplication.core.usecases.GetCryptoExchange
import com.nikita.demoapplication.core.usecases.GetCryptoList
import com.nikita.demoapplication.core.usecases.GetCryptoTrade
import com.nikita.demoapplication.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.*


class MainActivityViewModel(application: Application) : AndroidViewModel(application),
    KoinComponent {
    val cryptoLiveData: MutableLiveData<Resource<List<CryptoName>>> = MutableLiveData()
    val cryptoTradeLiveData: MutableLiveData<Resource<CryptoTrade>> = MutableLiveData()
    val cryptoExchangeLiveData: MutableLiveData<Resource<List<CryptoExchange>>> = MutableLiveData()
    val cryptoCandleLiveData: MutableLiveData<Resource<CandleData>> = MutableLiveData()
    val cryptoCandleXAxisLiveData: MutableLiveData<List<String>> = MutableLiveData()
    val cryptoBarchartLiveData: MutableLiveData<Resource<BarData>> = MutableLiveData()
    lateinit var cryptoResponse:LiveData<CryptoTrade>


    var previousJob: Job? = null
    var previousExchangeJob: Job? = null
    var previousChartJob: Job? = null
    var previousTradeJob: Job? = null

    val fetchCrypto: GetCryptoList by inject()
    val fetchCryptoExchange: GetCryptoExchange by inject()
    val fetchCryptoCandle: GetCryptoCandle by inject()
    val fetchCryptoTrade: GetCryptoTrade by inject()

    fun initiateListFetching() {
        cryptoExchangeLiveData.postValue(Resource(Status.Loading,null))
        previousExchangeJob?.cancel()


            previousExchangeJob = viewModelScope.launch(Dispatchers.IO) {

                when (val cryptoResponse = fetchCryptoExchange()) {
                    is ApiResponse.Success -> {

                        viewModelScope.launch(Dispatchers.Main) {
                            cryptoExchangeLiveData.postValue(Resource(Status.Success, cryptoResponse.data))

                        }
                    }
                    is ApiResponse.Failure -> {

                        cryptoLiveData.postValue(Resource(Status.Error(cryptoResponse.exception), null))

                    }
                }
            }

    }


    fun fetchCryptoList(selectedExchange:String){
        cryptoLiveData.postValue(Resource(Status.Loading,null))
        previousJob?.cancel()


        previousJob = viewModelScope.launch(Dispatchers.IO) {

            when (val cryptoResponse = fetchCrypto(selectedExchange)) {
                is ApiResponse.Success -> {

                    viewModelScope.launch(Dispatchers.Main) {
                        cryptoLiveData.postValue(Resource(Status.Success, cryptoResponse.data.subList(0,15)))

                    }
                }
                is ApiResponse.Failure -> {

                    cryptoLiveData.postValue(Resource(Status.Error(cryptoResponse.exception), null))

                }
            }
        }
    }
    fun initiateChartDataFetching(symbol:String){
        cryptoCandleLiveData.postValue(Resource(Status.Loading,null))
        cryptoBarchartLiveData.postValue(Resource(Status.Loading,null))
        previousChartJob?.cancel()


        previousChartJob = viewModelScope.launch(Dispatchers.IO) {

            val today:Calendar = Calendar.getInstance()
            val unixToTime=today.timeInMillis/1000L
            val from:Calendar = Calendar.getInstance()
            from.set(Calendar.YEAR, today.get(Calendar.YEAR)-1)
            val unixFromTime=from.timeInMillis/1000L
            when (val cryptoCandleResponse = fetchCryptoCandle(symbol, to = unixToTime.toString(), from = unixFromTime.toString())) {
                is ApiResponse.Success -> {
                    val raw= cryptoCandleResponse.data
                    val finalList:ArrayList<CandleEntry> = ArrayList()
                    val finalBarChartList:ArrayList<BarEntry> = ArrayList()
                    val xVals:ArrayList<String> = ArrayList()
                    for ((index,data) in raw.withIndex()){
                        val date:Calendar= Calendar.getInstance()
                        date.timeInMillis=data.time *1000L
//                            val monthString = DateFormat.format("MMM", Date(data.time)) as String // Jun


                        val month_date = SimpleDateFormat("MMM")
                        val month_name: String = month_date.format(date.getTime())
                        Log.d("month",month_name)
                        finalList.add(CandleEntry(index,data.high,data.low,data.open,data.close))
                        finalBarChartList.add(BarEntry(data.volume/1000,index))
                        xVals.add(month_name)
                    }

                    val set1 = CandleDataSet(finalList, "Data Set")
                    set1.shadowColor = Color.DKGRAY;
                    set1.shadowWidth = 0.7f;
                    set1.decreasingColor = Color.RED
                    set1.decreasingPaintStyle = Paint.Style.FILL;
                    set1.increasingColor = Color.rgb(122, 242, 84);
                    set1.increasingPaintStyle = Paint.Style.FILL;


                    val candleData = CandleData(xVals,set1)

                    viewModelScope.launch(Dispatchers.Main) {
                       cryptoCandleLiveData.postValue(Resource(Status.Success,candleData))
                        cryptoCandleXAxisLiveData.value=xVals

                    }



                    val set2=BarDataSet(finalBarChartList,"Volume (000)")
                        set2.color=Color.YELLOW
                        set2.valueTextColor=Color.WHITE

                    val barData= BarData(xVals,set2)
                    viewModelScope.launch(Dispatchers.Main) {

                        cryptoBarchartLiveData.postValue(Resource(Status.Success, barData))
                    }
                }
                is ApiResponse.Failure -> {
                    viewModelScope.launch(Dispatchers.Main) {
                        cryptoCandleLiveData.postValue(
                            Resource(
                                Status.Error(cryptoCandleResponse.exception),
                                null
                            )
                        )
                        cryptoBarchartLiveData.postValue(
                            Resource(
                                Status.Error(cryptoCandleResponse.exception),
                                null
                            )
                        )
                    }
                }
            }
        }


        previousTradeJob?.cancel()
        cryptoTradeLiveData.postValue(
            Resource(Status.Loading,null))
        previousTradeJob = viewModelScope.launch(Dispatchers.IO)  {

           cryptoResponse= fetchCryptoTrade(symbol)

            viewModelScope.launch(Dispatchers.Main) {

                cryptoResponse.observeForever(observer)
            }


        }


    }

    val observer = Observer<CryptoTrade> {


            if (it?.volume == 0.0F) {
                cryptoTradeLiveData.postValue(
                    Resource(
                        Status.Error(
                            BasicException(
                                ResponseCode.INVALID_DATA
                            )
                        ), null
                    )
                )
            } else {
                cryptoTradeLiveData.postValue(
                    Resource(
                        Status.Success,
                        it
                    )
                )
            }

    }

    override fun onCleared() {
        super.onCleared()
        if(this::cryptoResponse.isInitialized){
            cryptoResponse.removeObserver(observer)
        }
    }


}