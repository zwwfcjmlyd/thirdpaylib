
# thirdpaylib
第一次上传第三方支付库
How to
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

gradle
maven
sbt
leiningen
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.zwwfcjmlyd:thirdpaylib:v1.0.3'
	}
  
 Step 3. 项目根目录下创建wxapi包,里面创建类返回appid
 
	public class WXPayEntryActivity extends WXPayEntryBaseActivity {

	    @Override
	    public String getWXAppId() {
		return "APPID";
	    }
	}
  
  
 Step 4. Manifest里面添加如下内容
 
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.NFC"/>
    <uses-feature android:name="android.hardware.nfc.hce"/>
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
    <uses-permission android:name="org.simalliance.openmobileapi.SMARTCARD"/>
 
 	 <!-- pay start -->
        <uses-library
            android:name="org.simalliance.openmobileapi"
            android:required="false"/>

        <activity
            android:name="com.unionpay.uppay.PayActivity"
            android:configChanges="orientation|keyboardHidden|keyboard"
            android:excludeFromRecents="true"
            android:label="@string/app_name"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustResize"/>
        <activity
            android:name="com.unionpay.UPPayWapActivity"
            android:configChanges="orientation|keyboardHidden|fontScale"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="adjustResize"/>
        <activity
            android:name="com.zww.thirdpaylib.unionpay.activity.UnionPayAssistActivity"
            android:label=""
            android:launchMode="singleTask"
            android:theme="@style/Theme.Transparent"
            >
        </activity>

        <activity
            android:name="com.alipay.sdk.app.H5PayActivity"
            android:configChanges="orientation|keyboardHidden|navigation|screenSize"
            android:exported="false"
            android:screenOrientation="behind"
            android:windowSoftInputMode="adjustResize|stateHidden"></activity>
        <activity
            android:name="com.alipay.sdk.app.H5AuthActivity"
            android:configChanges="orientation|keyboardHidden|navigation"
            android:exported="false"
            android:screenOrientation="behind"
            android:windowSoftInputMode="adjustResize|stateHidden"></activity>
        <activity
            android:name=".wxapi.WXPayEntryActivity"
            android:configChanges="keyboardHidden|orientation|screenSize|keyboard|navigation"
            android:exported="true"
            android:launchMode="singleTop"
            android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
        <!-- pay end -->

 Step 5.调用
 
 

	 /**
     * 银联
     * @param param
     */
    private void unionpay(RechargeAliPayResponse param){
        //实例化银联支付策略
        UnionPay unionPay = new UnionPay();
        //构造银联订单实体。一般都是由服务端直接返回。测试时可以用Mode.TEST,发布时用Mode.RELEASE。
        UnionPayInfoImpli unionPayInfoImpli = new UnionPayInfoImpli();
        unionPayInfoImpli.setTn(/*param.result.from*/"814144587819703061900");
        unionPayInfoImpli.setMode(Mode.TEST);
        //策略场景类调起支付方法开始支付，以及接收回调。
        EasyPay.pay(unionPay, rechargeActivity, unionPayInfoImpli, new IPayCallback() {
            @Override
            public void success() {
                MyToast.show(rechargeActivity, "支付成功");
            }

            @Override
            public void failed() {
                MyToast.show(rechargeActivity, "支付失败");
            }

            @Override
            public void cancel() {
                MyToast.show(rechargeActivity, "支付取消");
            }
        });
    }


    /**
     * 支付宝支付业务
     *
     * @param param
     */
    public void payV2(RechargeAliPayResponse param) {
        //实例化支付宝支付策略
        AliPay aliPay = new AliPay();
        //构造支付宝订单实体。一般都是由服务端直接返回。
        AlipayInfoImpli alipayInfoImpli = new AlipayInfoImpli();
        alipayInfoImpli.setOrderInfo(param.result.from);
        //策略场景类调起支付方法开始支付，以及接收回调。
        EasyPay.pay(aliPay, rechargeActivity, alipayInfoImpli, new IPayCallback() {
            public void success() {
                MyToast.show(rechargeActivity, "支付成功");
            }

            @Override
            public void failed() {
                MyToast.show(rechargeActivity, "充值失败");
            }

            @Override
            public void cancel() {
                MyToast.show(rechargeActivity, "取消操作");
            }
        });
    }


    private void wxpay(WechatPayReponse wechatPayReponse) {
        //实例化微信支付策略
        WXPay wxPay = WXPay.getInstance(rechargeActivity, WX_APP_ID);
        //构造微信订单实体。一般都是由服务端直接返回。
        WXPayInfoImpli wxPayInfoImpli = new WXPayInfoImpli();
        wxPayInfoImpli.setTimestamp(wechatPayReponse.timestamp);
        wxPayInfoImpli.setSign(wechatPayReponse.sign);
        wxPayInfoImpli.setPrepayId(wechatPayReponse.prepayid);
        wxPayInfoImpli.setPartnerid(wechatPayReponse.partnerid);
        wxPayInfoImpli.setAppid(wechatPayReponse.appid);
        wxPayInfoImpli.setNonceStr(wechatPayReponse.noncestr);
        wxPayInfoImpli.setPackageValue(wechatPayReponse.packageX);
        //策略场景类调起支付方法开始支付，以及接收回调。

        EasyPay.pay(wxPay, rechargeActivity, wxPayInfoImpli, new IPayCallback() {
            @Override
            public void success() 
                MyToast.show(rechargeActivity, "支付成功");

            }

            @Override
            public void failed() {
                MyToast.show(rechargeActivity, "充值失败");
            }

            @Override
            public void cancel() {
                MyToast.show(rechargeActivity, "取消操作");
            }
        });
    }
