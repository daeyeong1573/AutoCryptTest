package org.gsm.autocrypttest.base

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseActivity<T:ViewDataBinding>(@LayoutRes private val layoutResId:Int) : AppCompatActivity() {
    private var _binding: T? = null
    val binding get() = _binding!!

    abstract val TAG : String
    private var waitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        init()
    }

    private fun performDataBinding() {
        _binding = DataBindingUtil.setContentView(this,layoutResId)
        binding.lifecycleOwner = this@BaseActivity

    }

    abstract fun init()

    override fun onBackPressed() {
        if (System.currentTimeMillis() - waitTime >= 1500) {
            waitTime = System.currentTimeMillis()
            makeToast("한 번 더 누르시면 종료됩니다.")
        } else finish()
    }


    fun makeToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}