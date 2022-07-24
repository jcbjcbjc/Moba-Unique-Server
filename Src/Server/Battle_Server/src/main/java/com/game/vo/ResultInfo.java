package com.game.vo;

import com.game.proto.Message.Result;

public class ResultInfo {
	public Result result;
	public String errormsg;

	public ResultInfo(Result result, String errormsg) {
		super();
		this.result = result;
		this.errormsg = errormsg;
	}

}
