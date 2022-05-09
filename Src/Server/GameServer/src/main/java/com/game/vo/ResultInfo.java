package com.game.vo;
import com.game.proto.C2GNet.Result;
//import com.game.proto.Message.Result;

public class ResultInfo<T> {
	public Result result;
	public String errormsg;
	public T data;
	
	public ResultInfo(Result result, String errormsg) {
		super();
		this.result = result;
		this.errormsg = errormsg;
	}

	public ResultInfo(Result result, String errormsg, T data) {
		super();
		this.result = result;
		this.errormsg = errormsg;
		this.data = data;
	}
}
