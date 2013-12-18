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
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class LoginActivity extends Activity implements ServiceCallback{


	private ServiceModel serviceModel=null;	

	private EditText userName=null;
	private EditText password=null;
	private RelativeLayout layoutLogin=null;
	private RelativeLayout layoutTriangle=null;
	private SharedPreferences sharedPrefs=null;

	//I358049040172566
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		Button btnLogin=(Button)findViewById(R.id.btnLogin);
		userName=(EditText)findViewById(R.id.txtUserName);
		serviceModel=new ServiceModel();
		layoutLogin=(RelativeLayout)findViewById(R.id.loginPanel);
		layoutTriangle=(RelativeLayout)findViewById(R.id.shapeTriangle);

		Log.i("VPS","IMEI "+getDeviceIMEI());
		PropertyInfo propertyInfo=new PropertyInfo();
		propertyInfo.setName("DeviceNo");
		propertyInfo.setValue(getDeviceIMEI());
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);
		serviceModel.setServiceOperation("IsValidDevice");
		WebServiceEngine engine=new WebServiceEngine(LoginActivity.this);
		engine.setServiceModel(serviceModel);
		engine.start();



		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				PropertyInfo propertyInfo=new PropertyInfo();
				propertyInfo.setName("UserID");
				propertyInfo.setValue("admin");
				propertyInfo.setType(PropertyInfo.STRING_CLASS);
				serviceModel.addProperty(propertyInfo);

				propertyInfo=new PropertyInfo();
				propertyInfo.setName("Password");
				propertyInfo.setValue("admin");
				propertyInfo.setType(PropertyInfo.STRING_CLASS);
				serviceModel.addProperty(propertyInfo);
				serviceModel.setServiceOperation("IsValidUser");
				WebServiceEngine engine=new WebServiceEngine(LoginActivity.this);
				engine.setServiceModel(serviceModel);
				engine.start();

			}
		});
	}



	@Override
	public void onResult(ServiceModel model) {
		Log.i("VPS","onResult :"+model.getServiceOperation());
		if (model.getServiceOperation().equals("IsValidDevice")) {
			sharedPrefs.edit().putString("DeviceCode", serviceModel.getResult().toString());
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					layoutLogin.setVisibility(RelativeLayout.VISIBLE);
					layoutTriangle.setVisibility(RelativeLayout.VISIBLE);

				}
			});

		}else if(model.getServiceOperation().equals("IsValidUser")){

		sharedPrefs.edit().putString("UserCode",serviceModel.getResult().toString());
		
		Intent intent=new Intent(LoginActivity.this,VehicleActivity.class);
		(LoginActivity.this).startActivity(intent);
		
		}

	}


	private String getDeviceIMEI(){

		TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		return mngr.getDeviceId();
	}


}
