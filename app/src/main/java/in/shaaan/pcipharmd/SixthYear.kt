package `in`.shaaan.pcipharmd

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.material.snackbar.Snackbar
import `in`.shaaan.pcipharmd.databinding.ActivitySixthYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentSixthYearBinding

class SixthYear : AppCompatActivity(), View.OnClickListener {
    private lateinit var yearBinding: ContentSixthYearBinding
    private var adUtil: AdUtil = AdUtil()
    private lateinit var activitySixthYearBinding: ActivitySixthYearBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySixthYearBinding = ActivitySixthYearBinding.inflate(
            layoutInflater
        )
        setContentView(activitySixthYearBinding.root)
        setSupportActionBar(activitySixthYearBinding.toolbar)
        yearBinding = activitySixthYearBinding.layout6y
        adUtil.loadInterAd(this)
        yearBinding.internActivities.setOnClickListener(this)
        yearBinding.internDocuments.setOnClickListener(this)

        initializeUI()
        refreshAd()
    }

    private fun initializeUI() {
        yearBinding.internActivities.setOnClickListener(this)
        yearBinding.internDocuments.setOnClickListener(this)

        activitySixthYearBinding.fab.setOnClickListener { view: View? ->
            Snackbar.make(
                view!!, "Like the app? Rate it on Play Store!", Snackbar.LENGTH_LONG
            )
                .setAction("RATE") {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=in.shaaan.pcipharmd")
                    )
                    startActivity(intent)
                }.show()
        }

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun refreshAd() {
        AdUtil.loadNativeAd(this, yearBinding.nativeAd61, yearBinding.nativeCard61)
        AdUtil.loadNativeAd(this, yearBinding.nativeAd62, yearBinding.nativeCard62)
    }

    override fun onClick(view: View) {
        val urlMap: MutableMap<Int, String> = HashMap()
        urlMap[R.id.intern_activities] = "https://shaaan.github.io/pcipd/syllabus6"
        urlMap[R.id.intern_documents] = "https://shaaan.github.io/pcipd/syllabus6_1/"

        val url = urlMap[view.id]
        if (url != null) {
            adUtil.showInterAd(this, url)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            startActivity(Intent(this, About::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        AdUtil.interstitialAd = null
    }
}
