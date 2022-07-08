package com.hackathon2022.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hackathon2022.scanner.databinding.FragmentThirdBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass as the third destination in the navigation.
 */
class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private val serverUrl = "http://172.20.36.150:5000/scanner"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude = 48.748654568574594
    private var longitude = 9.108372534029133

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    getNetworkLocation()
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

        when {
            ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getNetworkLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Toast.makeText(context, "This app needs location access to transmit the machine location", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        val scannedId = arguments?.getString("scannedID")
        binding.qrValue.text = scannedId
        binding.serverAddress.setText(serverUrl)

        binding.buttonFirst.setOnClickListener {
            getNetworkLocation()
            val jsonData = JSONObject()
            jsonData.put("id", scannedId)
            val timestamp = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss.SS")
                .withZone(ZoneId.of("Europe/Berlin"))
                .format(Instant.now())
            jsonData.put("timestamp", timestamp)
            jsonData.put("location", "${latitude};${longitude}")
            println(jsonData)

            Thread {
                with(URL(serverUrl).openConnection() as HttpURLConnection) {
                    // optional default is GET
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")

                    val wr = OutputStreamWriter(outputStream)
                    wr.write(jsonData.toString())
                    wr.flush()

                    println("URL : $url")
                    println("Response Code : $responseCode")

                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        println("Response : $response")
                    }
                }
            }.start()

            Toast.makeText(context, "Confirmation successful!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getNetworkLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            if (location!=null) {
                latitude = location.latitude
                longitude = location.longitude
            }
            else println("LOCATION IS NULL")
        }
    }
}