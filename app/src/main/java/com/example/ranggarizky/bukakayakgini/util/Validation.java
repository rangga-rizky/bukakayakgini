package com.example.ranggarizky.bukakayakgini.util;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.Toast;

import com.xwray.passwordview.PasswordView;

/**
 * Created by ranggarizky on 7/17/2016.
 */
public class Validation {

    public static Boolean checkEmpty(EditText editText){
        if(editText.getText().length() == 0){
            editText.setError("Harap diiisi");
            return false;
        }
        return true;
    }
    public static Boolean checkEmpty(TextInputLayout layout, EditText editText){
        if(editText.getText().length() == 0){
            layout.setError("Harap diiisi");
            return false;
        }
        return true;
    }




    public static Boolean checkPassword(Context context, PasswordView editText){
        if(editText.getText().length() <= 4){
            Toast.makeText(context,"Password Terlalu pendek", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
