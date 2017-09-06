package com.thatzit.oib.stamptourboard.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.thatzit.oib.stamptourboard.R;

/**
 * Created by kjw on 16. 9. 18..
 */
public class ProgressWaitDialog extends Dialog {
    private final Context context;
    private ProgressBar waitAnimation;
    private AnimationDrawable aniFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        lpWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lpWindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.comm_progress);
        setProgressWait();
    }

    public void setProgressWait() {
        waitAnimation = (ProgressBar)findViewById(R.id.wait_progress);
        waitAnimation.setVisibility(View.VISIBLE);
        waitAnimation.animate().alpha(1);
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    @Override
    public void dismiss() {
        waitAnimation.setVisibility(View.GONE);
        super.dismiss();
    }

    public ProgressWaitDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
    }

}
