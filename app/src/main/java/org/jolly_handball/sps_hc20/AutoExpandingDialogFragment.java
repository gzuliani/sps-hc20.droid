package org.jolly_handball.sps_hc20;

import android.app.Dialog;
import android.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

public class AutoExpandingDialogFragment extends DialogFragment {
    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float scaleFactor = metrics.density;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);

        if (smallestWidth > 720) {
            ; // 10" tablet -- do nothing
        } else if (smallestWidth > 600) {
            ; // 7" tablet -- do nothing
        } else {
            // smartphone
            Dialog dialog = getDialog();
            if (dialog != null) {
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
        }
    }
}
