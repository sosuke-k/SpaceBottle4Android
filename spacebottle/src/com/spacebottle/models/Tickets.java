package com.spacebottle.models;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

public class Tickets extends SBTable<Ticket> {

	public Tickets(MobileServiceTable<Ticket> table) {
		super(table);
	}
	
	public Tickets(MobileServiceClient client){
		this(client.getTable(Ticket.class));
	}

	@Override
	public void add(Ticket item, TableOperationCallback<Ticket> callback) {
		getTable().insert(item, callback);
	}

	@Override
	public void remove(Ticket item, TableDeleteCallback callback) {
		getTable().delete(item, callback);
	}

	@Override
	public void update(Ticket item, TableOperationCallback<Ticket> callback) {
		getTable().update(item, callback);
	}
	
	public void read(TableQueryCallback<Ticket> callback){
		getTable().where().execute(callback);
	}

}
