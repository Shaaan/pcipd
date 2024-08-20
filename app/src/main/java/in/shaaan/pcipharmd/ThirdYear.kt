package `in`.shaaan.pcipharmd

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import `in`.shaaan.pcipharmd.databinding.ActivityThirdYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentThirdYearBinding

class ThirdYear : AppCompatActivity(), View.OnClickListener {
    private lateinit var yearBinding: ContentThirdYearBinding
    private var adUtil: AdUtil = AdUtil()
    private lateinit var activityThirdYearBinding: ActivityThirdYearBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityThirdYearBinding = ActivityThirdYearBinding.inflate(
            layoutInflater
        )
        setContentView(activityThirdYearBinding.root)
        setSupportActionBar(activityThirdYearBinding.toolbar)
        yearBinding = activityThirdYearBinding.layout3y
        adUtil.loadInterAd(this)

        initializeUI()
        refreshAd()
    }

    private fun initializeUI() {
        yearBinding.tp2.setOnClickListener(this)
        yearBinding.pharmac.setOnClickListener(this)
        yearBinding.formulation.setOnClickListener(this)
        yearBinding.analysis.setOnClickListener(this)
        yearBinding.juris.setOnClickListener(this)
        yearBinding.mchem.setOnClickListener(this)

        activityThirdYearBinding.fab.setOnClickListener { view: View? ->
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
        AdUtil.loadNativeAd(this, yearBinding.nativeAd31, yearBinding.nativeCard31)
        AdUtil.loadNativeAd(this, yearBinding.nativeAd32, yearBinding.nativeCard32)
        AdUtil.loadNativeAd(this, yearBinding.nativeAd33, yearBinding.nativeCard33)
    }

    override fun onClick(view: View) {
        val urlMap: MutableMap<Int, String> = HashMap()
        urlMap[R.id.tp2] = "https://shaaan.github.io/pcipd/syllabus3/tp2"
        urlMap[R.id.pharmac] = "https://shaaan.github.io/pcipd/syllabus3/pharmac"
        urlMap[R.id.formulation] = "https://shaaan.github.io/pcipd/syllabus3/formulation"
        urlMap[R.id.analysis] = "https://shaaan.github.io/pcipd/syllabus3/analysis"
        urlMap[R.id.juris] = "https://shaaan.github.io/pcipd/syllabus3/juris"
        urlMap[R.id.mchem] = "https://shaaan.github.io/pcipd/syllabus3/mchem"

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
