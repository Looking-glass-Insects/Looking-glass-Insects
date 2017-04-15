package com.example.heyong.myreader.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.heyong.myreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.heyong.myreader.MyApplication.myApplication;

public class WebActivity extends AppCompatActivity {
    public static final String WebView = "WebView";

    @BindView(R.id.web_view)
    android.webkit.WebView webView;

    private WebBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        bean = intent.getParcelableExtra(WebView);
        setupHeader();
        setupContent();
    }

    private void setupContent() {
        //WebView加载web资源
        webView.loadUrl(bean.getUrl());
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    private void setupHeader() {
        myApplication.changeStatusBarColor(this);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(bean.getTitle());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.this.finish();
            }
        });
        if(myApplication.isNight())
            return;
        mToolbar.setBackgroundColor(myApplication.getThemeColor());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web_view,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.copy){
            ClipboardManager myClipboard;
            myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            ClipData myClip;
            myClip = ClipData.newPlainText("url", bean.getUrl());
            myClipboard.setPrimaryClip(myClip);
            Toast.makeText(this, "链接已复制到剪切板上", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.open_browser){
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(bean.getUrl());
            intent.setData(content_url);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    public static class WebBean implements Parcelable {
        String title;
        String url;

        public WebBean(String title, String url) {
            this.title = title;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        protected WebBean(Parcel in) {
            title = in.readString();
            url = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(url);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<WebBean> CREATOR = new Creator<WebBean>() {
            @Override
            public WebBean createFromParcel(Parcel in) {
                return new WebBean(in);
            }

            @Override
            public WebBean[] newArray(int size) {
                return new WebBean[size];
            }
        };
    }
}
