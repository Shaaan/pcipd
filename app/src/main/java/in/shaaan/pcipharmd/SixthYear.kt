package `in`.shaaan.pcipharmd

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.ads.nativetemplates.TemplateView
import `in`.shaaan.pcipharmd.databinding.ActivitySixthYearBinding
import `in`.shaaan.pcipharmd.databinding.ContentSixthYearBinding

/**
 * Activity for the Sixth Year syllabus.
 * What: Displays subjects for the sixth year and handles navigation to their details.
 * Why: Provides a dedicated screen for sixth-year content.
 * How: Extends `BaseYearActivity` to inherit common functionalities.
 */
class SixthYear : BaseYearActivity<ActivitySixthYearBinding, ContentSixthYearBinding>(
    ActivitySixthYearBinding::inflate
) {
    companion object {
        private const val INTERN_ACTIVITIES_PATH = "sixth_year/"
        private const val INTERN_DOCUMENTS_PATH = "internship/"
    }

    override fun getContentBinding(activityBinding: ActivitySixthYearBinding) = activityBinding.layout6y
    override fun getToolbar(activityBinding: ActivitySixthYearBinding): Toolbar = activityBinding.toolbar
    override fun getFab(activityBinding: ActivitySixthYearBinding): FloatingActionButton = activityBinding.fab

    override val subjectButtonIdToEndpointMap: Map<Int, String> = mapOf(
        R.id.intern_activities to INTERN_ACTIVITIES_PATH,
        R.id.intern_documents to INTERN_DOCUMENTS_PATH
    )

    override fun getNativeAdPlaceholderPairs(contentBinding: ContentSixthYearBinding): List<Pair<View, View>> {
        return listOf(
            Pair(contentBinding.nativeAd61, contentBinding.nativeCard61),
            Pair(contentBinding.nativeAd62, contentBinding.nativeCard62)
        )
    }

    override fun getSubjectButtons(contentBinding: ContentSixthYearBinding): List<View> {
        return listOf(
            contentBinding.internActivities, contentBinding.internDocuments
        )
    }
}
