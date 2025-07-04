package `in`.shaaan.pcipharmd

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.ads.nativetemplates.TemplateView
import `in`.shaaan.pcipharmd.databinding.ActivityFifthYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentFifthYearBinding

/**
 * Activity for the Fifth Year syllabus.
 * What: Displays subjects for the fifth year and handles navigation to their details.
 * Why: Provides a dedicated screen for fifth-year content.
 * How: Extends `BaseYearActivity` to inherit common functionalities.
 */
class FifthYear : BaseYearActivity<ActivityFifthYearBinding, ContentFifthYearBinding>(
    ActivityFifthYearBinding::inflate
) {
    companion object {
        private const val EPI_PATH = "fifth_year/epi"
        private const val RESEARCH_PATH = "fifth_year/research"
        private const val TDM_PATH = "fifth_year/tdm"
    }

    override fun getContentBinding(activityBinding: ActivityFifthYearBinding) = activityBinding.layout5y
    override fun getToolbar(activityBinding: ActivityFifthYearBinding): Toolbar = activityBinding.toolbar
    override fun getFab(activityBinding: ActivityFifthYearBinding): FloatingActionButton = activityBinding.fab

    override val subjectButtonIdToEndpointMap: Map<Int, String> = mapOf(
        R.id.epi to EPI_PATH,
        R.id.research to RESEARCH_PATH,
        R.id.tdm to TDM_PATH
    )

    override fun getNativeAdPlaceholderPairs(contentBinding: ContentFifthYearBinding): List<Pair<View, View>> {
        return listOf(
            Pair(contentBinding.nativeAd51, contentBinding.nativeCard51),
            Pair(contentBinding.nativeAd52, contentBinding.nativeCard52),
            Pair(contentBinding.nativeAd53, contentBinding.nativeCard53)
        )
    }

    override fun getSubjectButtons(contentBinding: ContentFifthYearBinding): List<View> {
        return listOf(
            contentBinding.epi, contentBinding.tdm, contentBinding.research
        )
    }
}
