package `in`.shaaan.pcipharmd

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import `in`.shaaan.pcipharmd.databinding.ActivitySecondYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentSecondYearBinding

/**
 * Activity for the Second Year syllabus.
 * What: Displays subjects for the second year.
 * Why: Provides a dedicated screen for second-year content.
 * How: Extends `BaseYearActivity` and provides the necessary bindings and data maps. The base class handles all logic.
 */
class SecondYear : BaseYearActivity<ActivitySecondYearBinding, ContentSecondYearBinding>(
    ActivitySecondYearBinding::inflate
) {
    companion object {
        private const val COLOGY_PATH = "second_year/cology"
        private const val TP1_PATH = "second_year/tp1"
        private const val MICRO_PATH = "second_year/micro"
        private const val COGNOSY_PATH = "second_year/cognosy"
        private const val PATHO_PATH = "second_year/patho"
        private const val COM_PATH = "second_year/cmp"
    }

    override fun getContentBinding(activityBinding: ActivitySecondYearBinding) =
        activityBinding.layout2y

    override fun getToolbar(activityBinding: ActivitySecondYearBinding): Toolbar =
        activityBinding.toolbar

    override fun getFab(activityBinding: ActivitySecondYearBinding): FloatingActionButton =
        activityBinding.fab

    override val subjectButtonIdToEndpointMap: Map<Int, String> = mapOf(
        R.id.cology to COLOGY_PATH,
        R.id.tp1 to TP1_PATH,
        R.id.micro to MICRO_PATH,
        R.id.cognosy to COGNOSY_PATH,
        R.id.patho to PATHO_PATH,
        R.id.com to COM_PATH
    )

    override fun getNativeAdPlaceholderPairs(contentBinding: ContentSecondYearBinding): List<Pair<View, View>> {
        return listOf(
            Pair(contentBinding.nativeAd21, contentBinding.nativeCard21),
            Pair(contentBinding.nativeAd22, contentBinding.nativeCard22),
            Pair(contentBinding.nativeAd23, contentBinding.nativeCard23)
        )
    }

    override fun getSubjectButtons(contentBinding: ContentSecondYearBinding): List<View> {
        return listOf(
            contentBinding.cology, contentBinding.tp1, contentBinding.micro,
            contentBinding.cognosy, contentBinding.patho, contentBinding.com
        )
    }
}
