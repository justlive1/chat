package cn.my.chatclient.exception;

public class CanceledException extends RuntimeException{

	private static final long serialVersionUID = 1123326113375784798L;

	public CanceledException(){
		super();
	}
	
	public CanceledException(String msg){
		super(msg);
	}
}
