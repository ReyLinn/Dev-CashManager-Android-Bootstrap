package com.androidbootstrap.bootstrap

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidbootstrap.bootstrap.ui.login.LoginActivity

private const val CAMERA_REQUEST_CODE = 101
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        setUpPermissionsCamera()
        setUpPermissionsInternet()
        setUpPermissionsNFC()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val setting = Intent(this, SettingActivity::class.java).apply {}
        val disconnect = Intent(this, LoginActivity::class.java).apply {}
        val sender = Intent(this, SenderActivity::class.java).apply {}
        // val receiver = Intent(this, ReceiverActivity::class.java).apply {}

        return when (item.itemId) {
            R.id.action_disconnect -> {
                startActivity(disconnect)
                true
            }
            R.id.action_setting -> {
                startActivity(setting)
                true
            }
            R.id.action_sender -> {
                startActivity(sender)
                true
            }
/*            R.id.action_receiver -> {
                startActivity(receiver)
                true
            }*/
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpPermissionsCamera(){
        val permissionCamera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if(permissionCamera != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun setUpPermissionsInternet(){
        val permissionInternet = ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET)

        if(permissionInternet != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun setUpPermissionsNFC(){
        val permissionNfc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.NFC)

        if(permissionNfc != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You need the camera permission to use this app", Toast.LENGTH_SHORT).show()
                }else{
                    //successful
                }
            }
        }
    }
}