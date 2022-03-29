package rcominfo.com.ejejyxt.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.rcominfo.tmjy.R;

import rcominfo.com.ejejyxt.Utils.ScannerReceiver;
import rcominfo.com.ejejyxt.Utils.ToastUtil;

public class TestActivity extends BaseActivity {

    private EditText ed_test;
    private TextView tv_test_result;
    private TestActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mContext = this;

        ed_test = (EditText)findViewById(R.id.ed_test);
        tv_test_result = (TextView)findViewById(R.id.tv_test_result);

        ScannerReceiver.getresult(new ScannerReceiver.doSome() {
            @Override
            public void dosomething(String message) {
                if(ed_test.hasFocus()){
                    ToastUtil.Show(mContext,"扫描API正常");
                    ed_test.setText(message);
                    ed_test.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        });

        ed_test.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==0&&keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    String text = ed_test.getText().toString();
                    tv_test_result.setText(text+"扫描成功");
                }
                return true;
            }
        });
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("key",event+"");
        if(event.getKeyCode()==23&&event.getAction()==KeyEvent.ACTION_DOWN){
            if(ed_test.hasFocus()){
                ed_test.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                return false;
            }
            return false;

        }else if(event.getKeyCode()==23&&event.getAction()==KeyEvent.ACTION_UP){
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
