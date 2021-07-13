package com.example.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    protected lateinit var viewBinding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pt = javaClass.genericSuperclass as ParameterizedType
        val cla = pt.actualTypeArguments[0] as Class<*>
        try {
            val declaredMethod = cla.getDeclaredMethod("inflate", LayoutInflater::class.java)
            viewBinding = declaredMethod.invoke(null, layoutInflater) as T
            setContentView(viewBinding.root)
            initCreate()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    abstract fun initCreate()
}