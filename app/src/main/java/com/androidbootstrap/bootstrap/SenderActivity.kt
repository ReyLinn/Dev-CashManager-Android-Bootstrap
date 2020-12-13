package com.androidbootstrap.bootstrap

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.androidbootstrap.bootstrap.nfc.OutcomingNfcManager


class SenderActivity : AppCompatActivity(), OutcomingNfcManager.NfcActivity {

    private lateinit var tvOutcomingMessage: TextView
    private lateinit var etOutcomingMessage: EditText
    private lateinit var btnSetOutcomingMessage: Button

    private var nfcAdapter: NfcAdapter? = null

    // vérifier si l'appareil prend en charge le NFC
    private val isNfcSupported: Boolean =
            this.nfcAdapter != null

    private lateinit var outcomingNfcCallback: OutcomingNfcManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sender)
/*        setSupportActionBar(findViewById(R.id.toolbar2))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)*/

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }

        // Si Nfcsupported = null alors ça veut dire qu'il ne prend pas en charge le NFC, on affiche un message et on arrête l'activité
        if (!isNfcSupported) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show()
            finish()
        }

        // On vérifie que le NFC est activé sur l'appareil, si ce n'est pas le cas on lui demande de l'activer
        if (!nfcAdapter?.isEnabled!!) {
            Toast.makeText(
                    this,
                    "NFC disabled on this device. Turn on to proceed",
                    Toast.LENGTH_SHORT
            ).show()
        }

        initViews()

        // encapsulate sending logic in a separate class
        this.outcomingNfcCallback = OutcomingNfcManager(this)
        this.nfcAdapter?.setOnNdefPushCompleteCallback(outcomingNfcCallback, this)
        this.nfcAdapter?.setNdefPushMessageCallback(outcomingNfcCallback, this)
    }

    private fun initViews() {
        this.tvOutcomingMessage = findViewById(R.id.tv_out_message)
        this.etOutcomingMessage = findViewById(R.id.et_message)
        this.btnSetOutcomingMessage = findViewById(R.id.btn_set_out_message)
        this.btnSetOutcomingMessage.setOnClickListener { setOutGoingMessage() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
    }

    private fun setOutGoingMessage() {
        val outMessage = this.etOutcomingMessage.text.toString()
        this.tvOutcomingMessage.text = outMessage
    }

    override fun getOutcomingMessage(): String =
            this.tvOutcomingMessage.text.toString()


    override fun signalResult() {
        // Après avoir effectué les contrôles NFC préliminaires, dans la même méthode onCreate(), définissons le OutcomingNfcManager comme un callback qui gérera la création d'un message que nous voulons pousser vers un autre appareil :
        runOnUiThread {
            Toast.makeText(this, R.string.message_beaming_complete, Toast.LENGTH_SHORT).show()
        }
    }

/*    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }*/
}