package com.example.matchit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.Message;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;
import com.scaledrone.lib.Listener;

import java.util.Random;

public class MessageBoard extends AppCompatActivity implements RoomListener {
    private String channelID = "gv19u95S3WlT5ZV6";
    private String roomName = "observable-room";
    private EditText editText;
    private MessageAdapter msdAdapter;
    private ListView messagesListView;

    private Scaledrone scaledrone;
    private Button buttonMessage;

    protected void onCreate(Bundle savedInstanceState) {
        Log.v("my message", "Hiii how are you");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);
        editText = (EditText) findViewById(R.id.editTextForMessage);
        buttonMessage = (Button) findViewById(R.id.messageClickButton);
        msdAdapter = new MessageAdapter(this);
        messagesListView = (ListView) findViewById(R.id.messages_view);
        messagesListView.setAdapter(msdAdapter);
        Log.d("my message", "Hiii how are you");
        MemberData data = new MemberData("himanshu", "blue");
        scaledrone = new Scaledrone(channelID, data);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                System.out.println("Scaledrone connection open");
                scaledrone.subscribe(roomName, MessageBoard.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onClosed(String reason) {
                System.err.println(reason);
            }
        });
        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                Log.d("my message", "message = " + message);
                if (message.length() > 0) {
                    scaledrone.publish("observable-room", message);
                    editText.getText().clear();
                }
            }
        });
        Log.v("my message", "Hiiiiii");
    }
    @Override
    public void onOpenFailure(Room room, Exception ex) {

    }

    @Override
    public void onOpen(Room room) {

    }

    @Override
    public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage) {
        ObjectMapper mapperObject = new ObjectMapper();
        MemberData data = null;
        try {
            data = mapperObject.treeToValue(receivedMessage.getMember().getClientData(), MemberData.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        boolean belongsToCurrentUser = receivedMessage.getClientID().equals(scaledrone.getClientID());
        if (data != null) {
            final Messages messageReceived = new Messages(receivedMessage.getData().asText(), data, belongsToCurrentUser);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msdAdapter.add(messageReceived);
                    messagesListView.setSelection(messagesListView.getCount() - 1);
                }
            });
        }
    }
}
