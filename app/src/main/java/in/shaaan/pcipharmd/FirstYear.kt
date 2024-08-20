package `in`.shaaan.pcipharmd

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import `in`.shaaan.pcipharmd.databinding.ActivityFirstYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentFirstYearBinding

class FirstYear : AppCompatActivity(), View.OnClickListener {
    private var yearBinding: ContentFirstYearBinding? = null
    private var adUtil: AdUtil = AdUtil()
    private var activityFirstYearBinding: ActivityFirstYearBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFirstYearBinding = ActivityFirstYearBinding.inflate(
            layoutInflater
        )
        setContentView(activityFirstYearBinding!!.root)
        setSupportActionBar(activityFirstYearBinding!!.toolbar)
        yearBinding = activityFirstYearBinding!!.layout1y
        adUtil.loadInterAd(this)

        initializeUI()
        refreshAd()
    }

    private fun initializeUI() {
        yearBinding!!.hap.setOnClickListener(this)
        yearBinding!!.ceutics.setOnClickListener(this)
        yearBinding!!.biochem.setOnClickListener(this)
        yearBinding!!.oc.setOnClickListener(this)
        yearBinding!!.ic.setOnClickListener(this)
        yearBinding!!.remMathBio.setOnClickListener(this)

        activityFirstYearBinding!!.fab.setOnClickListener { view: View? ->
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
        AdUtil.loadNativeAd(this, yearBinding!!.nativeAd11, yearBinding!!.nativeCard11)
        AdUtil.loadNativeAd(this, yearBinding!!.nativeAd12, yearBinding!!.nativeCard12)
        AdUtil.loadNativeAd(this, yearBinding!!.nativeAd13, yearBinding!!.nativeCard13)
    }

    override fun onClick(view: View) {
        val urlMap: MutableMap<Int, String> = HashMap()
        urlMap[R.id.hap] = "https://shaaan.github.io/pcipd/syllabus1/hap/"
        urlMap[R.id.ceutics] = "https://shaaan.github.io/pcipd/syllabus1/pahrmaceutics/"
        urlMap[R.id.biochem] = "https://shaaan.github.io/pcipd/syllabus1/biochem/"
        urlMap[R.id.ic] = "https://shaaan.github.io/pcipd/syllabus1/ic/"
        urlMap[R.id.oc] = "https://shaaan.github.io/pcipd/syllabus1/oc/"
        urlMap[R.id.rem_mathBio] = "https://shaaan.github.io/pcipd/syllabus1/math_bio/"

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
