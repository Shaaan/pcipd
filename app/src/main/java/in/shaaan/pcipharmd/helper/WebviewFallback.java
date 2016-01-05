package in.shaaan.pcipharmd.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by shaaan on 5/1/16 for in.shaaan.pcipharmd.helper
 */
public class WebviewFallback implements CustomTabActivityHelper.CustomTabFallback {
    @Override
    public void openUri(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, WebviewActivity.class);
        intent.putExtra(WebviewActivity.EXTRA_URL, uri.toString());
        activity.startActivity(intent);
    }
}
