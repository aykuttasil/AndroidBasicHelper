package com.aykuttasil.androidbasichelper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aykuttasil.androidbasichelperlib.UiHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTheme(R.style.AppTheme_MyX)

        btnShowDialog.setOnClickListener {
            //UiHelper.UiDialog.newInstance(this).getOKDialog("title", "content", null).debugMode(false).show()
            //UiHelper.UiDialog.newInstance(this).getProgressDialog("Reaktif", "İşlem Yapılıyor.\nLütfen Bekleyin...", null).debugMode(false).show()
            UiHelper.UiDialog.newInstance(this).getIndeterminateDialog("Reaktif").show()

        }
    }
}
