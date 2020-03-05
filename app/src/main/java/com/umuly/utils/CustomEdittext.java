package com.umuly.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.ArrayList;


@SuppressLint("AppCompatCustomView")
public class CustomEdittext extends EditText {
    ArrayList<CopyListener> listeners;

    public CustomEdittext(Context context) {
        super(context);
        listeners = new ArrayList<>();
    }

    public CustomEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        listeners = new ArrayList<>();
    }

    public CustomEdittext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        listeners = new ArrayList<>();
    }

    public void addListener(CopyListener listener) {
        try {
            listeners.add(listener);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean consumed = super.onTextContextMenuItem(id);
        switch (id) {
            case android.R.id.cut:
                onTextCut();
                break;
            case android.R.id.paste:
                onTextPaste();
                break;
            case android.R.id.copy:
                onTextCopy();
        }
        return consumed;
    }

    public void onTextCut() {
    }

    public void onTextCopy() {
    }


    public void onTextPaste() {
        for (CopyListener listener : listeners) {
            listener.onUpdate();
        }
    }
}
