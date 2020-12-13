package com.androidbootstrap.bootstrap.nfc

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import com.androidbootstrap.bootstrap.MIME_TEXT_PLAIN

// Afin de ne pas conserver la logique liée au NFC dans l'activité, nous créons une classe séparée pour encapsuler cette logique :
// Cette classe sera responsable de la création du message NDEF à partir des données que nous avons fournies dans l'EditText de la SenderActivity. Elle met en œuvre deux interfaces :
//NfcAdapter.CreateNdefMessageCallback - responsable de la création et de l'envoi dynamique du message au moment même où notre appareil entre en portée d'un autre appareil NFC.
//NfcAdapter.OnNdefPushCompleteCallback - signale lorsque le message est poussé avec succès vers un autre appareil
class OutcomingNfcManager(
    private val nfcActivity: NfcActivity
) :
    NfcAdapter.CreateNdefMessageCallback,
    NfcAdapter.OnNdefPushCompleteCallback {

    override fun createNdefMessage(event: NfcEvent): NdefMessage {
        // creating outcoming NFC message with a helper method
        // you could as well create it manually and will surely need, if Android version is too low
        val outString = nfcActivity.getOutcomingMessage()

        with(outString) {
            val outBytes = this.toByteArray()
            val outRecord = NdefRecord.createMime(MIME_TEXT_PLAIN, outBytes)
            return NdefMessage(outRecord)
        }
    }

    override fun onNdefPushComplete(event: NfcEvent) {
        // onNdefPushComplete() is called on the Binder thread, so remember to explicitly notify
        // your view on the UI thread
        nfcActivity.signalResult()
    }


    //La communication entre OutcomingNfcManager et SenderActivity se fait par l'intermédiaire d'une interface mise en place par SenderActivity :
    interface NfcActivity {
        fun getOutcomingMessage(): String

        fun signalResult()
    }
}