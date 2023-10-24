package com.example.story_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.story_app.data.local.AuthPreference
import com.example.story_app.ui.story.StoryPage
import com.example.story_app.ui.WelcomePage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val authPreference = AuthPreference(this)
        val user = authPreference.getUser()

        val fragmentManager = supportFragmentManager

        if (user.token.isNotEmpty()) {
            val mainFragment = StoryPage()
            fragmentManager.beginTransaction()
                .replace(
                    R.id.frame_container,
                    mainFragment,
                    StoryPage::class.java.simpleName
                )
                .commit()
        } else {
            val welcomeFragment = WelcomePage()
            fragmentManager.beginTransaction()
                .replace(
                    R.id.frame_container,
                    welcomeFragment,
                    WelcomePage::class.java.simpleName
                )
                .commit()
        }
    }
}