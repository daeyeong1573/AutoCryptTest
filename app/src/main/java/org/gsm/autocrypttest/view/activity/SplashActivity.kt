package org.gsm.autocrypttest.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.gsm.autocrypttest.R
import org.gsm.autocrypttest.base.BaseActivity
import org.gsm.autocrypttest.data.model.Data
import org.gsm.autocrypttest.databinding.ActivitySplashBinding
import org.gsm.autocrypttest.viewmodel.CenterViewModel

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1004
    }

    override val TAG: String
        get() = SplashActivity::class.java.simpleName

    private val vm: CenterViewModel by viewModels()


    override fun init() {
        binding.vm = vm

        CoroutineScope(Dispatchers.Main).launch {
            vm.getLineCnt()
            vm.setProcess()
            vm.progress.observe(this@SplashActivity){
                if (it == 100) {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }

    }

    override fun onPause() {
        super.onPause()
        CoroutineScope(Dispatchers.Main).cancel()
    }


}