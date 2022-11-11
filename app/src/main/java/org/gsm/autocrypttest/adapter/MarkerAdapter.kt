package org.gsm.autocrypttest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.naver.maps.map.overlay.InfoWindow
import org.gsm.autocrypttest.databinding.CenterInfoBinding
import org.gsm.autocrypttest.viewmodel.CenterViewModel

class MarkerAdapter(
    context: Context,
   private val vm : CenterViewModel
) : InfoWindow.DefaultViewAdapter(context) {
    private val binding by lazy { CenterInfoBinding.inflate(LayoutInflater.from(context)) }

    override fun getContentView(info: InfoWindow): View {
        binding.vm = vm
        binding.address.text = "주소: "+vm.addr.value
        binding.centerName.text = "센터명: "+vm.centerName.value
        binding.facilityName.text = "기관명: "+vm.facilityName.value
        binding.phoneNumber.text = "전화번호: "+vm.phoneNum.value
        binding.updateAt.text = "업데이트: "+vm.updateAt.value


        return binding.root
    }

}