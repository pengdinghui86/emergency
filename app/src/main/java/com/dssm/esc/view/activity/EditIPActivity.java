package com.dssm.esc.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.view.widget.DeleteEditText;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.utils.SpUtil;

/**
 * 设置服务器地址
 */
public class EditIPActivity extends com.easemob.chatuidemo.activity.BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ip);
        ((TextView) findViewById(R.id.tv_actionbar_title)).setText("设置BCM服务器");
        ImageView backImg = (ImageView) findViewById(R.id.iv_actionbar_back);
        backImg.setVisibility(View.VISIBLE);
        backImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        final DeleteEditText editText = (DeleteEditText) findViewById(R.id.edit_ip);
        String url = SpUtil.getSpUtil("address", MODE_PRIVATE).getSPValue("address", DemoApplication.getInstance().getUrl());
        editText.setText(url.replace("http://", "").replace("https://", "").replace("/app/", ""));
        String address = editText.getText().toString().trim();
        editText.setSelection(address.length());//将光标移至文字末尾
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.raido_gp);
        RadioButton https_rb = (RadioButton) findViewById(R.id.https_rb);
        if (url.contains("https://")) {
            https_rb.setChecked(true);
        }
        findViewById(R.id.sure_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkId = radioGroup.getCheckedRadioButtonId();
                String address = editText.getText().toString().trim();
                SpUtil.getSpUtil("address", MODE_PRIVATE).putSPValue("address", address);
                if (checkId == R.id.http_rb) {
                    DemoApplication.getInstance().setUrl("http://" + address + "/app/");
                    SpUtil.getSpUtil("address", MODE_PRIVATE).putSPValue("url", "http://" + address + "/app/");
                } else {
                    DemoApplication.getInstance().setUrl("https://" + address + "/app/");
                    SpUtil.getSpUtil("address", MODE_PRIVATE).putSPValue("url", "https://" + address + "/app/");
                }
                setResult(RESULT_OK);
                finish();

            }
        });
    }
}
