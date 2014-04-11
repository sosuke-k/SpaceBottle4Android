package com.spacebottle.models;

import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

abstract class SBTable<E> {
	protected final TableOperationCallback<E> EmptyOperationCallback = new TableOperationCallback<E>(){

		@Override
		public void onCompleted(E item, Exception exception,
				ServiceFilterResponse response) {
		}};
	protected final TableDeleteCallback EmptyDeleteCallback = new TableDeleteCallback(){

		@Override
		public void onCompleted(Exception exception, ServiceFilterResponse response) {
		}};
	public SBTable(MobileServiceTable<E> table){
		setTable(table);
	}
	private MobileServiceTable<E> mTable;
	
	public MobileServiceTable<E> getTable(){
		return mTable;
	}
	public final void setTable(MobileServiceTable<E> table){
		mTable = table;
	}
	abstract public void add(E item, TableOperationCallback<E> callback);
	abstract public void remove(E item, TableDeleteCallback callback);
	abstract public void update(E item, TableOperationCallback<E> callback);
	
}
