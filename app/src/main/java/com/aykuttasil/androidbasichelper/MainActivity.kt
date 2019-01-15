package com.aykuttasil.androidbasichelper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aykuttasil.androidbasichelperlib.UiHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UiHelper.UiDialog.newInstance(this).getOKDialog("","",null)
    }
}
