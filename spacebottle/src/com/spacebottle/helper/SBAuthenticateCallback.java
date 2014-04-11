package com.spacebottle.helper;

public interface SBAuthenticateCallback {
	public void success();
	public void error(Exception exception);
}
