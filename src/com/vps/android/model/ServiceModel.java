package com.vps.android.model;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.PropertyInfo;

import com.vps.android.worker.WEBSERVICE_CONSTANT;

public class ServiceModel {
	
private WEBSERVICE_CONSTANT constant;
private String serviceOperation=null;;
private List<PropertyInfo> propertyList=new ArrayList<PropertyInfo>();
private Object result=null;

public WEBSERVICE_CONSTANT getConstant() {
	return constant;
}
public void setConstant(WEBSERVICE_CONSTANT constant) {
	this.constant = constant;
}
public String getServiceOperation() {
	return serviceOperation;
}
public void setServiceOperation(String serviceOperation) {
	this.serviceOperation = serviceOperation;
}
public List<PropertyInfo> getPropertyList() {
	return propertyList;
}

private void setPropertyList(List<PropertyInfo> propertyList) {
	this.propertyList = propertyList;
}

public void addProperty(PropertyInfo propertyInfo) {
	this.propertyList.add(propertyInfo);
}
public Object getResult() {
	return result;
}
public void setResult(Object result) {
	this.result = result;
}




}
