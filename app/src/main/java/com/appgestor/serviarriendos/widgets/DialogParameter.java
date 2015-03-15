package com.appgestor.serviarriendos.widgets;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.appgestor.serviarriendos.R;
import com.appgestor.serviarriendos.views.ButtonFlat;

/**
 * Created by Poo_Code on 08/12/2014.
 */
public class DialogParameter extends android.app.Dialog {

    String message;
    TextView messageTextView;
    String title;
    TextView titleTextView;
    EditText ediNombre, edEmail, edTelefono;
    ButtonFlat buttonAccept;
    ButtonFlat buttonCancel;


    View.OnClickListener onAcceptButtonClickListener;
    View.OnClickListener onCancelButtonClickListener;

    public DialogParameter(Context context,String title, String message) {
        super(context, android.R.style.Theme_Translucent);
        this.message = message;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_parameter);

        this.titleTextView = (TextView) findViewById(R.id.title);
        setTitle(title);

        this.ediNombre = (EditText) findViewById(R.id.ediNombre);
        this.edEmail = (EditText) findViewById(R.id.edEmail);
        this.edTelefono = (EditText) findViewById(R.id.edTelefono);

        //this.messageTextView = (TextView) findViewById(R.id.message);
        //setMessage(message);

        this.buttonAccept = (ButtonFlat) findViewById(R.id.button_accept);
        buttonAccept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if(onAcceptButtonClickListener != null)
                    onAcceptButtonClickListener.onClick(v);
            }
        });

        this.buttonCancel = (ButtonFlat) findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if(onCancelButtonClickListener != null)
                    onCancelButtonClickListener.onClick(v);
            }
        });
    }

    // GETERS & SETTERS
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        messageTextView.setText(message);
    }

    public TextView getMessageTextView() {
        return messageTextView;
    }

    public void setMessageTextView(TextView messageTextView) {
        this.messageTextView = messageTextView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if(title == null)
            titleTextView.setVisibility(View.GONE);
        else{
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        }
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public EditText getNombre() {
        return ediNombre;
    }
    public EditText getEmail() {
        return edEmail;
    }
    public EditText getTelefono() {
        return edTelefono;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public ButtonFlat getButtonAccept() {
        return buttonAccept;
    }

    public void setButtonAccept(ButtonFlat buttonAccept) {
        this.buttonAccept = buttonAccept;
    }

    public ButtonFlat getButtonCancel() {
        return buttonCancel;
    }

    public void setButtonCancel(ButtonFlat buttonCancel) {
        this.buttonCancel = buttonCancel;
    }

    public void setOnAcceptButtonClickListener(
            View.OnClickListener onAcceptButtonClickListener) {
        this.onAcceptButtonClickListener = onAcceptButtonClickListener;
        if(buttonAccept != null)
            buttonAccept.setOnClickListener(onAcceptButtonClickListener);
    }

    public void setOnCancelButtonClickListener(
            View.OnClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        if(buttonCancel != null)
            buttonCancel.setOnClickListener(onAcceptButtonClickListener);
    }



}
