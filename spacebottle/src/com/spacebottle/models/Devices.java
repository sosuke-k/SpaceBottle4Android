package com.spacebottle.models;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

public class Devices extends SBTable<Device>{

	public Devices(MobileServiceTable<Device> table) {
		super(table);
	}
	
	public Devices(MobileServiceClient client){
		this(client.getTable(Device.class));
	}

	@Override
	public void add(Device item, TableOperationCallback<Device> callback) {
		getTable().insert(item, callback);
	}
	public void add(Device item){
		add(item, EmptyOperationCallback);
	}

	@Override
	public void remove(Device item, TableDeleteCallback callback) {
		getTable().delete(item, callback);
	}

	@Override
	public void update(Device item, TableOperationCallback<Device> callback) {
		getTable().update(item, callback);
	}

}
