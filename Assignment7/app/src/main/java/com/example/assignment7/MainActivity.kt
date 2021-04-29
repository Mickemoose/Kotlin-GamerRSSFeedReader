package com.example.assignment7

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
val myPrefs : String = "MyPrefsFile"
var articleURL: String = ""
var articleTitle: String = ""
var lastTitle : String = ""
var articleContent: String =""
var articlePubDate: String =""
var articleImageLink: String=""
val pcgFeed: String ="https://www.pcgamer.com/rss/"
val ninFeed: String ="https://www.nintendolife.com/feeds/news"
val rpsFeed: String ="https://www.rockpapershotgun.com/feed/"
val xbxFeed: String ="https://news.xbox.com/en-us/feed/stories/"
val psFeed: String = "https://blog.playstation.com/feed/"
val gamespotFeed: String ="https://www.gamespot.com/feeds/game-news/"
val metaFeed : String ="https://www.metacritic.com/rss/features"
val giantbombFeed : String = "https://www.giantbomb.com/feeds/news/"
var chosenFeed: String = pcgFeed

var settingFeed: String = pcgFeed
var settingOpenInBrowser : Boolean = false
var settingDarkMode : Boolean = false
var setting24hour : Boolean = true

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val articleList: ArrayList<Article> = ArrayList()
    private lateinit var listView: ListView
    private lateinit var articleAdapter : ArticleAdapter
    private var feedTitle: String = "PC Gamer"

    override fun onStop() {
        val editor = getSharedPreferences(myPrefs, MODE_PRIVATE)
        when(settingFeed) {
            pcgFeed -> {editor.edit().putString("settingTitle", "PC Gamer").apply()}
            ninFeed -> {editor.edit().putString("settingTitle", "Nintendo Life").apply()}
            rpsFeed -> {editor.edit().putString("settingTitle", "Rock Paper Shotgun").apply()}
            psFeed -> {editor.edit().putString("settingTitle", "Playstation Blog").apply()}
            xbxFeed -> {editor.edit().putString("settingTitle", "Xbox Wire").apply()}
            gamespotFeed -> {editor.edit().putString("settingTitle", "GameSpot").apply()}
            giantbombFeed -> {editor.edit().putString("settingTitle", "Giant Bomb").apply()}
            metaFeed -> {editor.edit().putString("settingTitle", "Metacritic").apply()}
        }
        editor.edit().putString("settingFeed", settingFeed).apply()

        super.onStop()
    }
    override fun onDestroy() {
        val editor = getSharedPreferences(myPrefs, MODE_PRIVATE)
        when(settingFeed) {
            pcgFeed -> {editor.edit().putString("settingTitle", "PC Gamer").apply()}
            ninFeed -> {editor.edit().putString("settingTitle", "Nintendo Life").apply()}
            rpsFeed -> {editor.edit().putString("settingTitle", "Rock Paper Shotgun").apply()}
            psFeed -> {editor.edit().putString("settingTitle", "Playstation Blog").apply()}
            xbxFeed -> {editor.edit().putString("settingTitle", "Xbox Wire").apply()}
            gamespotFeed -> {editor.edit().putString("settingTitle", "GameSpot").apply()}
            giantbombFeed -> {editor.edit().putString("settingTitle", "Giant Bomb").apply()}
            metaFeed -> {editor.edit().putString("settingTitle", "Metacritic").apply()}

        }
        editor.edit().putString("settingFeed", settingFeed).apply()

        super.onDestroy()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = getSharedPreferences(myPrefs, MODE_PRIVATE)
        chosenFeed = prefs.getString("settingFeed", pcgFeed).toString()
        settingDarkMode = prefs.getBoolean("settingDarkMode", false)
        setting24hour = prefs.getBoolean("setting24hour", true)
        val toolbar: Toolbar = this.findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        isNetworkAvailable()
       // processRSS()
        val rssProcessingTask = RssProcessingTask()
        rssProcessingTask.execute()
        articleAdapter = ArticleAdapter(applicationContext, R.layout.list_item, articleList)

        //set the adapter of the ListView
        listView = findViewById(R.id.listView)


        //have listview respond to selected items
        listView.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l -> //we could start an activity to display details of the selected item
                val intent = Intent(this, DetailActivity::class.java)
                articleURL = articleList.elementAt(i).linkurl
                articleTitle = articleList.elementAt(i).title
                articlePubDate = articleList.elementAt(i).pubDate
                articleContent = articleList.elementAt(i).content
                articleImageLink = articleList.elementAt(i).imageurl
                startActivity(intent)

            }
        title = prefs.getString("settingTitle", "PC Gamer")
        if(!settingDarkMode) {
            listView.setBackgroundColor(Color.WHITE)
            val currentLayout: LinearLayout = findViewById<LinearLayout>(R.id.mainlayout)
            currentLayout.setBackgroundColor(Color.DKGRAY)
        } else {
            listView.setBackgroundColor(Color.BLACK)
            val currentLayout: LinearLayout = findViewById<LinearLayout>(R.id.mainlayout)
            currentLayout.setBackgroundColor(Color.LTGRAY)
        }
        when(title){
            "PC Gamer" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPrimaryDarkNintendo
                        )
                    )
                )
            }
            "Nintendo Life" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPrimaryNintendo
                        )
                    )
                )
            }
            "Playstation Blog" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPlaystation
                        )
                    )
                )
            }
            "Metacritic" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorYellow
                        )
                    )
                )
            }
            "Xbox Wire" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorXbox
                        )
                    )
                )
            }
            "GameSpot" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorOrange
                        )
                    )
                )
            }
            "Rock Paper Shotgun" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPurple
                        )
                    )
                )
            }
            "Giant Bomb" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPrimaryDarkNintendo
                        )
                    )
                )
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onResume() {
        val prefs = getSharedPreferences(myPrefs, MODE_PRIVATE)


        settingDarkMode = prefs.getBoolean("settingDarkMode", false)
        setting24hour = prefs.getBoolean("setting24hour", true)
        //chosenFeed = prefs.getString("settingFeed", pcgFeed).toString()
        settingOpenInBrowser = prefs.getBoolean("settingBrowser", false)
        title = prefs.getString("settingTitle", "PC Gamer")
        when(title){
            "PC Gamer" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPrimaryDarkNintendo
                        )
                    )
                )
            }
            "Metacritic" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorYellow
                        )
                    )
                )
            }
            "Nintendo Life" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPrimaryNintendo
                        )
                    )
                )
            }
            "Playstation Blog" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPlaystation
                        )
                    )
                )
            }
            "Xbox Wire" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorXbox
                        )
                    )
                )
            }
            "GameSpot" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorOrange
                        )
                    )
                )
            }
            "Rock Paper Shotgun" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPurple
                        )
                    )
                )
            }
            "Giant Bomb" -> {
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPrimaryDarkNintendo
                        )
                    )
                )
            }
        }
        articleAdapter.clearData()
        articleAdapter.notifyDataSetChanged()
        val rssProcessingTask = RssProcessingTask()
        rssProcessingTask.execute()
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                articleAdapter.notifyDataSetChanged()
            }
            R.id.more -> {
                lastTitle = title.toString()
                val intent = Intent(this, SelectorActivity::class.java)
                startActivityForResult(intent, 1)
            }
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent, 1)
            }
            R.id.pcgamer -> {
                chosenFeed = pcgFeed
                title = "PC Gamer"
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPrimaryDarkNintendo
                        )
                    )
                )
                articleAdapter.clearData()
                articleAdapter.notifyDataSetChanged()
                val rssProcessingTask = RssProcessingTask()
                rssProcessingTask.execute()
            }
            R.id.rockpapershotgun -> {
                chosenFeed = rpsFeed
                title = "Rock Paper Shotgun"
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorPurple
                        )
                    )
                )
                articleAdapter.clearData()
                articleAdapter.notifyDataSetChanged()
                val rssProcessingTask = RssProcessingTask()
                rssProcessingTask.execute()
            }
            R.id.gamespot -> {
                chosenFeed = gamespotFeed
                title = "GameSpot"
                toolbar.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.colorOrange
                        )
                    )
                )
                articleAdapter.clearData()
                articleAdapter.notifyDataSetChanged()
                val rssProcessingTask = RssProcessingTask()
                rssProcessingTask.execute()
            }
        }
        return true
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    //multi-threading (asynchronous processing) example -- using AsyncTask
    fun processRSS(view: View?) {
        //create an instance of our AsyncTask... and execute it!
        val rssProcessingTask = RssProcessingTask()
        rssProcessingTask.execute()
        //Log.d("LOL", "$view")
    }

    //nested class example of an AsyncTask
    inner class RssProcessingTask : AsyncTask<Void?, Void?, Void?>() {
        //can access the UI/system/main thread
        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("JG", "onPreExecute()")
        }

        //the "main" method of the AsyncTask
        //does NOT have access to the UI/system thread
        override fun doInBackground(vararg voids: Void?): Void? {
            Log.d("JG", "doInBackground()")

            //for(int i=0; i<120000; i++) {
            //    Log.d("JG", "i = " + i);
            //}
            val saxParserFactory = SAXParserFactory.newInstance()
            var saxParser: SAXParser? = null
            try {
                saxParser = saxParserFactory.newSAXParser()
            } catch (e: ParserConfigurationException) {
                e.printStackTrace()
            } catch (e: SAXException) {
                e.printStackTrace()
            }
            var rssFeedURL: URL? = null
            try {
                rssFeedURL = URL(chosenFeed)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
            var inputStream: InputStream? = null
            try {
                inputStream = rssFeedURL!!.openStream()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            //create instance of our subclassed DefaultHandler
            val freepHandler = FreepHandler()
            //parse the inputSteam using our custom handler
            try {
                saxParser!!.parse(inputStream, freepHandler)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: SAXException) {
                e.printStackTrace()
            }
            return null
        }

        //can access the UI/system/main thread
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            articleAdapter = ArticleAdapter(applicationContext, R.layout.list_item, articleList)
            //set the adapter of the ListView
            listView = findViewById(R.id.listView)
            listView.adapter = articleAdapter
        }

    }

    //handler for SAX parsing Winnipeg Free Press RSS feeds
    inner class FreepHandler : DefaultHandler() {


        //private val articleList =  ArrayList<Article>()
        private val title = ArrayList<String>()
        private val imageLink = ArrayList<String>()
        private val desc = ArrayList<String>()
        private val pubDate = ArrayList<String>()
        private val link = ArrayList<String>()
        private val content = ArrayList<String>()
        private var inTitle : Boolean = false
        private var inDesc : Boolean = false
        private var inLink : Boolean = false
        private var inURL : Boolean = false
        private var inMediaContent : Boolean =false
        private var inContent : Boolean = false
        private var inPubDate : Boolean = false
        private var inImage : Boolean = false
        private var inItem: Boolean = false
        private var iTitle : String = ""
        private var iDesc : String = ""
        private var iPubDate : String = ""
        private var iImageLink : String = ""
        private var iLinkUrl : String = ""
        private var iContent : String =""
        private lateinit var stringBuilder : StringBuilder

        @Throws(SAXException::class)
        override fun startDocument() {
            super.startDocument()
            Log.d("JG", "startDocument()")
        }

        @Throws(SAXException::class)
        override fun endDocument() {
            super.endDocument()
            Log.d("JG", "endDocument()")
        }

        @Throws(SAXException::class)
        override fun startElement(
            uri: String,
            localName: String,
            qName: String,
            attributes: Attributes
        ) {
            super.startElement(uri, localName, qName, attributes)
            Log.d("JG", "startElement() $qName")
            if (qName == "media:content")  {
                inMediaContent = true
                stringBuilder = StringBuilder(12000)
            }
            if(qName == "dc:content" || qName=="content:encoded"){
                inContent = true
                stringBuilder = StringBuilder(1200000)
            }
            if(qName == "link"){
                inLink = true
                stringBuilder = StringBuilder(12000)
            }
            if(qName == "item") {
                inItem = true
            }
            if(qName == "image") {
                inImage = true
            }
            if(qName == "url"){
                inURL = true
                stringBuilder = StringBuilder(1000)
            }
            if(qName == "title") {
                inTitle = true
                stringBuilder = StringBuilder(500)
            }

            if(qName == "description") {
                inDesc = true
                stringBuilder = StringBuilder(12000)
            }

            if(qName == "pubDate") {
                inPubDate = true
                stringBuilder = StringBuilder(12000)
            }
        }

        @Throws(SAXException::class)
        override fun endElement(uri: String, localName: String, qName: String) {
            super.endElement(uri, localName, qName)
            Log.d("JG", "endElement() $qName")
            if(qName == "image") {
                inImage = false
            }
            if(qName == "url"){
                inURL = false
                if(inImage)
                    iImageLink = stringBuilder.toString()

            }
            if(qName == "title") {
                inTitle = false
                title.add(stringBuilder.toString())
                if(!inImage)
                    iTitle = stringBuilder.toString()
            }
            if(qName == "description") {
                inDesc = false
                desc.add(Html.fromHtml(stringBuilder.toString().trimIndent()).toString())
                iDesc = Html.fromHtml(stringBuilder.toString().trimIndent()).toString()
            }
            if(qName=="dc:content" || qName=="content:encoded"){
                inContent = false
                content.add(Html.fromHtml(stringBuilder.toString().trimIndent()).toString())
                iContent = Html.fromHtml(stringBuilder.toString().trimIndent()).toString()
            }

            if(qName == "link") {
                inLink = false
                link.add(stringBuilder.toString())
                iLinkUrl = stringBuilder.toString()
            }
            if(qName == "pubDate") {
                inPubDate = false
                pubDate.add(stringBuilder.toString())
                iPubDate = stringBuilder.toString()
                if(setting24hour) {
                    //grab date from RSS
                    val dateFormat: DateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
                    val newdate = dateFormat.parse(Date(iPubDate).toString())
                    //reformat date
                    val dateFormat2: DateFormat = SimpleDateFormat(
                        "EEE, MMM dd yyyy HH:mm ", Locale.US
                    )

                    iPubDate = dateFormat2.format(newdate).toString()

                } else {
                    //grab date from RSS
                    val dateFormat: DateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
                    val newdate = dateFormat.parse(Date(iPubDate).toString())
                    //reformat date
                    val dateFormat2: DateFormat = SimpleDateFormat(
                        "EEE, MMM dd yyyy hh:mm aa", Locale.US
                    )

                    iPubDate = dateFormat2.format(newdate).toString()
                }

            }

            if(qName == "item") {
                inItem = false
                if(!inImage)
                    Log.d("DMY", "ARTICLE: $iTitle $iPubDate")
                    if(iImageLink.isEmpty())
                        iImageLink = ""
                    articleList.add(
                        Article(
                            iTitle,
                            iDesc,
                            iPubDate,
                            iImageLink,
                            iLinkUrl,
                            iContent
                        )
                    )
            }

        }

        @Throws(SAXException::class)
        override fun characters(ch: CharArray, start: Int, length: Int) {
            super.characters(ch, start, length)
            val s  = String(ch, start, length)

            if(inURL && inImage){
                imageLink.add(s)
                stringBuilder.append(ch, start, length)
            }
            if(inLink) {
                link.add(s)
                stringBuilder.append(ch, start, length)
            }
            if(inTitle) {
                title.add(s)
                stringBuilder.append(ch, start, length)
            }
            if(inDesc) {
                desc.add(s)
                stringBuilder.append(ch, start, length)
            }
            if(inPubDate) {
                pubDate.add(s)
                stringBuilder.append(ch, start, length)
            }
            if(inContent) {
                content.add(s)
                stringBuilder.append(ch, start, length)
            }
        }
    }

    //custom ArrayAdapter for our ListView
    private class ArticleAdapter(
        context: Context,
        textViewResourceId: Int,
        val items: ArrayList<Article>
    ) : BaseAdapter()
    {
        private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        //This method is called once for every item in the ArrayList as the list is loaded.
        //It returns a View -- a list item in the ListView -- for each item in the ArrayList

        //1
        override fun getCount(): Int {
            return items.size
        }

        //2
        override fun getItem(position: Int): Any {
            return items[position]
        }

        //3
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        fun clearData() {
            // clear the data
            items.clear()
        }
        //4
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val rowView = inflater.inflate(R.layout.list_item, parent, false)

            val tt = rowView.findViewById<View>(R.id.toptext) as TextView
            val bt = rowView.findViewById<View>(R.id.bottomtext) as TextView
            val pdt = rowView.findViewById<View>(R.id.pubdateview) as TextView
            val image = rowView.findViewById(R.id.imageView) as ImageView
            val o: Article = items[position]
            tt.text = o.title
            pdt.text = o.pubDate
            bt.text = o.description
            if(!settingDarkMode) {
                tt.setTextColor(Color.BLACK)
                pdt.setTextColor(Color.GRAY)
                bt.setTextColor(Color.DKGRAY)
                val currentLayout: RelativeLayout = rowView.findViewById<RelativeLayout>(R.id.listlayout)
                currentLayout.setBackgroundColor(Color.WHITE)
            } else {
                tt.setTextColor(Color.WHITE)
                pdt.setTextColor(Color.GRAY)
                bt.setTextColor(Color.LTGRAY)
                val currentLayout: RelativeLayout = rowView.findViewById<RelativeLayout>(R.id.listlayout)
                currentLayout.setBackgroundColor(Color.BLACK)
            }

            if(o.imageurl != "")
                Picasso.get().load(o.imageurl).into(image);
            return rowView
        }
    }

    //class to define properties of each item in the list
    class Article(
        var title: String,
        var description: String,
        var pubDate: String,
        var imageurl: String,
        var linkurl: String,
        var content: String
    )


}