package `in`.shaaan.pcipharmd

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import `in`.shaaan.pcipharmd.databinding.ActivityFirstYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentFirstYearBinding

/**
 * Activity for the First Year syllabus.
 * What: Displays subjects for the first year and handles navigation to their details.
 * Why: Provides a dedicated screen for first-year content.
 * How: Extends `BaseYearActivity` to inherit common functionalities.
 */
class FirstYear : BaseYearActivity<ActivityFirstYearBinding, ContentFirstYearBinding>(
    ActivityFirstYearBinding::inflate
) {
    companion object {
        private const val HAP_PATH = "first_year/hap/"
        private const val CEUTICS_PATH = "first_year/pahrmaceutics/"
        private const val BIOCHEM_PATH = "first_year/biochem/"
        private const val IC_PATH = "first_year/ic/"
        private const val OC_PATH = "first_year/oc/"
        private const val REM_MATH_BIO_PATH = "first_year/math_bio/"
    }

    override fun getContentBinding(activityBinding: ActivityFirstYearBinding) =
        activityBinding.layout1y

    override fun getToolbar(activityBinding: ActivityFirstYearBinding): Toolbar =
        activityBinding.toolbar

    override fun getFab(activityBinding: ActivityFirstYearBinding): FloatingActionButton =
        activityBinding.fab

    override val subjectButtonIdToEndpointMap: Map<Int, String> = mapOf(
        R.id.hap to HAP_PATH,
        R.id.ceutics to CEUTICS_PATH,
        R.id.biochem to BIOCHEM_PATH,
        R.id.ic to IC_PATH,
        R.id.oc to OC_PATH,
        R.id.rem_mathBio to REM_MATH_BIO_PATH
    )

    override fun getNativeAdPlaceholderPairs(contentBinding: ContentFirstYearBinding): List<Pair<View, View>> {
        return listOf(
            Pair(contentBinding.nativeAd11, contentBinding.nativeCard11),
            Pair(contentBinding.nativeAd12, contentBinding.nativeCard12),
            Pair(contentBinding.nativeAd13, contentBinding.nativeCard13)
        )
    }

    override fun getSubjectButtons(contentBinding: ContentFirstYearBinding): List<View> {
        return listOf(
            contentBinding.hap, contentBinding.ceutics, contentBinding.biochem,
            contentBinding.oc, contentBinding.ic, contentBinding.remMathBio
        )
    }
}
