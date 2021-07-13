package com.example.viewbinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.viewbinding.databinding.ActivityMain2Binding

class MainActivity2 : BaseActivity<ActivityMain2Binding>() {
    override fun initCreate() {
        viewBinding.btnOne.setOnClickListener {
              Toast.makeText(this@MainActivity2,"点击one", Toast.LENGTH_SHORT).show()
        }
    }

}