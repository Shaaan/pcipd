package `in`.shaaan.pcipharmd

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import `in`.shaaan.pcipharmd.databinding.ActivityFifthYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentFifthYearBinding

class FifthYear : AppCompatActivity(), View.OnClickListener {
    private lateinit var yearBinding: ContentFifthYearBinding
    private var adUtil: AdUtil = AdUtil()
    private lateinit var activityFifthYearBinding: ActivityFifthYearBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFifthYearBinding = ActivityFifthYearBinding.inflate(
            layoutInflater
        )
        setContentView(activityFifthYearBinding.root)
        setSupportActionBar(activityFifthYearBinding.toolbar)
        yearBinding = activityFifthYearBinding.layout5y
        adUtil.loadInterAd(this)

        initializeUI()
        refreshAd()
    }

    private fun initializeUI() {
        yearBinding.nativeCard51.visibility = View.GONE
        yearBinding.nativeCard52.visibility = View.GONE
        yearBinding.epi.setOnClickListener(this)
        yearBinding.tdm.setOnClickListener(this)
        yearBinding.research.setOnClickListener(this)

        activityFifthYearBinding.fab.setOnClickListener { view: View? ->
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
        AdUtil.loadNativeAd(this, yearBinding.nativeAd51, yearBinding.nativeCard51)
        AdUtil.loadNativeAd(this, yearBinding.nativeAd52, yearBinding.nativeCard52)
        AdUtil.refreshAd(this, yearBinding.nativeAd53, yearBinding.nativeCard53)
    }

    override fun onClick(view: View) {
        val urlMap: MutableMap<Int, String> = HashMap()
        urlMap[R.id.epi] = "https://shaaan.github.io/pcipd/syllabus5/epi"
        urlMap[R.id.research] = "https://shaaan.github.io/pcipd/syllabus5/research"
        urlMap[R.id.tdm] = "https://shaaan.github.io/pcipd/syllabus5/tdm"

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

    public override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        AdUtil.interstitialAd = null
    }
}
