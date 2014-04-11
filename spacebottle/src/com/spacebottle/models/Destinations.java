package com.spacebottle.models;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

public class Destinations extends SBTable<Destination> {

	public Destinations(MobileServiceTable<Destination> table) {
		super(table);
	}
	
	public Destinations(MobileServiceClient client){
		this(client.getTable(Destination.class));
	}

	@Override
	public void add(Destination item,
			TableOperationCallback<Destination> callback) {
		getTable().insert(item, callback);
	}

	@Override
	public void remove(Destination item, TableDeleteCallback callback) {
		getTable().delete(item, callback);
	}

	@Override
	public void update(Destination item,
			TableOperationCallback<Destination> callback) {
		getTable().update(item, callback);
	}

}
