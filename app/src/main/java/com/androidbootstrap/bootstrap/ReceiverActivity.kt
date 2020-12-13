package com.androidbootstrap.bootstrap

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Parcelable
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

const val MIME_TEXT_PLAIN = "text/plain"

class ReceiverActivity : AppCompatActivity() {

    private var tvIncomingMessage: TextView? = null

    private var nfcAdapter: NfcAdapter? = null

    // need to check NfcAdapter for nullability. Null means no NFC support on the device
    private val isNfcSupported: Boolean =
        this.nfcAdapter != null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)
/*        setSupportActionBar(findViewById(R.id.toolbar2))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)*/

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }

        if (!isNfcSupported) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show()
            finish()
        }

        if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(
                this,
                "NFC disabled on this device. Turn on to proceed",
                Toast.LENGTH_SHORT
            ).show()
        }

        initViews()
    }

    // onNewIntent() sera appelé chaque fois que ReceiverActivity est lancé avec une certaine intention. C'est le cas pour la réception d'un nouveau message et le remplacement du précédent si ReceiverActivity est déjà lancé. Nous vérifions ici l'intention avec laquelle l'activité a été lancée :
    private fun initViews() {
        this.tvIncomingMessage = findViewById(R.id.tv_in_message)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // also reading NFC message from here in case this activity is already started in order
        // not to start another instance of this activity
        receiveMessageFromDevice(intent)
    }

    // Nous construisons et configurons une intention avec des filtres que nous voulons traiter lorsque l'application est au premier plan et nous définissons le type MIME des données que nous attendons.
    override fun onResume() {
        super.onResume()

        // foreground dispatch should be enabled here, as onResume is the guaranteed place where app
        // is in the foreground
        enableForegroundDispatch(this, this.nfcAdapter)
        receiveMessageFromDevice(intent)
    }

    override fun onPause() {
        super.onPause()
        disableForegroundDispatch(this, this.nfcAdapter)
    }

    private fun receiveMessageFromDevice(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables) {
                val inNdefMessage = this?.get(0) as NdefMessage
                val inNdefRecords = inNdefMessage.records
                val ndefRecord_0 = inNdefRecords[0]

                val inMessage = String(ndefRecord_0.payload)
                tvIncomingMessage?.text = inMessage
            }
        }
    }

/*    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }*/


    //Nous devons extraire l'action de l'intention et nous attendre à ce qu'elle soit ACTION_NDEF_DISCOVERED.
    //Si c'est vraiment le cas, nous procédons à l'analyse et à l'affichage du message à l'utilisateur.
    //Nous enregistrons également l'activité pour ce que l'on appelle la répartition en avant-plan. Ceci est fait pour donner à l'application la plus haute priorité pour les messages NDEF entrants, de sorte qu'aucune autre application filtrant ACTION_NDEF_DISCOVERED sur l'appareil ne puisse intercepter le message que nous poussons :

    private fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {

        // here we are setting up receiving activity for a foreground dispatch
        // thus if activity is already started it will take precedence over any other activity or app
        // with the same intent filters

        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)

        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        filters[0] = IntentFilter()
        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType(MIME_TEXT_PLAIN)
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("Check your MIME type")
            }
        }

        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    private fun disableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        adapter?.disableForegroundDispatch(activity)
    }
}