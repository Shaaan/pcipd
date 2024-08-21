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
import `in`.shaaan.pcipharmd.databinding.ActivityFourthYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentFourthYearBinding

class FourthYear : AppCompatActivity(), View.OnClickListener {
    private lateinit var yearBinding: ContentFourthYearBinding
    private var adUtil: AdUtil = AdUtil()
    private lateinit var activityFourthYearBinding: ActivityFourthYearBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFourthYearBinding = ActivityFourthYearBinding.inflate(
            layoutInflater
        )
        setContentView(activityFourthYearBinding.root)
        setSupportActionBar(activityFourthYearBinding.toolbar)
        yearBinding = activityFourthYearBinding.layout4y
        adUtil.loadInterAd(this)

        initializeUI()
        refreshAd()
    }

    private fun initializeUI() {
        yearBinding.tp3.setOnClickListener(this)
        yearBinding.toxicology.setOnClickListener(this)
        yearBinding.hp.setOnClickListener(this)
        yearBinding.cp.setOnClickListener(this)
        yearBinding.biopharm.setOnClickListener(this)
        yearBinding.biostat.setOnClickListener(this)

        activityFourthYearBinding.fab.setOnClickListener { view: View? ->
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
        AdUtil.loadNativeAd(this, yearBinding.nativeAd41, yearBinding.nativeCard41)
        AdUtil.loadNativeAd(this, yearBinding.nativeAd42, yearBinding.nativeCard42)
        AdUtil.refreshAd(this, yearBinding.nativeAd43, yearBinding.nativeCard43)
    }

    override fun onClick(view: View) {
        val baseUrl = getBaseUrl(this)
        val endpointMap: MutableMap<Int, String> = HashMap()
        endpointMap[R.id.tp3] = "fourth_year/pt3"
        endpointMap[R.id.toxicology] = "fourth_year/toxicology"
        endpointMap[R.id.cp] = "fourth_year/cp"
        endpointMap[R.id.hp] = "fourth_year/hosp_pharm"
        endpointMap[R.id.biopharm] = "fourth_year/biopharm"
        endpointMap[R.id.biostat] = "fourth_year/biostat"

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
