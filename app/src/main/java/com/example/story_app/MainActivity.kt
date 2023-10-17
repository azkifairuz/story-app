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
        val searchUserFragment = LoginPage()
        val fragment = fragmentManager
            .findFragmentByTag(LoginPage::class.java.simpleName)
        if (fragment !is LoginPage){
            fragmentManager
                .beginTransaction()
                .add(
                    R.id.frame_container,
                    searchUserFragment,
                    LoginPage::class.java.simpleName
                )
                .commit()
        }
    }
}