package `in`.shaaan.pcipharmd

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import `in`.shaaan.pcipharmd.databinding.ActivityFourthYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentFourthYearBinding

/**
 * Activity for the Fourth Year syllabus.
 * What: Displays subjects for the fourth year and handles navigation to their details.
 * Why: Provides a dedicated screen for fourth-year content.
 * How: Extends `BaseYearActivity` to inherit common functionalities.
 */
class FourthYear : BaseYearActivity<ActivityFourthYearBinding, ContentFourthYearBinding>(
    ActivityFourthYearBinding::inflate
) {
    companion object {
        private const val TP3_PATH = "fourth_year/pt3"
        private const val TOXICOLOGY_PATH = "fourth_year/toxicology"
        private const val CP_PATH = "fourth_year/cp"
        private const val HP_PATH = "fourth_year/hosp_pharm"
        private const val BIOPHARM_PATH = "fourth_year/biopharm"
        private const val BIOSTAT_PATH = "fourth_year/biostat"
    }

    override fun getContentBinding(activityBinding: ActivityFourthYearBinding) =
        activityBinding.layout4y

    override fun getToolbar(activityBinding: ActivityFourthYearBinding): Toolbar =
        activityBinding.toolbar

    override fun getFab(activityBinding: ActivityFourthYearBinding): FloatingActionButton =
        activityBinding.fab

    override val subjectButtonIdToEndpointMap: Map<Int, String> = mapOf(
        R.id.tp3 to TP3_PATH,
        R.id.toxicology to TOXICOLOGY_PATH,
        R.id.cp to CP_PATH,
        R.id.hp to HP_PATH,
        R.id.biopharm to BIOPHARM_PATH,
        R.id.biostat to BIOSTAT_PATH
    )

    override fun getNativeAdPlaceholderPairs(contentBinding: ContentFourthYearBinding): List<Pair<View, View>> {
        return listOf(
            Pair(contentBinding.nativeAd41, contentBinding.nativeCard41),
            Pair(contentBinding.nativeAd42, contentBinding.nativeCard42),
            Pair(contentBinding.nativeAd43, contentBinding.nativeCard43)
        )
    }

    override fun getSubjectButtons(contentBinding: ContentFourthYearBinding): List<View> {
        return listOf(
            contentBinding.tp3, contentBinding.toxicology, contentBinding.hp,
            contentBinding.cp, contentBinding.biopharm, contentBinding.biostat
        )
    }
}
