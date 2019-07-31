package com.example.shan.mqtt;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    TextView textView;
    Integer currentIndex = 0;
    private ErrorLog errorLog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        errorLog = ErrorLog.getInstance(getApplicationContext());
        currentIndex = errorLog.getLogCount() -1;
        textView = (TextView) findViewById(R.id.text_history);
        textView.setText(errorLog.getLog(currentIndex));
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        Button btn = (Button) this.findViewById(R.id.last_page);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex > 0) {
                    currentIndex--;
                    textView.setText(errorLog.getLog(currentIndex));
                    textView.scrollTo(0,0);
                }
                System.out.println("last");
            }
        });
        btn = (Button) this.findViewById(R.id.next_page);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex < errorLog.getLogCount() -1) {
                    currentIndex++;
                    textView.setText(errorLog.getLog(currentIndex));
                } //else {
//                    errorLog.setFileList();
//                    textView.scrollTo(0,0);
//                }
//                System.out.println("next");
            }
        });
//        System.out.println("history activity create");
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
                Intent intent = new Intent(HistoryActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.navigation_history:
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
