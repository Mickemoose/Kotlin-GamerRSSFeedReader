package com.example.assignment7

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar: Toolbar = this.findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)
       // supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Article"

        val titleView = findViewById<TextView>(R.id.titleView)
        val contentView = findViewById<TextView>(R.id.contentView)
        val dateView = findViewById<TextView>(R.id.dateView)
        val imageView2 : ImageView = findViewById(R.id.imageView2)
        val scrollView : ScrollView = findViewById<ScrollView>(R.id.scrollViewDetail)

        titleView.text = articleTitle
        dateView.text = articlePubDate
        contentView.text = articleContent
        if(settingDarkMode) {
            titleView.setTextColor(Color.WHITE)
            dateView.setTextColor(Color.LTGRAY)
            contentView.setTextColor(Color.WHITE)
            scrollView.setBackgroundColor(Color.BLACK)
        } else {
            titleView.setTextColor(Color.BLACK)
            dateView.setTextColor(Color.GRAY)
            contentView.setTextColor(Color.DKGRAY)
            scrollView.setBackgroundColor(Color.WHITE)
        }
        if(articleImageLink != "")
            Picasso.get().load(articleImageLink).into(imageView2);

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu2, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.webGo ->{
                if(settingOpenInBrowser){
                    val openURL = Intent(android.content.Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(articleURL)
                    startActivity(openURL)
                }
                else {
                    val intent = Intent(this, WebActivity::class.java)
                    startActivity(intent)
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }
}