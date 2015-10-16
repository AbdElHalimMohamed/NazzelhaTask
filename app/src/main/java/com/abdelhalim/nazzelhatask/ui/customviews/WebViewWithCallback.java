package com.abdelhalim.nazzelhatask.ui.customviews;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.abdelhalim.nazzelhatask.R;

/**
 * Created by Abd El-Halim on 10/16/2015.
 */
public class WebViewWithCallback extends WebView {

    private OnExecuteCallBackUrlListener onExecuteCallBackUrlListener;
    private String callBackUrl;

    public WebViewWithCallback(Context context) {
        super(context);
        init();
    }

    public WebViewWithCallback(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebViewWithCallback(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WebViewWithCallback(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setHorizontalFadingEdgeEnabled(false);
        setVerticalScrollBarEnabled(false);
        getSettings().setJavaScriptEnabled(true);
        setWebViewClient(new CallBackWebViewClient());
    }

    public void setOnExecuteCallBackUrlListener(OnExecuteCallBackUrlListener onExecuteCallBackUrlListener) {
        this.onExecuteCallBackUrlListener = onExecuteCallBackUrlListener;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    private class CallBackWebViewClient extends WebViewClient {

        ProgressDialog loadingDialog;

        public CallBackWebViewClient() {
            loadingDialog = new ProgressDialog(getContext());
            loadingDialog.setMessage(getResources().getString(R.string.auth_progress_message));
            loadingDialog.setCancelable(false);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            loadingDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadingDialog.dismiss();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (onExecuteCallBackUrlListener != null) {
                onExecuteCallBackUrlListener.onError(errorCode, description, failingUrl);
            }
            loadingDialog.dismiss();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith(callBackUrl)) {
                if (onExecuteCallBackUrlListener != null) {
                    onExecuteCallBackUrlListener.onComplete(url.replace(callBackUrl, ""));
                }
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    public interface OnExecuteCallBackUrlListener {
        void onComplete(String response);

        void onError(int errorCode, String description, String failingUrl);
    }
}
