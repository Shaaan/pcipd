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
import `in`.shaaan.pcipharmd.databinding.ActivityFirstYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentFirstYearBinding

class FirstYear : AppCompatActivity(), View.OnClickListener {
    private lateinit var yearBinding: ContentFirstYearBinding
    private var adUtil: AdUtil = AdUtil()
    private lateinit var activityFirstYearBinding: ActivityFirstYearBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFirstYearBinding = ActivityFirstYearBinding.inflate(
            layoutInflater
        )
        setContentView(activityFirstYearBinding.root)
        setSupportActionBar(activityFirstYearBinding.toolbar)
        yearBinding = activityFirstYearBinding.layout1y
        adUtil.loadInterAd(this)

        initializeUI()
        refreshAd()
    }

    private fun initializeUI() {
        yearBinding.hap.setOnClickListener(this)
        yearBinding.ceutics.setOnClickListener(this)
        yearBinding.biochem.setOnClickListener(this)
        yearBinding.oc.setOnClickListener(this)
        yearBinding.ic.setOnClickListener(this)
        yearBinding.remMathBio.setOnClickListener(this)

        activityFirstYearBinding.fab.setOnClickListener { view: View? ->
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
        AdUtil.loadNativeAd(this, yearBinding.nativeAd11, yearBinding.nativeCard11)
        AdUtil.loadNativeAd(this, yearBinding.nativeAd12, yearBinding.nativeCard12)
        AdUtil.refreshAd(this, yearBinding.nativeAd13, yearBinding.nativeCard13)
    }

    override fun onClick(view: View) {
        val baseUrl = getBaseUrl(this)
        val endpointMap: MutableMap<Int, String> = HashMap()
        endpointMap[R.id.hap] = "first_year/hap/"
        endpointMap[R.id.ceutics] = "first_year/pahrmaceutics/"
        endpointMap[R.id.biochem] = "first_year/biochem/"
        endpointMap[R.id.ic] = "first_year/ic/"
        endpointMap[R.id.oc] = "first_year/oc/"
        endpointMap[R.id.rem_mathBio] = "first_year/math_bio/"

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
