package com.androidbootstrap.bootstrap
import java.util.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidbootstrap.bootstrap.data.model.CartModel
import com.budiyev.android.codescanner.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerView: CodeScannerView
    private lateinit var textViewScanner: TextView
    private lateinit var textViewCart: TextView
    private lateinit var cart : CartModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        textViewScanner = view.findViewById<TextView>(R.id.textview_first)
        textViewCart = view.findViewById<TextView>(R.id.textview_cart)
        codeScanner()
        /*view.findViewById<Button>(R.id.button_add).setOnClickListener {
            try {
                val text = textViewScanner.text
                cart.add(text as String)
                textViewCart.text = cart.toString()
            }catch (e: Error) {
                System.err.println(e)
            }
        }*/
        view.findViewById<Button>(R.id.button_cart).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun codeScanner(){
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false
            decodeCallback = DecodeCallback {
                activity.runOnUiThread() {
                    textViewScanner.text = it.text

                }
            }

            errorCallback = ErrorCallback {
                activity.runOnUiThread {
                    Log.e("Main", "Camera initialisation error: ${it.message}")
                }
            } }
    }
}