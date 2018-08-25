package com.dssm.esc.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.util.Const;
import com.dssm.esc.util.HttpUrl;
import com.dssm.esc.util.Utils;
import com.easemob.chatuidemo.DemoApplication;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 操作手册界面（webview）
 *
 * @author Zsj
 * @Description TODO
 * @date 2015-9-14
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co.,
 * Ltd. Inc. All rights reserved.
 */
@ContentView(R.layout.activity_operationmenu)
public class OperationMenuActivity extends BaseActivity {
    /** 1，应急；2，演练 */
    // private String tag;
    /**
     * 标题
     */
    @ViewInject(R.id.tv_actionbar_title)
    private TextView title;
    /**
     * 返回按钮
     */
    @ViewInject(R.id.iv_actionbar_back)
    private ImageView back;
    /**
     * WebView
     */
    @ViewInject(R.id.webView)
    private WebView webView;
    // manualDetailId 操作手册详细内容记录id
    // planResType 演练计划类型 1、事件 2、演练计划
    // drillPrecautionId 演练计划id
    // name 步骤名称
    private String manualDetailId = "";
    private String planResType = "";
    private String drillPrecautionId = "";
    private String name = "";
    private String planInfoId = "";
    private String url;
    /**
     * 1.预案列表2，预案步骤
     */
    private String tag;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_operationmenu);
        View findViewById = findViewById(R.id.operationmenu);
        findViewById.setFitsSystemWindows(true);
        intent = getIntent();
        tag = intent.getStringExtra("tag");
        planResType = intent.getStringExtra("planResType");
        drillPrecautionId = intent.getStringExtra("drillPrecautionId");
        name = intent.getStringExtra("name");
        Utils.getInstance().showProgressDialog(
                OperationMenuActivity.this, "", Const.SUBMIT_MESSAGE);
        initView();
    }

    private void initView() {
        back.setVisibility(View.VISIBLE);
        if("".equals(name) || name == null)
            title.setText("操作手册");
        else
            title.setText(name);
        if (tag.equals("1")) {
            // planResType 预案来源类型 1、事件 2、演练计划
            // drillPrecautionId 演练预案id
            // planInfoId 执行预案ID
            planInfoId = intent.getStringExtra("planInfoId");
            url = DemoApplication.getInstance().getUrl() + HttpUrl.SHOWALLOPENMANUAL + "?planResType=" + planResType
                    + "&drillPrecautionId=" + drillPrecautionId
                    + "&planInfoId=" + planInfoId;
        } else if (tag.equals("2")) {
            manualDetailId = intent.getStringExtra("manualDetailId");
            name = intent.getStringExtra("name");
            url = DemoApplication.getInstance().getUrl() + HttpUrl.SHOWOPENMANUAL + "?manualDetailId=" + manualDetailId
                    + "&planResType=" + planResType + "&drillPrecautionId="
                    + drillPrecautionId + "&name=" + name;
        }
        WebSettings webSettings = webView.getSettings();
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        Log.i("操作手册url", url);
        webView.loadUrl(url);

        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Utils.getInstance().hideProgressDialog();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    // private class LoadWebViewTask extends AsyncTask<Void, Void, Void> {
    //
    // CookieManager cookieManager;
    //
    //
    // @Override
    // protected void onPreExecute() {
    // CookieSyncManager.createInstance(OperationMenuActivity.this);
    //
    // cookieManager = CookieManager.getInstance();
    // cookieManager.setAcceptCookie(true);
    // cookieManager.removeSessionCookie(); //移除上一次的session
    // }
    //
    //
    // @Override
    //
    // protected Void doInBackground(Void... params) {
    //
    // try {
    // new Thread().sleep(1500);
    // } catch (InterruptedException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } //等待cookieManager处理完毕
    //
    // return null;
    //
    // }
    //
    //
    // @Override
    // protected void onPostExecute(Void result) {
    // cookieManager.setCookie("test.com", "ASP.NET_SessionId=" +
    //
    // businessLogicManager.getSessionId()); //设置cookie
    //
    // CookieSyncManager.getInstance().sync(); //同步
    // webView.loadUrl("http://test.com");
    //
    // }
    //
    // }
}
