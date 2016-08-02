package me.zouooh.bota;

public class BurroEvent {
	
	public static BurroEvent event(int code){
		return new BurroEvent(code);
	}
	
	public static BurroEvent event(int code,Object assign){
		return new BurroEvent(code,assign);
	}
	
	private int code = 0;
	private Object assign;
			
	public int getCode() {
		return code;
	}

	public BurroEvent(int code){
		this(code, null);
	}
	
	public BurroEvent(int code,Object assign){
		this.code = code;
		this.assign = assign;
	}

	public Object getAssign() {
		return assign;
	}

	public void setAssign(Object assign) {
		this.assign = assign;
	}
}
