package com.androidbootstrap.bootstrap.nfc

interface NfcActivity {
    fun getOutcomingMessage(): String

    fun signalResult()
}