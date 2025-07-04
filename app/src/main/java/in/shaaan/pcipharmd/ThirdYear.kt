package `in`.shaaan.pcipharmd

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.ads.nativetemplates.TemplateView
import `in`.shaaan.pcipharmd.databinding.ActivityThirdYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentThirdYearBinding

/**
 * Activity for the Third Year syllabus.
 * What: Displays subjects for the third year and handles navigation to their details.
 * Why: Provides a dedicated screen for third-year content.
 * How: Extends `BaseYearActivity` to inherit common functionalities.
 */
class ThirdYear : BaseYearActivity<ActivityThirdYearBinding, ContentThirdYearBinding>(
    ActivityThirdYearBinding::inflate
) {
    companion object {
        private const val TP2_PATH = "third_year/tp2"
        private const val PHARMAC_PATH = "third_year/pharmac"
        private const val FORMULATION_PATH = "third_year/formulation"
        private const val ANALYSIS_PATH = "third_year/analysis"
        private const val JURIS_PATH = "third_year/juris"
        private const val MCHEM_PATH = "third_year/mchem"
    }

    override fun getContentBinding(activityBinding: ActivityThirdYearBinding) = activityBinding.layout3y
    override fun getToolbar(activityBinding: ActivityThirdYearBinding): Toolbar = activityBinding.toolbar
    override fun getFab(activityBinding: ActivityThirdYearBinding): FloatingActionButton = activityBinding.fab

    override val subjectButtonIdToEndpointMap: Map<Int, String> = mapOf(
        R.id.tp2 to TP2_PATH,
        R.id.pharmac to PHARMAC_PATH,
        R.id.formulation to FORMULATION_PATH,
        R.id.analysis to ANALYSIS_PATH,
        R.id.juris to JURIS_PATH,
        R.id.mchem to MCHEM_PATH
    )

    override fun getNativeAdPlaceholderPairs(contentBinding: ContentThirdYearBinding): List<Pair<View, View>> {
        return listOf(
            Pair(contentBinding.nativeAd31, contentBinding.nativeCard31),
            Pair(contentBinding.nativeAd32, contentBinding.nativeCard32),
            Pair(contentBinding.nativeAd33, contentBinding.nativeCard33)
        )
    }

    override fun getSubjectButtons(contentBinding: ContentThirdYearBinding): List<View> {
        return listOf(
            contentBinding.tp2, contentBinding.pharmac, contentBinding.formulation,
            contentBinding.analysis, contentBinding.juris, contentBinding.mchem
        )
    }
}
