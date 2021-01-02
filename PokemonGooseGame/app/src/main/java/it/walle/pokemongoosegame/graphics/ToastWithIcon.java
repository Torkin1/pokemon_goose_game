package it.walle.pokemongoosegame.graphics;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import it.walle.pokemongoosegame.R;

public class ToastWithIcon extends Toast {

    private final ImageView ivIcon;
    private final TextView tvMsg;
    private static ToastWithIcon lastToast;
    private boolean isCancelable = true;

    public ToastWithIcon(Activity activity, Drawable icon, String msg) {
        super(activity);
        View toastContent = activity.getLayoutInflater().inflate(R.layout.toast_with_icon, activity.findViewById(R.id.llToastLayout));
        this.tvMsg = toastContent.findViewById(R.id.tvMsg);

        int nightModeFlags = activity.getApplicationContext().
                getResources().
                getConfiguration().
                uiMode & Configuration.UI_MODE_NIGHT_MASK;
//TODO Add more personalization for the other cases
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                this.tvMsg.setTextColor(0xff0000ff);
                break;
        }
        this.ivIcon = toastContent.findViewById(R.id.ivIcon);
        tvMsg.setText(msg);
        ivIcon.setImageDrawable(icon);
        this.setView(toastContent);
    }

    public ToastWithIcon(Activity activity, Drawable icon, String msg, int duration){
        this(activity, icon, msg);
        this.setDuration(duration);
    }

    public ToastWithIcon(Activity activity, Drawable icon, String msg, int duration, boolean isCancelable){
        this(activity, icon, msg, duration);
        this.isCancelable = false;
    }

    @Override
    public void show() {

        if (lastToast != null && lastToast.isCancelable){
            lastToast.cancel();
        }
        super.show();
        lastToast = this;
    }
}
