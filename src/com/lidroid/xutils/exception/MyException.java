package com.lidroid.xutils.exception;

public class MyException extends Exception {
	protected String message;
	protected String causeMessage;

	private int exceptionCode;

	public int getExceptionCode() {
		return exceptionCode;
	}

	public MyException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MyException(Throwable detailMessage) {
		super(detailMessage);
		message = detailMessage.getMessage();
	}

	public MyException(String detailMessage) {
		super();
		message = detailMessage;
	}

	/**
	 * @param detailMessage：正常显示
	 * @param cause：补充在日志中的数据
	 */
	public MyException(String detailMessage, Throwable logError) {
		this(detailMessage);
		causeMessage = logError.getMessage();
	}

	public MyException(int code, String detailMessage) {
		this(detailMessage);
		this.exceptionCode = code;
	}

	public MyException setMessage(String message) {
		this.message = message;
		return this;
	}

	@Override
	public String getMessage() {
		if (exceptionCode != 0) {
			return exceptionCode + ":" + message;
		} else {
			return message;
		}
	}

	@Override
	public void printStackTrace() {
		// TODO Auto-generated method stub
		super.printStackTrace();
		MyLog.setLogText(message + causeMessage);
	}
}
