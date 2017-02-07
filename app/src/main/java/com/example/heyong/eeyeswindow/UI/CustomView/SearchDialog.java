package com.example.heyong.eeyeswindow.UI.CustomView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.Activity.ScanActivity;
import com.example.heyong.eeyeswindow.UI.Activity.SearchActivity;
import com.google.zxing.integration.android.IntentIntegrator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class SearchDialog extends Dialog{
    static public String SUBMIT_TEXT = "submit";

    @BindView(R.id.image_back)
    ImageView imageBack;

    @BindView(R.id.image_scan)
    ImageView imageScan;

    static Activity context;

    @BindView(R.id.sv_search)
    SearchView svSearch;


    private void init() {
        View layout = LayoutInflater.from(context).inflate(R.layout.popup_search,null,false);
        this.setContentView(layout);
        ButterKnife.bind(this, layout);
        svSearch.onActionViewExpanded();
        svSearch.setOnQueryTextListener(new MyQueryTextListener());
    }

    private SearchDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }


    public static SearchDialog buildInstance(Activity context){
        SearchDialog.context = context;
        SearchDialog dialog = new SearchDialog(context, R.style.CustomDialog);
        return dialog;
    }

    @OnClick(R.id.image_back)
    public void onClick() {
        dismiss();
    }

    @OnClick(R.id.image_scan)
    public void onClickImageScan() {
        if(context == null)
            throw new IllegalStateException("context is null");
        new IntentIntegrator(context).
                setCaptureActivity(ScanActivity.class).initiateScan();
        dismiss();
    }


    class MyQueryTextListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra(SUBMIT_TEXT, query);
            context.startActivity(intent);
            dismiss();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }

}
