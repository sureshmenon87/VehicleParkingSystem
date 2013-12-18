package com.vps.android.worker;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.vps.android.model.ServiceModel;


public class WebServiceEngine extends Thread{


	private String NAMESPACE = "http://tempuri.org/";
	private String URL       = "http://swastech.in/service.asmx";
	private ServiceCallback callback=null;
	private ServiceModel serviceModel;

	
	public WebServiceEngine(ServiceCallback callback) {
	this.callback=callback;
	}


	public ServiceModel getServiceModel() {
		return serviceModel;
	}



	public void setServiceModel(ServiceModel serviceModel) {
		this.serviceModel = serviceModel;
	}



	@Override
	public void run() {

		try {
			if(serviceModel!=null){

				HttpTransportSE httpTransport = new HttpTransportSE(URL);
				SoapObject request = new SoapObject(NAMESPACE, serviceModel.getServiceOperation());
				
				
				ArrayList<PropertyInfo> propertyList=(ArrayList<PropertyInfo>) serviceModel.getPropertyList();		
				int length=propertyList.size();						
				for(int i=0;i<length;i++){
				request.addProperty(propertyList.get(i));
				}
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				httpTransport.debug = true;
				Log.i("VPS",""+serviceModel.getServiceOperation());
				Log.i("VPS",""+httpTransport);
				httpTransport.call(NAMESPACE+serviceModel.getServiceOperation(), envelope);
				Log.i("VPS",""+envelope.getResponse());
				serviceModel.setResult(envelope.getResponse());
				callback.onResult(serviceModel);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
