package com.example.heyong.eeyeswindow.Tools;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Heyong
 * <p>
 * 简单封装dialog方便使用
 */

public class SimpleDialogFactory {
    /**
     * 普通对话框（确认/取消）
     */
    public static void alertDialog(Context context, String title,
                                   String message, int iconID, final IAlertDialogCallBack callBack) {
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(callBack == null) return;
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        callBack.doSomething(true);
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        callBack.doSomething(false);
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  //先得到构造器
        builder.setTitle(title); //设置标题
        builder.setMessage(message); //设置内容
        builder.setIcon(iconID);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
    }


    public interface IAlertDialogCallBack {
        void doSomething(boolean isOK);
    }
}
