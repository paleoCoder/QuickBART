/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

/**
 * Provides a generic exception for any call to the BART API.
 * @author scott
 *
 */
public class BartApiException extends Exception {

	private static final long serialVersionUID = -3405771719461522524L;

	public BartApiException() {

	}

	public BartApiException(String detailMessage) {
		super(detailMessage);
	}

	public BartApiException(Throwable throwable) {
		super(throwable);
	}

	public BartApiException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
