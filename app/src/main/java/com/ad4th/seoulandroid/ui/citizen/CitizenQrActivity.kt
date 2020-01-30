package com.ad4th.seoulandroid.ui.citizen

import android.Manifest
import android.content.Intent
import android.graphics.PointF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ad4th.seoulandroid.*
import com.ad4th.seoulandroid.BusEvent.BusProvider
import com.ad4th.seoulandroid.BusEvent.Events
import com.ad4th.seoulandroid.ICON.ICX
import com.ad4th.seoulandroid.ui.dialog.MaterialDialogUtil
import com.ad4th.seoulandroid.utils.Constants
import com.ad4th.seoulandroid.utils.PermissionUtil
import com.ad4th.seoulandroid.utils.PreferenceManager
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_citizen_qr.*
import java.util.ArrayList

class CitizenQrActivity : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {

    private var qrCodeReaderView: QRCodeReaderView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citizen_qr)
        BusProvider.instance.register(this)

        permissionCheck()
        initQrScan()

        close_btn.setOnClickListener {
            finish()
        }
    }

    private var isScan = false

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        if(!isScan) {
            isScan = true
            qrCodeReaderView!!.stopCamera()

            var resultCode: String = text!!
            Log.e("[QRCODE] resultCode ", resultCode)
            // private key로 address 알아오기
            val icx = ICX(resultCode) // private key
            val user_address = icx.wallet.address.toString()

            PreferenceManager.setCitizenPrivateKey(this, resultCode)
            BusProvider.instance.post(Events(Constants.CITIZEN_FRAGMENT, user_address));

            finish()
        }
    }

    fun initQrScan() {
        qrCodeReaderView = findViewById(R.id.qrdecoderview)

        qrCodeReaderView!!.setOnQRCodeReadListener(this)
        // Use this function to enable/disable decoding
        qrCodeReaderView!!.setQRDecodingEnabled(true)
        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView!!.setAutofocusInterval(2000L)
        // Use this function to enable/disable Torch
        qrCodeReaderView!!.setTorchEnabled(true)
        // Use this function to set front camera preview
        qrCodeReaderView!!.setFrontCamera()
        // Use this function to set back camera preview
        qrCodeReaderView!!.setBackCamera()

    }

    /**
     * 필수 퍼미션 체크
     */
    fun permissionCheck() {
        var hasCameraPermission = PermissionUtil.hasCameraGranted(this)
        if (!hasCameraPermission) {
            // 카메라 퍼미션 체크
            TedPermission.with(this)
                    .setPermissions(Manifest.permission.CAMERA)
                    .setPermissionListener(object : PermissionListener {
                        override fun onPermissionGranted() {
                            // QR코드
                            finish()
                            val intent = Intent(this@CitizenQrActivity, CitizenQrActivity::class.java)
                            startActivity(intent)
                        }

                        override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                        }
                    }).check()
        }
    }

    override fun onDestroy() {
        BusProvider.instance.unregister(this)
        super.onDestroy()
    }
}
