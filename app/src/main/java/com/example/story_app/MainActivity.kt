package com.example.story_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.story_app.ui.LoginPage
import com.example.story_app.ui.WelcomePage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val fragmentManager = supportFragmentManager
        val welcomeFragment = WelcomePage()
        val fragment = fragmentManager
            .findFragmentByTag(WelcomePage::class.java.simpleName)
        if (fragment !is WelcomePage){
            fragmentManager
                .beginTransaction()
                .add(
                    R.id.frame_container,
                    welcomeFragment,
                    WelcomePage::class.java.simpleName
                )
                .commit()
        }
    }
}