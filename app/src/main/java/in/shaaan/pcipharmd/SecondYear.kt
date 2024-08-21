package `in`.shaaan.pcipharmd

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import `in`.shaaan.pcipharmd.HomeActivity.Companion.getBaseUrl
import `in`.shaaan.pcipharmd.databinding.ActivitySecondYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentSecondYearBinding

class SecondYear : AppCompatActivity(), View.OnClickListener {
    private lateinit var yearBinding: ContentSecondYearBinding
    private var adUtil: AdUtil = AdUtil()
    private lateinit var activitySecondYearBinding: ActivitySecondYearBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySecondYearBinding = ActivitySecondYearBinding.inflate(
            layoutInflater
        )
        setContentView(activitySecondYearBinding.root)
        setSupportActionBar(activitySecondYearBinding.toolbar)
        yearBinding = activitySecondYearBinding.layout2y
        adUtil.loadInterAd(this)

        initializeUI()
        refreshAd()
    }

    private fun initializeUI() {
        yearBinding.cology.setOnClickListener(this)
        yearBinding.tp1.setOnClickListener(this)
        yearBinding.cognosy.setOnClickListener(this)
        yearBinding.micro.setOnClickListener(this)
        yearBinding.patho.setOnClickListener(this)
        yearBinding.com.setOnClickListener(this)

        activitySecondYearBinding.fab.setOnClickListener { view: View? ->
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
        AdUtil.loadNativeAd(this, yearBinding.nativeAd21, yearBinding.nativeCard21)
        AdUtil.loadNativeAd(this, yearBinding.nativeAd22, yearBinding.nativeCard22)
        AdUtil.refreshAd(this, yearBinding.nativeAd23, yearBinding.nativeCard23)
    }

    override fun onClick(view: View) {
        val baseUrl = getBaseUrl(this)
        val endpointMap: MutableMap<Int, String> = HashMap()
        endpointMap[R.id.cology] = "second_year/cology"
        endpointMap[R.id.tp1] = "second_year/tp1"
        endpointMap[R.id.micro] = "second_year/micro"
        endpointMap[R.id.cognosy] = "second_year/cognosy"
        endpointMap[R.id.patho] = "second_year/patho"
        endpointMap[R.id.com] = "second_year/cmp"

        val endpoint = endpointMap[view.id]
        val url = if (endpoint != null) baseUrl + endpoint else null
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
