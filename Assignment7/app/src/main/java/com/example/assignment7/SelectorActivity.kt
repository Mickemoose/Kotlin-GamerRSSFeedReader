package com.example.assignment7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
class SelectorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selector)
        val toolbar: Toolbar = this.findViewById(R.id.toolbarSEL)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Select a Feed"
        val btnPCG = findViewById<ImageButton>(R.id.pcgamerBUTTON)
        btnPCG.setOnClickListener {
            chosenFeed = pcgFeed
            val editor = getSharedPreferences(myPrefs, MODE_PRIVATE)
            editor.edit().putString("settingTitle", "PC Gamer").apply()
            finish()
        }

        val btnXBOX = findViewById<ImageButton>(R.id.xboxBUTTON)
        btnXBOX.setOnClickListener {
            chosenFeed = xbxFeed
            val editor = getSharedPreferences(myPrefs, MODE_PRIVATE)
            editor.edit().putString("settingTitle", "Xbox Wire").apply()
            finish()
        }

        val btnRPS = findViewById<ImageButton>(R.id.rpsBUTTON)
        btnRPS.setOnClickListener {
            chosenFeed = rpsFeed
            val editor = getSharedPreferences(myPrefs, MODE_PRIVATE)
            editor.edit().putString("settingTitle", "Rock Paper Shotgun").apply()
            finish()
        }

        val btnGST = findViewById<ImageButton>(R.id.gamespotBUTTON)
        btnGST.setOnClickListener {
            chosenFeed = gamespotFeed
            val editor = getSharedPreferences(myPrefs, MODE_PRIVATE)
            editor.edit().putString("settingTitle", "GameSpot").apply()
            finish()
        }

        val btnNIN = findViewById<ImageButton>(R.id.nintendoBUTTON)
        btnNIN.setOnClickListener {
            chosenFeed = ninFeed
            val editor = getSharedPreferences(myPrefs, MODE_PRIVATE)
            editor.edit().putString("settingTitle", "Nintendo Life").apply()
            finish()
        }

        val btnPS = findViewById<ImageButton>(R.id.playstationBUTTON)
        btnPS.setOnClickListener {
            chosenFeed = psFeed
            val editor = getSharedPreferences(myPrefs, MODE_PRIVATE)
            editor.edit().putString("settingTitle", "Playstation Blog").apply()
            finish()
        }

        val btnMETA = findViewById<ImageButton>(R.id.metacriticBUTTON)
        btnMETA.setOnClickListener {
            chosenFeed = metaFeed
            val editor = getSharedPreferences(myPrefs, MODE_PRIVATE)
            editor.edit().putString("settingTitle", "Metacritic").apply()
            finish()
        }
        val btnGB = findViewById<ImageButton>(R.id.giantbombBUTTON)
        btnGB.setOnClickListener {
            chosenFeed = giantbombFeed
            val editor = getSharedPreferences(myPrefs, MODE_PRIVATE)
            editor.edit().putString("settingTitle", "Giant Bomb").apply()
            finish()
        }
    }

    override fun onBackPressed() {
        val editor = getSharedPreferences(myPrefs, MODE_PRIVATE)
        editor.edit().putString("settingTitle", lastTitle).apply()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
