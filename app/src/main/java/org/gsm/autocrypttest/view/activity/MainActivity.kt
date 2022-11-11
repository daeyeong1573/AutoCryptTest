package org.gsm.autocrypttest.view.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.gsm.autocrypttest.R
import org.gsm.autocrypttest.adapter.MarkerAdapter
import org.gsm.autocrypttest.base.BaseActivity
import org.gsm.autocrypttest.databinding.ActivityMainBinding
import org.gsm.autocrypttest.viewmodel.CenterViewModel


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), OnMapReadyCallback {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1004
    }

    override val TAG: String
        get() = MainActivity::class.java.simpleName

    private val vm: CenterViewModel by viewModels()
    private val mapView: MapView by lazy { binding.mapView }
    private lateinit var navMap: NaverMap
    private lateinit var fLocationSource: FusedLocationSource
    private val infoWindow = InfoWindow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this@MainActivity)
        fLocationSource = FusedLocationSource(this@MainActivity, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun init() {
        binding.activity = this@MainActivity
        val adapter = MarkerAdapter(this@MainActivity, vm)
        infoWindow.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: NaverMap) {
        navMap = map
        navMap.locationSource = fLocationSource
        setMarker()
        requestPermission()
    }

    private fun moveCamera(lat: Double, lng: Double, marker: Marker) {
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(lat, lng))
            .animate(CameraAnimation.Fly, 1000)
            .finishCallback {
                if (marker.infoWindow == null) {
                    // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                    infoWindow.open(marker)
                } else {
                    // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                    infoWindow.close()
                }
            }
        val cameraZoom = CameraUpdate.zoomTo(15.0)
        navMap.moveCamera(cameraZoom)
        navMap.moveCamera(cameraUpdate)
    }

    private fun setMarker() = CoroutineScope(Dispatchers.Main).launch {
        val markers = mutableListOf<Marker>()
        vm.getLatLng()

        vm.latLng.observe(this@MainActivity) {
            it.forEach { response ->
                markers += Marker().apply {
                    position = LatLng(response.latLng.latitude, response.latLng.longitude)
                    this.icon = MarkerIcons.BLACK
                    when (response.centerType) {
                        "지역" -> {
                            this.iconTintColor = Color.CYAN
                        }
                        "중앙/권역" -> {
                            this.iconTintColor = Color.RED
                        }

                    }
                    this.setOnClickListener {
                        moveCamera(response.latLng.latitude, response.latLng.longitude, this)
                        vm.getCenterInfo(response.id)
                        true
                    }

                }
            }

            markers.forEach {
                it.map = navMap
            }
        }
    }


    fun currentLocationBtn() {
        requestPermission()
    }


    private fun requestPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    navMap.locationTrackingMode = LocationTrackingMode.Follow
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {
                    makeToast("권한을 허용해 주세요")
                    navMap.locationTrackingMode = LocationTrackingMode.None
                }
            })
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .check()
    }

}