package com.example.bighome.mvp.main.neighbor;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bighome.R;
import com.example.bighome.RxBus.RxBus;
import com.example.bighome.data.Message;
import com.example.bighome.util.StatuUtil;
import com.example.bighome.util.Utils;
import com.example.bighome.websocket.WsConfig;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.inputMsg)
    EditText inputMsg;
    @BindView(R.id.list_view_messages)
    ListView listViewMessages;

    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<Message> listMessages;
    WebSocketClient client;

    private Utils utils;
    private URI uri;

    // Client name
    private String name = null;

    // JSON flags to identify the kind of JSON response
    private static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_MESSAGE = "message", TAG_EXIT = "exit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        StatuUtil.setDarkStatusIcon(true,getWindow());
        ButterKnife.bind(this);

        utils = new Utils(this);

        // 从上一个屏幕获取姓名
        Intent i = getIntent();
        name = i.getStringExtra("name");

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("聊天",name);
                Log.d("聊天",inputMsg.getText().toString());
                // Sending message to web socket server
                sendMessageToServer(utils.getSendMessageJSON(inputMsg.getText().toString()));

                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }
        });
        listMessages = new ArrayList<Message>();

        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);

        initSockect();
    }

    public void initSockect() {
        try {
            String address=WsConfig.URL_WEBSOCKET+name;
            uri = new URI(address);
            Log.d("聊天链接",uri+"丨"+address);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (null == client) {
            client = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {

                }
                @Override
                public void onMessage(String s) {
                    parseMessage(s);
                }

                @Override
                public void onMessage(ByteBuffer bytes) {
                    byte[] data = new byte[bytes.remaining()];
                    // Message will be in JSON format
                    parseMessage(bytesToHex(data));
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    String message = String.format(Locale.US,
                            "Disconnected! Code: %d Reason: %s", i, s);
                    showToast(message);
                    // clear the session id from shared preferences
                    utils.storeSessionId(null);
                }
                @Override
                public void onError(Exception e) {
                    showToast("Error! : " + e);
                }
            };
            client.connect();
        }
    }

    /**
     * 发送消息
     * */
    private void sendMessageToServer(String message) {
        if (client != null && client.isOpen()) {
            Log.d("聊天",message);
            client.send(message);
        }
    }

    /**
     * 解析从服务端收到的json 消息的目的由flag字段所指定，flag=self，消息属于指定的人，
     * new：新人加入   *    到对话中，message：新的消息，exit：退出
     *
     *
     *
     *
     * */
    private void parseMessage(final String msg) {

        try {
            JSONObject jObj = new JSONObject(msg);

            // JSON node 'flag'
            String flag = jObj.getString("flag");

            // 如果是self，json中包含sessionId信息
            if (flag.equalsIgnoreCase(TAG_SELF)) {

                String sessionId = jObj.getString("sessionId");

                // Save the session id in shared preferences
                utils.storeSessionId(sessionId);


            } else if (flag.equalsIgnoreCase(TAG_NEW)) {
                // If the flag is 'new', new person joined the room
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                // number of people online
                String onlineCount = jObj.getString("onlineCount");

                showToast(name + message + ". Currently " + onlineCount
                        + " people online!");

            } else if (flag.equalsIgnoreCase(TAG_MESSAGE)) {
                // if the flag is 'message', new message received
                String fromName = name;
                String message = jObj.getString("message");
                String sessionId = jObj.getString("sessionId");
                boolean isSelf = true;

                // Checking if the message was sent by you
                if (!sessionId.equals(utils.getSessionId())) {
                    fromName = jObj.getString("name");
                    isSelf = false;
                }

                Message m = new Message(fromName, message, isSelf);

                // 把消息加入到arraylist中
                appendMessage(m);

            } else if (flag.equalsIgnoreCase(TAG_EXIT)) {
                // If the flag is 'exit', somebody left the conversation
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                showToast(name + message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(client != null & client.isOpen()){
            client.close();
        }
    }

    /**
     * 把消息放到listView里
     * */
    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);
                adapter.notifyDataSetChanged();

                // Playing device's notification
                playBeep();
            }
        });
    }

    private void showToast(final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ChatActivity.this, message,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * 播放默认的通知声音
     * */
    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(this,
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
