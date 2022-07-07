package com.hackathon2022.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
    private val serverUrl = "http://172.20.36.211:5000/scanner"

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

        val scannedId = arguments?.getString("scannedID")
        binding.qrValue.text = scannedId
        binding.serverAddress.setText(serverUrl)

        binding.buttonFirst.setOnClickListener {
            val jsonData = JSONObject()
            jsonData.put("id", scannedId)
            val timestamp = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss.SS")
                .withZone(ZoneId.of("Europe/Berlin"))
                .format(Instant.now())
            jsonData.put("timestamp", timestamp)
            println(jsonData)

            Thread {
                with(URL(serverUrl).openConnection() as HttpURLConnection) {
                    // optional default is GET
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")

                    val wr = OutputStreamWriter(outputStream);
                    wr.write(jsonData.toString());
                    wr.flush();

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
}