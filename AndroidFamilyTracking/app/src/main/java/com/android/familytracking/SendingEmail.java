package com.android.familytracking;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by PC13 on 8/21/2017.
 */

public class SendingEmail extends AsyncTask<String, Void, String> {

    private String id, email;

    public SendingEmail(String id, String email) {
        this.id = id;
        this.email = email;
    }

    @Override
    protected String doInBackground(String... strings) {
        SendEmail sender = new SendEmail("famtrack2017@gmail.com", "wisuda2018");
        try {
            Log.e("SendMail", "Trying sending");
            sender.sendMail("Family Tracking - Group Code",
                    "Your Code : " + id,
                    "famtrack2017@gmail.com", email);
            Log.e("SendMail", "Finish sending");
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
        return null;
    }
}
