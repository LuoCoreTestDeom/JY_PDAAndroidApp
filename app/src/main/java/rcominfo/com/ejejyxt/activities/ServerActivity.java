package rcominfo.com.ejejyxt.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rcominfo.tmjy.R;

import rcominfo.com.ejejyxt.Utils.ShareUtil;

public class ServerActivity extends BaseActivity {

    private EditText server_IP;
    private EditText server_Port;
    private EditText new_server_IP;
    private EditText new_server_Port;
    private Button btn_save;
    private ServerActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        mContext= this;
        initView();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.getInstance(mContext).server(server_IP.getText().toString(),server_Port.getText().toString());
                ShareUtil.getInstance(mContext).new_server(new_server_IP.getText().toString(),new_server_Port.getText().toString());
                finish();
            }
        });
    }

    private void initView() {
        server_IP = (EditText)findViewById(R.id.server_IP);
        server_Port = (EditText)findViewById(R.id.server_Port);
        new_server_IP = (EditText)findViewById(R.id.new_server_IP);
        new_server_Port = (EditText)findViewById(R.id.new_server_Port);
        server_IP.setText(ShareUtil.getInstance(mContext).getServerIP());
        server_Port.setText(ShareUtil.getInstance(mContext).getServerPort());
        new_server_IP.setText(ShareUtil.getInstance(mContext).getnewServerIP());
        new_server_Port.setText(ShareUtil.getInstance(mContext).getnewServerPort());
        btn_save = (Button)findViewById(R.id.btn_save);
    }
}
