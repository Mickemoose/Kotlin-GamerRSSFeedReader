package com.example.assignment7


import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        val toolbar: Toolbar = this.findViewById(R.id.toolbar3)
        setSupportActionBar(toolbar)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Settings"

    }



    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            //getListView().setBackgroundColor(Color.TRANSPARENT);

            val clockSwitch = findPreference("clockSET") as SwitchPreferenceCompat?
            clockSwitch?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _: Preference, _: Any ->
                    if (clockSwitch!!.isChecked) {
                        clockSwitch.isChecked = false
                        setting24hour = false

                    } else {
                        clockSwitch.isChecked = true
                        setting24hour = true
                    }
                    val editor = activity?.getSharedPreferences(myPrefs, MODE_PRIVATE)
                    editor?.edit()?.putBoolean("setting24hour", setting24hour)?.apply()

                    setting24hour
                }

            val darkmodeSwitch = findPreference("darkmodeSET") as SwitchPreferenceCompat?
            darkmodeSwitch?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _: Preference, _: Any ->
                    if (darkmodeSwitch!!.isChecked) {
                        darkmodeSwitch.isChecked = false
                        settingDarkMode = false

                    } else {
                        darkmodeSwitch.isChecked = true
                        settingDarkMode = true
                    }
                    val editor = activity?.getSharedPreferences(myPrefs, MODE_PRIVATE)
                    editor?.edit()?.putBoolean("settingDarkMode", settingDarkMode)?.apply()

                    settingDarkMode
                }

            val browserSwitch = findPreference("browserSET") as SwitchPreferenceCompat?
            browserSwitch?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _: Preference, _: Any ->
                    if (browserSwitch!!.isChecked) {
                        browserSwitch.isChecked = false
                        settingOpenInBrowser = false

                    } else {
                        browserSwitch.isChecked = true
                        settingOpenInBrowser = true
                    }
                    val editor = activity?.getSharedPreferences(myPrefs, MODE_PRIVATE)
                    editor?.edit()?.putBoolean("settingBrowser", settingOpenInBrowser)?.apply()

                    settingOpenInBrowser
                }


            val feedList = findPreference("feedSET") as ListPreference?
            feedList?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _: Preference, value: Any ->
                    when (value) {
                        "pcgFeed" -> {
                            settingFeed = pcgFeed
                            settingFeed = pcgFeed
                            val editor = activity?.getSharedPreferences(myPrefs, MODE_PRIVATE)
                            editor?.edit()?.putString("settingTitle", "PC Gamer")?.apply()
                        }
                        "ninFeed" -> {
                            settingFeed = ninFeed
                            settingFeed = ninFeed
                            val editor = activity?.getSharedPreferences(myPrefs, MODE_PRIVATE)
                            editor?.edit()?.putString("settingTitle", "Nintendo Life")?.apply()
                        }
                        "xbxFeed" -> {
                            settingFeed = xbxFeed
                            settingFeed = xbxFeed
                            val editor = activity?.getSharedPreferences(myPrefs, MODE_PRIVATE)
                            editor?.edit()?.putString("settingTitle", "Xbox Wire")?.apply()
                        }
                        "rpsFeed" -> {
                            settingFeed = rpsFeed
                            settingFeed = rpsFeed
                            val editor = activity?.getSharedPreferences(myPrefs, MODE_PRIVATE)
                            editor?.edit()?.putString("settingTitle", "Rock Paper Shotgun")?.apply()
                        }
                        "psFeed" -> {
                            settingFeed = psFeed
                            settingFeed = psFeed
                            val editor = activity?.getSharedPreferences(myPrefs, MODE_PRIVATE)
                            editor?.edit()?.putString("settingTitle", "Playstation Blog")?.apply()
                        }
                        "gamespotFeed" -> {
                            settingFeed = gamespotFeed
                            settingFeed = gamespotFeed
                            val editor = activity?.getSharedPreferences(myPrefs, MODE_PRIVATE)
                            editor?.edit()?.putString("settingTitle", "GameSpot")?.apply()
                        }
                    }
                    val editor = activity?.getSharedPreferences(myPrefs, MODE_PRIVATE)
                    editor?.edit()?.putString("settingFeed", settingFeed)?.apply()
                    chosenFeed = settingFeed
                    true
                }
        }


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