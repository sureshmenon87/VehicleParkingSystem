package com.vps.android.activity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.vps.android.model.ServiceModel;
import com.vps.android.worker.ServiceCallback;
import com.vps.android.worker.WebServiceEngine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements ServiceCallback{


	private ServiceModel serviceModel=null;	

	private EditText userName=null;
	private EditText password=null;
	private RelativeLayout layoutLogin=null;
	private RelativeLayout layoutTriangle=null;
	private RelativeLayout layout01=null;
	private RelativeLayout deviceCheck=null;
	private SharedPreferences sharedPrefs=null;
	private SharedPreferences.Editor editor;
	private TextView sLogin=null;

	//I358049040172566
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		sharedPrefs  = getSharedPreferences("com.vps.android.activity", Context.MODE_PRIVATE);
		editor=sharedPrefs.edit();

		Button btnLogin=(Button)findViewById(R.id.btnLogin);
		Button btnExit=(Button)findViewById(R.id.btnExit);
		userName=(EditText)findViewById(R.id.txtUserName);
		password=(EditText)findViewById(R.id.txtPassword);

		serviceModel=new ServiceModel();
		layoutLogin=(RelativeLayout)findViewById(R.id.loginPanel);
		layoutTriangle=(RelativeLayout)findViewById(R.id.shapeTriangle);
		layout01=(RelativeLayout)findViewById(R.id.layout01);
		deviceCheck=(RelativeLayout)findViewById(R.id.deviceCheck);
		sLogin=(TextView)findViewById(R.id.start);
if(!isNetworkAvailable())
{
	Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG).show();
finish();	
}
		Log.i("VPS","IMEI "+getDeviceIMEI());
		Log.i("VPS","Using IMEI I358049040172566");
		PropertyInfo propertyInfo=new PropertyInfo();
		propertyInfo.setName("DeviceNo");
		propertyInfo.setValue("I358049040172566");
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);
		serviceModel.setServiceOperation("IsValidDevice");
		WebServiceEngine engine=new WebServiceEngine(LoginActivity.this);
		engine.setServiceModel(serviceModel);
		engine.start();



		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {


				Log.i("VPS","UN:"+userName.getEditableText().toString());
				Log.i("VPS","PWD:"+password.getEditableText().toString());
				
				if(userName.getEditableText().toString().length()!=0 && password.getEditableText().toString().length()!=0){
					PropertyInfo propertyInfo=new PropertyInfo();
					propertyInfo.setName("UserID");
					propertyInfo.setValue(userName.getEditableText().toString());
					propertyInfo.setType(PropertyInfo.STRING_CLASS);
					serviceModel.addProperty(propertyInfo);

					propertyInfo=new PropertyInfo();
					propertyInfo.setName("Password");
					propertyInfo.setValue(password.getEditableText().toString());
					propertyInfo.setType(PropertyInfo.STRING_CLASS);
					serviceModel.addProperty(propertyInfo);
					serviceModel.setServiceOperation("IsValidUser");
					WebServiceEngine engine=new WebServiceEngine(LoginActivity.this);
					engine.setServiceModel(serviceModel);
					engine.start();
				}else{
					Toast.makeText(LoginActivity.this,"Username/Password cannot be left empty", 3000).show();
				}

			}
		});
		btnExit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				System.exit(0);
			}
		});
	}



	@Override
	public void onResult(ServiceModel model) {
		Log.i("VPS","onResult :"+model.getServiceOperation());
		String result="-1";
		if (model.getServiceOperation().equals("IsValidDevice")) {
			
			Log.i("VPS","Devicecode :"+serviceModel.getResult().toString());
			
			if(serviceModel.getResult().toString().equals("0")){
			runOnUiThread("Invalid device. Please contact administrator");	
			}else{
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					editor.putString("DeviceCode", serviceModel.getResult().toString());
					
					editor.commit();
					Log.i("VPS"," DeviceCode "+sharedPrefs.getString("DeviceCode",null));
					
					layout01.setVisibility(RelativeLayout.VISIBLE);
					layoutLogin.setVisibility(RelativeLayout.VISIBLE);
					layoutTriangle.setVisibility(RelativeLayout.VISIBLE);
					deviceCheck.setVisibility(RelativeLayout.INVISIBLE);

					sLogin.setVisibility(View.VISIBLE);
					


				}
			});
			}

		}else if(model.getServiceOperation().equals("IsValidUser")){


			result=serviceModel.getResult().toString();
			Log.i("VPS","Usercode :"+result);
			if(result.equals("0")){
				runOnUiThread("Invalid username or password.");
			}else if(!result.equals("0") && !result.equals("-1") ){

				editor.putString("UserName", userName.getEditableText().toString());
				editor.putString("UserCode",serviceModel.getResult().toString());
				editor.commit();

				Intent intent=new Intent(LoginActivity.this,EntryExitActivity.class);
				(LoginActivity.this).startActivity(intent);
			}else if(result.equals("-1")){
				runOnUiThread("Please check internet and try again later.");
			}



		
		
		
		
		

		}

	}


	private String getDeviceIMEI(){

		TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		return mngr.getDeviceId();
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void runOnUiThread(final String msg){
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();	
			}
		});			
	}


}
