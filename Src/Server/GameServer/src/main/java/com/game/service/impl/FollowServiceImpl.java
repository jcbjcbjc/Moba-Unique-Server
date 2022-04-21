package com.game.service.impl;

import org.springframework.stereotype.Service;

import com.game.entity.User;
import com.game.network.NetConnection;
import com.game.proto.Message.FollowListRequest;
import com.game.proto.Message.FollowListResponse;
import com.game.proto.Message.NetMessageResponse;
import com.game.service.FollowService;

@Service
public class FollowServiceImpl implements FollowService {

	/**
	 * 关注列表查询
	 */
	@Override
	public void OnFollowList(NetConnection connection, FollowListRequest followListReq) {
		User user = connection.getSession().user;
		NetMessageResponse.Builder response = connection.getResponse();
		FollowListResponse build = FollowListResponse.newBuilder().addAllFollows(user.followManager.getFollowInfos())
				.build();
		response.setFollowListRes(build);
		connection.send();
	}

}
