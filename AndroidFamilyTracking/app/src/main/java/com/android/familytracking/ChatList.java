package com.android.familytracking;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PC13 on 8/3/2017.
 */

public class ChatList extends ArrayAdapter<Chat> {
    private Activity context;
    private List<Chat> ChatList;

    public ChatList(Activity context, List<Chat> ChatList){
        super(context, R.layout.chat_layout, ChatList);
        this.context = context;
        this.ChatList = ChatList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.chat_layout, null, true);

        TextView messageText = (TextView)listViewItem.findViewById(R.id.message_text);
        TextView messageUser = (TextView)listViewItem.findViewById(R.id.message_user);
        TextView messageTime = (TextView)listViewItem.findViewById(R.id.message_time);

        Chat chat = ChatList.get(position);

        // Set their text
        messageText.setText(chat.getMessageText());
        messageUser.setText(chat.getMessageUser());

        // Format the date before showing it
        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                chat.getMessageTime()));

        return listViewItem;
    }
}
