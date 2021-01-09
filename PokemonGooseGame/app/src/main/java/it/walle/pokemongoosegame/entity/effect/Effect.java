package it.walle.pokemongoosegame.entity.effect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.graphics.DialogManager;

public abstract class Effect {

    private String title;                                           // The readable name of the effect
    private String description;                                     // A textual description of the effect
    private int effect_image_dialogID;


    public abstract void doEffect(InvocationContext invocationContext);       // Implementation of the actual effect

    //Overloading del metodo generalDialog per creare dialoghi cancellabili e senza pulsanti positivi e negativi
    protected Dialog generalDialog(InvocationContext invocationContext){
        return this
                .generalDialog(
                        invocationContext,
                        null,
                        null,
                        null,
                        null,
                        true);
    }

    //Overloading del metodo generalDialog per creare dialoghi non cancellabili e con pulsanti positivi e negativi
    protected Dialog generalDialog(InvocationContext invocationContext,
                                   String positiveButtonText,
                                   DialogInterface.OnClickListener positiveButton,
                                   String negativeButtonText,
                                   DialogInterface.OnClickListener negativeButton){
       return this
               .generalDialog(
                       invocationContext,
                       positiveButtonText,
                       positiveButton,
                       negativeButtonText,
                       negativeButton,
                       false);
    }

    protected Dialog generalDialog(InvocationContext invocationContext,
                                   String positiveButtonText,
                                   DialogInterface.OnClickListener positiveButton,
                                   String negativeButtonText,
                                   DialogInterface.OnClickListener negativeButton,
                                   boolean isCancelable) {
        Context context;
        context = invocationContext.getContext();

        LayoutInflater dialog_layout_inflater = LayoutInflater.from(context);

        LinearLayout dialog_layout = (LinearLayout) dialog_layout_inflater.inflate(R.layout.general_dialog, null);
        TextView effect_description_dialog = dialog_layout.findViewById(R.id.dialog_description_text);
        TextView effect_title_dialog = dialog_layout.findViewById(R.id.dialog_title_text);
        ImageView effect_image_dialog = dialog_layout.findViewById(R.id.dialog_image);


        effect_description_dialog.setText(description);
        effect_title_dialog.setText(title);
        effect_image_dialog.setImageDrawable(ContextCompat.getDrawable(context, effect_image_dialogID));

        Dialog dialog;

        if(positiveButton == null && negativeButton == null){
            dialog = new AlertDialog.Builder(context)
                    .setView(dialog_layout)
                    .create();
        }
        else{
            dialog = new AlertDialog.Builder(context)
                    .setView(dialog_layout)
                    .setPositiveButton(positiveButtonText, positiveButton)
                    .setNegativeButton(negativeButtonText, negativeButton)
                    .create();

            dialog.setCancelable(isCancelable);
        }

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setGravity(Gravity.CENTER);

        return dialog;
    }

    protected void showDialog(Dialog dialog){
        DialogManager.getInstance().enqueueDialog(dialog);
    }

    protected void showDialog(Dialog dialog, DialogInterface.OnDismissListener onDismissListener){
        DialogManager.getInstance().enqueueDialog(dialog, onDismissListener);
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.getDescription();
    }

    public String getTitle() {
        return title;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    protected void setEffect_image_dialogID(int effect_image_dialogID){
        this.effect_image_dialogID =effect_image_dialogID;
    }

    public void DialogBuilder(){}
}