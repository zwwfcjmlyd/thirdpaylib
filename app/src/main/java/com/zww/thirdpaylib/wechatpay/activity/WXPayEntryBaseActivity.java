package com.zww.thirdpaylib.wechatpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zww.thirdpaylib.wechatpay.wxpay.WXPay;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * 在调用方项目的 包名.wxapi.WXPayEntryActivity类直接继续本类，并在AndroidManifest.xml中声明即可。
 */
public abstract class WXPayEntryBaseActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXPay.getInstance(this,getWXAppId()).getWXApi().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WXPay.getInstance(this,getWXAppId()).getWXApi().handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            WXPay.getInstance(this,getWXAppId()).onResp(baseResp.errCode);
            finish();
        }
    }

    public abstract String getWXAppId();
}