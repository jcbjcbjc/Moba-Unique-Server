package com.game.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;


import com.game.enums.UserStatus;
import com.game.network.NetConnection;

import com.game.proto.C2BNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.manager.ConnectionManager;
import com.game.manager.RoomManager;
import com.game.models.Room;
import com.game.models.User;
import com.game.network.NetConnectionWebSocket;
import com.game.proto.C2BNet.FrameHandle;
import com.game.proto.C2BNet.FrameHandleResponse;
import com.game.proto.C2BNet.C2BNetMessageResponse;
//import com.game.proto.Message.FrameHandle;
//import com.game.proto.Message.FrameHandleResponse;
//import com.game.proto.Message.NetMessageResponse2;
import com.game.util.CachedThreadPoolUtil;
import com.game.util.Config;

/**
 * 对战房间线程
 * @author Administrator
 *
 */
public class BattleRoomThread extends Thread{
	
	private static final Logger logger = LoggerFactory.getLogger(BattleRoomThread.class);
	private Room room;
	private FrameHandleResponse.Builder frameHandleResponseBuilder=FrameHandleResponse.newBuilder();
	private Collection<FrameHandle> frameHandleList=new ArrayList<>();
	private C2BNetMessageResponse.Builder response = C2BNetMessageResponse.newBuilder();
	Map<Integer, C2BNet.FrameHandlesFromClient> userFrameHandleMap = new HashMap<Integer, C2BNet.FrameHandlesFromClient>();
	
	public BattleRoomThread(Room room) {
		this.room=room;
	}
	
	/**
	 * 设置帧数据
	 */
	private void setFrameData() {
		frameHandleList.clear();
		frameHandleResponseBuilder.clear();
		userFrameHandleMap.clear();
		
		//当前帧所有用户的操作
		Map<Integer,C2BNet.FrameHandlesFromClient> userFrameHandle=room.frameHandles.get(room.currentFramId);
		if(userFrameHandle == null) {
			room.frameHandles.put(room.currentFramId, userFrameHandleMap);
		}else if(userFrameHandle != null && userFrameHandle.size() > 0) {
			for(C2BNet.FrameHandlesFromClient value: userFrameHandle.values()){
				frameHandleList.addAll(value.getFrameHandlesList());
			}
		}
		frameHandleResponseBuilder.setFrame(room.currentFramId);
		frameHandleResponseBuilder.addAllFrameHandles(frameHandleList);
		response.setFrameHandleRes(frameHandleResponseBuilder);
	}
	
	public void run() {
		//开始休眠
		try {
			Thread.sleep(room.startSleepMs);
		} catch (Exception e) {
			logger.error("异常",e);
		}
		//开观战线程
		ExecutorService cachedThreadPool = CachedThreadPoolUtil.instance();
        cachedThreadPool.execute(new BattleRoomLiveThread(this.room));
		while(true) {
			try {
				Thread.sleep(Config.FPS);  
				//设置帧数据
				this.setFrameData();
				//下发房间用户当前帧所有用户的操作
				for (User user: room.users) {
					NetConnection conn= ConnectionManager.getConnection(user.id);

					if(conn==null) {
						continue;
					}
					conn.sendFrameHandleRes(response);
				}
				
				room.currentFramId++;  //帧号+1

				if(room.users.isEmpty()){
					room.isGameOver=true;
				}
				if(room.isGameOver) { //游戏结束退出线程
					System.out.println("房间id:"+room.id+"，游戏正常结束");
					room.GameOver();
					room.overFramId = room.currentFramId;
					return;
				}
				//效验是否游戏是否超时
				if(room.currentFramId % 100 == 0) {
					long jg=new Date().getTime()-room.createTime;
					if(jg > room.gameOverMs) {
						System.out.println("房间id:"+room.id+"，游戏超时结束");
						room.GameOver();
						room.overFramId = room.currentFramId;
						return;
					}
				}
			} catch (Exception e) {
				logger.error("异常",e);
			}
		}
	}
}
