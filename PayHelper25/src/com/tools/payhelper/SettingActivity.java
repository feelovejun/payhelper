package com.tools.payhelper;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tools.payhelper.utils.AbSharedUtil;
import com.tools.payhelper.utils.MD5;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener{
	public String VERSION_RETURN_URL="http://doc.uc111888.com/api/pay/personal/notify";
	public String VERSION_NOTIFY_URL=VERSION_RETURN_URL;
	private EditText et_returnurl,et_notifyurl,et_signkey,et_wxid,et_apiurl,et_qrcode_id;
	private Button bt_save,bt_back;
	private RelativeLayout rl_back;
	String apiurl,qrcode_id,signkey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		setContentView(R.layout.activity_setting);
		
		et_qrcode_id=(EditText) findViewById(R.id.qrcodeid);
		et_apiurl=(EditText) findViewById(R.id.apiurl);
		et_signkey=(EditText) findViewById(R.id.signkey);
		et_wxid=(EditText) findViewById(R.id.et_wxid);

		if(!TextUtils.isEmpty(AbSharedUtil.getString(getApplicationContext(), "signkey"))){
			et_signkey.setText(AbSharedUtil.getString(getApplicationContext(), "signkey"));
		}
		if(!TextUtils.isEmpty(AbSharedUtil.getString(getApplicationContext(), "account"))){
			et_wxid.setText(AbSharedUtil.getString(getApplicationContext(), "account"));
		}
		if(!TextUtils.isEmpty(AbSharedUtil.getString(getApplicationContext(), "apiurl"))){
			et_apiurl.setText(AbSharedUtil.getString(getApplicationContext(), "apiurl"));
		}else{
			et_apiurl.setText("http://www.fj6868.com");
		}
		if(!TextUtils.isEmpty(AbSharedUtil.getString(getApplicationContext(), "ids"))){
			et_qrcode_id.setText(AbSharedUtil.getString(getApplicationContext(), "ids"));
		}
		bt_save=(Button) findViewById(R.id.save);
		bt_back=(Button) findViewById(R.id.back);
		rl_back=(RelativeLayout) findViewById(R.id.rl_back);
		bt_back.setOnClickListener(this);
		bt_save.setOnClickListener(this);
		rl_back.setOnClickListener(this);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
		
			apiurl=et_apiurl.getText().toString();	
			if(TextUtils.isEmpty(apiurl)){
				Toast.makeText(getApplicationContext(), "API域名不能为空！", Toast.LENGTH_LONG).show();
				return;
			}else{
				
				AbSharedUtil.putString(getApplicationContext(), "apiurl", apiurl);				
			}
			
			qrcode_id=et_qrcode_id.getText().toString();	
			if(TextUtils.isEmpty(qrcode_id)){
				Toast.makeText(getApplicationContext(), "二维码IDS不能为空！", Toast.LENGTH_LONG).show();
				return;
			}else{
				AbSharedUtil.putString(getApplicationContext(), "ids", qrcode_id);				
			}
			
			signkey=et_signkey.getText().toString();

			if(TextUtils.isEmpty(signkey)){
				Toast.makeText(getApplicationContext(), "密钥不能为空！", Toast.LENGTH_LONG).show();
				return;
			}else{
				AbSharedUtil.putString(getApplicationContext(), "signkey", signkey);
			}
			
			String wxid=et_wxid.getText().toString();
			if(!TextUtils.isEmpty(wxid)){
				AbSharedUtil.putString(getApplicationContext(), "account", wxid);
			}
			check();
			//Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.rl_back:
			finish();
			break;
		default:
			break;
		}
	}
	
	public void check() {
		try {
		
			HttpUtils httpUtils=new HttpUtils(15000);
			RequestParams params=new RequestParams();
			params.addBodyParameter("signkey", signkey);
			params.addBodyParameter("ids", qrcode_id);
	
			httpUtils.send(HttpMethod.POST, apiurl+"/pays/api/check", params, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(getApplicationContext(), "API验证失败", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					String result=arg0.result;
					if(result.contains("success")){
						Toast.makeText(getApplicationContext(), "验证成功", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(getApplicationContext(), "验证失败", Toast.LENGTH_LONG).show();
					}
				}
			});
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "API域名异常"+e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
}
