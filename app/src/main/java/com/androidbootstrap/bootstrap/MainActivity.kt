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
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

private const val CAMERA_REQUEST_CODE = 101
private const val NFC_REQUEST_CODE = 102

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
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.NFC), NFC_REQUEST_CODE)
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
            NFC_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You need the camera permission to use this app", Toast.LENGTH_SHORT).show()
                }else{
                    //successful
                }
            }
        }
    }
    private lateinit var apiService: APIService
    private lateinit var productAdapter: ProductAdapter

    private var products = listOf<Product>()
//    val APIConfig = APIConfig.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)

        setContentView(R.layout.activity_main)


        setSupportActionBar(toolbar)
        apiService = APIConfig.getRetrofitClient(this).create(APIService::class.java)


        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))

        swipeRefreshLayout.isRefreshing = true

        swipeRefreshLayout.setOnRefreshListener {
            getProducts()
        }

//        val layoutManager = StaggeredGridLayoutManager(this, Lin)

        products_recyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        cart_size.text = ShoppingCart.getShoppingCartSize().toString()

        getProducts()


        showCart.setOnClickListener {

            startActivity(Intent(this, ShoppingCartActivity::class.java))
        }

    }


    fun getProducts() {

        apiService.getProducts().enqueue(object : retrofit2.Callback<List<Product>> {
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {

                print(t.message)
                Log.d("Data error", t.message)
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                swipeRefreshLayout.isRefreshing = false
//                swipeRefreshLayout.isEnabled = false

                products = response.body()!!

                productAdapter = ProductAdapter(this@MainActivity, products)

                products_recyclerview.adapter = productAdapter

//                productAdapter.notifyDataSetChanged()

            }

        })
    }
}