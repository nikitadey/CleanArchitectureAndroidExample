package com.nikita.demoapplication.app.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.nikita.demoapplication.R
import com.nikita.demoapplication.app.framework.base.BaseRecyclerViewAdapter
import com.nikita.demoapplication.app.framework.base.BaseViewHolder
import com.nikita.demoapplication.app.framework.base.BaseViewModelFactory
import com.nikita.demoapplication.app.framework.utils.Resource
import com.nikita.demoapplication.core.domain.CryptoExchange
import com.nikita.demoapplication.core.domain.CryptoName
import com.nikita.demoapplication.core.domain.CryptoTrade
import com.nikita.demoapplication.databinding.ActivityMainBinding
import com.nikita.demoapplication.databinding.LayoutButtonBinding
import com.nikita.demoapplication.utils.GsonUtils
import com.nikita.demoapplication.utils.Status
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
//    lateinit var mSocket: Socket;

    private val viewModel: MainActivityViewModel by viewModels {
        BaseViewModelFactory({
            MainActivityViewModel(application)
        }, application)
    }


    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BaseRecyclerViewAdapter<LayoutButtonBinding, CryptoName>
    private lateinit var adapterExchange: BaseRecyclerViewAdapter<LayoutButtonBinding, CryptoExchange>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            com.nikita.demoapplication.R.layout.activity_main
        )

        adapter = BaseRecyclerViewAdapter(com.nikita.demoapplication.R.layout.layout_button)

        binding.rvCrypto.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvCrypto.adapter = adapter


        adapterExchange = BaseRecyclerViewAdapter(com.nikita.demoapplication.R.layout.layout_button)

        binding.rvCryptoExchange.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvCryptoExchange.adapter = adapterExchange
//        setContent {


//            DemoApplicationTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//            DemoApp()
//        }
        initiateView()
//        connectToSocket()

        observeData()

        viewModel.initiateListFetching()
    }

    private fun initiateView() {


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    val intent = Intent(this@MainActivity, MapsActivity::class.java)
                    startActivity(intent)
                    // Respond to navigation item 1 click
                    false
                }
                R.id.page_2 -> {
                    // Respond to navigation item 2 click
                    true
                }
                else -> false
            }
        }


        // if more than 60 entries are displayed in the chart, no values will be
        // drawn

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        binding.chart1.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately

        // scaling can now only be done on x- and y-axis separately
        binding.chart1.setPinchZoom(false)

        binding.chart1.setDrawGridBackground(false)

        binding.chart1.legend.isEnabled = false
        val xAxis: XAxis = binding.chart1.getXAxis()
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.WHITE


        val rightAxis: YAxis = binding.chart1.axisRight
        rightAxis.setEnabled(true);
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawAxisLine(true)
        rightAxis.textColor = Color.WHITE

        val leftAxis: YAxis = binding.chart1.axisLeft
        leftAxis.isEnabled = false
        binding.chart1.setNoDataText("")

        binding.chart1.invalidate()


        val yAxis: YAxis = binding.chart2.getAxisLeft()
        yAxis.isEnabled=false
        val yAxisR: YAxis = binding.chart2.axisRight
        yAxisR.isEnabled=false
//        yAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
//        yAxis.spaceTop = 15f
//        yAxis.mAxisMinimum=0f // this replaces setStartAtZero(true)


        val xAxis2: XAxis = binding.chart2.getXAxis()
        xAxis2.position = XAxisPosition.BOTTOM
        xAxis2.setDrawGridLines(false)
        xAxis2.isEnabled=true
        xAxis2.textColor=Color.WHITE


        val l: Legend = binding.chart2.getLegend()
        l.position=Legend.LegendPosition.BELOW_CHART_LEFT
        l.form = LegendForm.SQUARE
        l.formSize = 9f
        l.textSize = 11f
        l.xEntrySpace = 4f
        l.textColor=Color.WHITE

        binding.chart2.setDescription("")
        binding.chart2.setDrawGridBackground(false)
        binding.chart2.setBackgroundColor(Color.TRANSPARENT)
        binding.chart2.invalidate()
    }

    private fun observeData() {
        viewModel.cryptoExchangeLiveData.observe(this, Observer<Resource<List<CryptoExchange>>> {
            when (it.status) {

                is Status.Loading -> {
                    binding.loading.visibility = View.VISIBLE
                    binding.llError.visibility=View.GONE
                }
                is Status.Success -> {
                    adapterExchange.setList(
                        it.data?.toMutableList() ?: mutableListOf(),
                        object :
                            BaseRecyclerViewAdapter.ViewHolderListener<LayoutButtonBinding, CryptoExchange> {
                            override fun onBindViewHolder(
                                holder: BaseViewHolder<LayoutButtonBinding>,
                                item: CryptoExchange
                            ) {

                                holder.binding.btnCrypto.text = item.exchange
                                holder.binding.btnCrypto.setOnClickListener {
                                    viewModel.fetchCryptoList(item.exchange)
                                    binding.tvCryptoExchange.text = item.exchange
                                }
                            }

                        })
                    binding.tvExchangeLabel.text="Select Exchange"

                    binding.tvExchangeLabel.visibility=View.VISIBLE

                    binding.loading.visibility = View.GONE

                }
                is Status.Error -> {
                    binding.tvError.text="Unable to load data"
                    binding.llError.visibility=View.VISIBLE
                    binding.loading.visibility = View.GONE

                }
                Status.Uninitalized -> TODO()
            }
        })



        viewModel.cryptoLiveData.observe(this, Observer<Resource<List<CryptoName>>> {
            when (it.status) {

                is Status.Loading -> {
                    binding.loading.visibility = View.VISIBLE
                    adapter.setList(mutableListOf())
                    binding.tvCryptoName.text=""
                    binding.chart1.clear()
                    binding.chart1.invalidate()
                    binding.chart2.clear()
                    binding.chart2.invalidate()
                }
                is Status.Success -> {
                    adapter.setList(
                        it.data?.toMutableList() ?: mutableListOf(),
                        object :
                            BaseRecyclerViewAdapter.ViewHolderListener<LayoutButtonBinding, CryptoName> {
                            override fun onBindViewHolder(
                                holder: BaseViewHolder<LayoutButtonBinding>,
                                item: CryptoName
                            ) {

                                holder.binding.btnCrypto.text = item.displaySymbol
                                holder.binding.btnCrypto.setOnClickListener {
                                    viewModel.initiateChartDataFetching(item.symbol)
                                    binding.tvCryptoName.text = item.displaySymbol
                                }
                            }

                        })
                    binding.loading.visibility = View.GONE

                }
                is Status.Error -> {
                    binding.loading.visibility = View.GONE

                }
                Status.Uninitalized -> TODO()
            }
        })


        viewModel.cryptoCandleLiveData.observe(this) {
            when (it.status) {
                Status.Loading -> {
                    binding.chart1.setNoDataText("loading..")
                    binding.chart1.clear()
                    binding.chart1.invalidate()

                }
                Status.Success -> {

                    binding.chart1.data = it.data!!
                    binding.chart1.setNoDataText("")
                    binding.chart1.invalidate()

                }
                else -> {
                    binding.chart1.setNoDataText("No Data to display")
                    binding.chart1.clear()
                    binding.chart1.invalidate()
                }
            }
        }


        viewModel.cryptoBarchartLiveData.observe(this) {
            when (it.status) {
                Status.Loading -> {
                    binding.chart2.setNoDataText("loading..")
                    binding.chart2.clear()
                    binding.chart2.invalidate()

                }
                Status.Success -> {
                    val data = it.data!!
                    data.setValueTextSize(10f)

                    binding.chart2.data =data
                    binding.chart2.setNoDataText("")
                    binding.chart2.invalidate()

                }
                else -> {
                    binding.chart2.setNoDataText("No Data to display")
                    binding.chart2.clear()
                    binding.chart2.invalidate()
                }
            }
        }

        viewModel.cryptoTradeLiveData.observe(this) {
            when (it.status) {
                Status.Success->{
                    it.data?.let { data ->
                        binding.tvSymbol.text=data.symbol
                        binding.tvPrice.text="$"+data.lastPrice
                    }
                    binding.clTopView.visibility=View.VISIBLE

                }
                else -> {
                    binding.clTopView.visibility=View.GONE
                }
            }
        }


    }


    private fun connectToSocket() {
        //Let's connect to our Chat room! :D
        try {
            //This address is the way you can connect to server
            var mSocket: Socket =
                IO.socket("wss://ws.finnhub.io?token=cd1ee22ad3i7tm8rq110cd1ee22ad3i7tm8rq11g")
//            mSocket = IO.socket("https://socket-io-chat.now.sh/")

//            if(mSocket.connected()) {

            //Register all the listener and callbacks here.
            mSocket.on(Socket.EVENT_CONNECT, Emitter.Listener {
                //After getting a Socket.EVENT_CONNECT which indicate socket has been connected to server,
                //send type and symbol so that they can fetch data.
                Log.d("success", mSocket.id()+"")

                val jsonData: String =
                    "{\"type\":\"subscribe\",\"symbol\":\"BINANCE:BTCUSDT\"}"// Gson changes data object to Json type.
                mSocket.emit("subscribe", jsonData)

                val cryptoTrade: CryptoTrade? =
                    GsonUtils.fromJson(it[0].toString(), CryptoTrade::class.java)


            })
//            }
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val options = IO.Options()
                    options.reconnection = true //reconnection
                    options.forceNew = true

                    mSocket.connect()
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("fail", "Failed to connect")
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }


    }


}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    DemoApplicationTheme {
//        Greeting("Android")
//    }
//}