package com.vps.android.activity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ksoap2.serialization.PropertyInfo;

import com.vps.android.model.ServiceModel;
import com.vps.android.worker.ServiceCallback;
import com.vps.android.worker.WebServiceEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ParkingDetailsActivity extends Activity implements OnClickListener, ServiceCallback {
	EditText entryTime=null,token=null,ve=null;
	Button back=null,movie=null,pass=null;
	TextView amount=null,user=null,vType=null,timeSet=null,vt=null;
	String time=null;
	String vehType=null;
	int type=0;
	double vals;
	int paasType=0;
	String vehicType="FourWheeler";
	ServiceModel serviceModel=null;
	long tokenNo=32;
	String tost="";
	String userCode=null;
	String deviceCode=null;
	private SharedPreferences sharedPrefs=null;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.details);
		ImageView imgLogout=(ImageView)findViewById(R.id.imgLogout);
		sharedPrefs  = getSharedPreferences(
				"com.vps.android.activity", Context.MODE_PRIVATE);
		userCode=sharedPrefs.getString("UserCode",null);
		deviceCode=sharedPrefs.getString("DeviceCode",null);

		TextView txtUserName= (TextView) findViewById(R.id.user);
		String userName=getSharedPreferences("com.vps.android.activity", Context.MODE_PRIVATE).getString("UserName","null");
		Log.i("VPS","Entry Exit "+userName);
		txtUserName.setText("Welcome, "+userName+" ");
		Log.i("VPS","UserCode "+userCode+" DeviceCode "+deviceCode);
		vehType=getIntent().getExtras().getString("v");
		type=getIntent().getExtras().getInt("type");
		intialize();
		setListiners();
		Log.i("VPS","finalType "+type+" vehType "+vehType);
		TextView textView=(TextView)(findViewById(R.id.vtype));
		if(vehType.equals("two")){
			textView.setText(getResources().getString(R.string.TWO));
		}else if(vehType.equals("four")){
			textView.setText(getResources().getString(R.string.FOUR));
		}


		imgLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
				Intent intent=new Intent(ParkingDetailsActivity.this,LoginActivity.class);
				(ParkingDetailsActivity.this).startActivity(intent);

			}
		});
	}
	private void setListiners() {
		entryTime.setOnClickListener(this);
		back.setOnClickListener(this);
		movie.setOnClickListener(this);
		pass.setOnClickListener(this);


	}
	public void intialize()
	{
		serviceModel=new ServiceModel();
		entryTime=(EditText)findViewById(R.id.entryTime);
		back=(Button)findViewById(R.id.back);
		movie=(Button)findViewById(R.id.movie);
		pass=(Button)findViewById(R.id.pass);
		token=(EditText)findViewById(R.id.token);
		vType=(TextView)findViewById(R.id.vtype);
		user=(TextView)findViewById(R.id.user);
		amount=(TextView)findViewById(R.id.textView3);
		timeSet=(TextView)findViewById(R.id.textView5);
		vt=(TextView)findViewById(R.id.vt);
		ve=(EditText)findViewById(R.id.ve);


		String notation="AM";
		Calendar mcurrentTime = Calendar.getInstance();
		int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
		if(hour>=12)
		{
			notation="PM";
		}
		entryTime.setText(getDate()+" "+notation);



		if(type==0)
		{
			token.setVisibility(View.GONE);
			amount.setVisibility(View.GONE);
			if(vehType.equalsIgnoreCase("two"))
			{
				vehicType="TwoWheeler";
				vType.setText("TWO WHEELERS");
			}
		}
		else if(type==1)
		{
			vt.setVisibility(View.GONE);
			ve.setVisibility(View.GONE);
			timeSet.setText("Exit Time");
			vType.setVisibility(View.VISIBLE);
			movie.setVisibility(View.GONE);
			pass.setText("Print");
		}
	}


	public void onClick(View v) {
		switch (v.getId())
		{


		case R.id.back:
			finish();
			Intent intent=new Intent(ParkingDetailsActivity.this,VehicleActivity.class);
			intent.putExtra("type", type);
			(ParkingDetailsActivity.this).startActivity(intent);
			break;

		case R.id.movie:
			servicecall("Movie");
			break;

		case R.id.pass:
			if(type==1)
			{
				exitServiceCall(Long.parseLong(token.getEditableText().toString()));	
			}
			else
			{
				servicecall("Pass");	
			}
			break;


		}
	}


	private void exitServiceCall(long i) {
		// TODO Auto-generated method stub
		PropertyInfo propertyInfo=new PropertyInfo();
		propertyInfo.setName("TokenNo");
		propertyInfo.setValue(i);
		propertyInfo.setType(PropertyInfo.LONG_CLASS);
		serviceModel.addProperty(propertyInfo);

		propertyInfo=new PropertyInfo();
		propertyInfo.setName("ExitDateTime");
		propertyInfo.setValue(entryTime.getText().toString());
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);

		propertyInfo=new PropertyInfo();
		propertyInfo.setName("DeviceCode");
		propertyInfo.setValue(deviceCode);
		propertyInfo.setType(PropertyInfo.INTEGER_CLASS);
		serviceModel.addProperty(propertyInfo);

		propertyInfo=new PropertyInfo();
		propertyInfo.setName("UserCode");
		propertyInfo.setValue(userCode);
		propertyInfo.setType(PropertyInfo.INTEGER_CLASS);
		serviceModel.addProperty(propertyInfo);
		propertyInfo=new PropertyInfo();

		propertyInfo.setName("TokenType");
		propertyInfo.setValue("Movie");
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);

		propertyInfo=new PropertyInfo();
		propertyInfo.setName("VehicleType");
		propertyInfo.setValue("TwoWheeler");
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);

		propertyInfo=new PropertyInfo();
		propertyInfo.setName("VehicleNo");
		propertyInfo.setValue(0);
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);
		propertyInfo=new PropertyInfo();

		propertyInfo.setName("EntryDateTime");
		propertyInfo.setValue("");
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);



		serviceModel.setServiceOperation("SetExit");
		WebServiceEngine engine=new WebServiceEngine(ParkingDetailsActivity.this);
		engine.setServiceModel(serviceModel);
		engine.start();
	}
	private void servicecall(String passValue) {
		// TODO Auto-generated method stub

		Log.i("VPS","Vehicle No: "+ve.getEditableText().toString()+" Date: "+entryTime.getText().toString()+" Type :"+vehicType+" D_CODE : "+deviceCode+"U_CODE"+userCode);
		PropertyInfo propertyInfo=new PropertyInfo();
		propertyInfo.setName("VehicleNo");
		propertyInfo.setValue(ve.getEditableText().toString());
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);

		propertyInfo=new PropertyInfo();
		propertyInfo.setName("EntryDateTime");
		propertyInfo.setValue(entryTime.getText().toString());
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);

		propertyInfo=new PropertyInfo();
		propertyInfo.setName("TokenType");
		propertyInfo.setValue(passValue);
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);

		propertyInfo=new PropertyInfo();
		propertyInfo.setName("VehicleType");
		propertyInfo.setValue(vehicType);
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);

		propertyInfo=new PropertyInfo();
		propertyInfo.setName("DeviceCode");
		propertyInfo.setValue(deviceCode);
		propertyInfo.setType(PropertyInfo.INTEGER_CLASS);
		serviceModel.addProperty(propertyInfo);

		propertyInfo=new PropertyInfo();
		propertyInfo.setName("UserCode");
		propertyInfo.setValue(userCode);
		propertyInfo.setType(PropertyInfo.INTEGER_CLASS);
		serviceModel.addProperty(propertyInfo);
		serviceModel.setServiceOperation("SetEntry");
		WebServiceEngine engine=new WebServiceEngine(ParkingDetailsActivity.this);
		engine.setServiceModel(serviceModel);
		engine.start();
	}

	@Override
	public void onBackPressed() {
		//finish();
		//Intent intent=new Intent(ParkingDetailsActivity.this,EntryExitActivity.class);
		//(ParkingDetailsActivity.this).startActivity(intent);

	}

	@Override
	public void onResult(ServiceModel model) {
		// TODO Auto-generated method stub


		if(model.getResult().toString().equals("-1")){
			this.runOnUiThread(show_toast);
		}else{

			Log.i("VPS","parkingToken :"+model.getServiceOperation());
			Log.i("VPS","parkingToken :"+serviceModel.getResult().toString());


			if(model.getServiceOperation()=="SetEntry")
			{
				System.out.println(serviceModel.getResult().toString());
				tost="The Token Id is "+serviceModel.getResult().toString();
				this.runOnUiThread(show_toast);
			}
			else if(model.getServiceOperation()=="SetExit")
			{
				System.out.println(serviceModel.getResult().toString());
				tost="The bill has generated with the values "+serviceModel.getResult().toString();
				List list=(List) serviceModel.getResult();

				Log.i("VPS","SIZE "+list.size());

				if(list.get(0).toString().equals("true")){
					StringBuffer sb=new StringBuffer();
					sb.append("<div style='padding:20px'>");
					sb.append("<h4>BILL:</h4></br>");
					sb.append("<div><b>Token Type</b></div>");
					sb.append(list.get(1));
					sb.append("<div><b>Vehicle :</b></div>");
					sb.append(list.get(2));
					sb.append("<div><b>Vehicle No:</b><div>");
					sb.append(list.get(3));
					sb.append("<div><b>Entry DateTime:</b><div>");
					sb.append(list.get(4));
					sb.append("<div><b>Amount:</b></div>");
					sb.append("Rs. "+list.get(5));
					sb.append("</div>");
					tost=sb.toString();
					this.runOnUiThread(show_toast);
				}else{
					runOnUiThread(new Runnable() {						
						@Override
						public void run() {
						Toast.makeText(ParkingDetailsActivity.this,"Invalid token number",Toast.LENGTH_LONG).show();
							
						}
					});
				}

			}
			
		}
	}


	private String getDate()
	{
		java.text.DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
		Date date = new Date();


		return dateFormat.format(date);

	}


	private Runnable show_toast = new Runnable()
	{
		public void run()
		{
			//Toast.makeText(ParkingDetailsActivity.this,tost, Toast.LENGTH_SHORT).show();
			TextView msg = new TextView(ParkingDetailsActivity.this);

			Builder builder = new AlertDialog.Builder(ParkingDetailsActivity.this);
			//builder.setMessage(tost);
			msg.setText(Html.fromHtml(tost));
			builder.setView(msg);
			builder.setCancelable(true);
			builder.setPositiveButton("OK", new OkOnClickListener());

			AlertDialog dialog = builder.create();
			dialog.show();

		}
	};


	private final class OkOnClickListener implements   DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			finish();
			Intent intent=new Intent(ParkingDetailsActivity.this,EntryExitActivity.class);
			intent.putExtra("type", type);
			(ParkingDetailsActivity.this).startActivity(intent);
		}
	}




}
