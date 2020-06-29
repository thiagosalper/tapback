package com.thiagosalper.tapback

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var mSensorManager: SensorManager?= null
    private var mAccelerometer: Sensor?= null
    private var inercia: Int? = null
    private var toque: Int = 0
    private val updateHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.txtdesc).setText(R.string.descricao)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val btativar = findViewById<Button>(R.id.btativar)
        try {
            btativar.setOnClickListener {
                vibrate()
            }
        }catch (e:Error) {
            Log.d("error", e.toString())
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = ListAdapter(links(), this)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
    }

    private var updateWidgetRunnable: Runnable = Runnable {
        run {
            toque = 0
            inercia = null

            updateHandler.postDelayed(updateWidgetRunnable, 1000L)
        }
    }

    fun links(): List<Atalhos> {
        return listOf(
            Atalhos("Print de tela", ""),
            Atalhos("Assistente virtual", ""),
            Atalhos("Navegador", ""),
            Atalhos("Telefone", ""),
            Atalhos("Mensagem", ""),
            Atalhos("Whatsapp", ""),
            Atalhos("Ligação de Emergência", ""),
            Atalhos("Música", "")
        )
    }

    fun vibrate() {
        val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibratorService.vibrate(100)
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(p0: SensorEvent?) {
        var movz = Math.round(p0!!.values[1])
        var movx = Math.round(p0!!.values[0])
        if(inercia==null){
            inercia = movz
        }

        if((movz > inercia!! && movz <= inercia!! + 1) || (movz < inercia!! && movz >= inercia!! - 1)){
            toque = toque + 1
            if(toque>=30){
                toque = 0
                vibrate()
            }
        }

        if(p0 != null){
            findViewById<TextView>(R.id.txtvalue0).setText(movx.toString())
            findViewById<TextView>(R.id.txtvalue1).setText(movz.toString())
        }

        findViewById<TextView>(R.id.txtinercia).setText("inercia: "+inercia.toString())
        findViewById<TextView>(R.id.txttoque).setText("toque: " + toque.toString())
    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
        updateHandler.postDelayed(updateWidgetRunnable, 1000L)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(this)
        updateHandler.removeCallbacks(updateWidgetRunnable)
    }
}