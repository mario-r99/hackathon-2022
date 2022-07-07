package com.hackathon2022.scanner

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.hackathon2022.scanner.databinding.FragmentSecondBinding
import java.io.IOException
import android.Manifest
import android.os.Looper
import androidx.core.os.bundleOf
import androidx.core.text.isDigitsOnly
import androidx.navigation.findNavController

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""
    private var qr_detected = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        qr_detected = false

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    setupCamera()
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

        when {
            ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                setupCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(context, "This app needs camera access to scan the QR code", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        val aniSlide: Animation = AnimationUtils.loadAnimation(context, R.anim.scanner_animation)
        binding.barcodeLine.startAnimation(aniSlide)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::cameraSource.isInitialized) cameraSource.stop()
        _binding = null
    }

    private fun setupCamera() {
        barcodeDetector =
            BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(context, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true)
            .build()

        binding.cameraSurfaceView.getHolder().addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    //Start preview after 1s delay
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(context, "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue

                    if (scannedValue.isDigitsOnly() && scannedValue.length == 6) {
                        if (!qr_detected) {
                            qr_detected = true
                            activity?.runOnUiThread {
                                cameraSource.stop()
                                val bundle = bundleOf("scannedID" to scannedValue)
                                findNavController().navigate(R.id.action_SecondFragment_to_ThirdFragment, bundle)
                            }
                        }
                    }
                    else {
                        activity?.runOnUiThread {
                            Toast.makeText(context, "Invalid data format", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }
}