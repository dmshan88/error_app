package com.example.shan.mqtt;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ErrorLog errorLog = null;
//    private boolean isFirstStart;
    private  long lastModifyTime = -1;
    TextView mainTextView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if ( errorLog.getLastModiiedTime() > lastModifyTime) {
                    String[] lineArray = errorLog.getNewLog().split("\n");
                    if (lineArray.length > 0 ) {
                        mainTextView.setText("");
                        lastModifyTime = errorLog.getLastModiiedTime();
                    }
                    for (int i = lineArray.length -1; i >= 0; i--) {
                        mainTextView.append(lineArray[i] + "\n");
                    }

                }

//                int offset = mainTextView.getLineCount() * mainTextView.getLineHeight();
//                if (offset > mainTextView.getHeight()) {
//                    mainTextView.scrollTo(0, offset - mainTextView.getHeight());
//                }
                sendEmptyMessageDelayed(1,5000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, MyMqttService.class);
        startService(intent);
        errorLog = ErrorLog.getInstance(getApplicationContext());
        mainTextView = (TextView) findViewById(R.id.text_main);
        mainTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        handler.sendEmptyMessage(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_home:
                return true;
            case R.id.navigation_history:
                Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
