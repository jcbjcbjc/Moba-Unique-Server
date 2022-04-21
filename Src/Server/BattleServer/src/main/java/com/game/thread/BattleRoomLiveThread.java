package com.game.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.manager.ConnectionManager;
import com.game.manager.RoomManager;
import com.game.models.Room;
import com.game.models.User;
import com.game.network.NetConnection;
import com.game.proto.Message.LiveFrameResponse;
import com.game.proto.Message.NetMessageResponse2;
import com.game.proto.Message.RepairFrame;
import com.game.proto.Message.RepairFrameResponse;
import com.game.util.Config;

/**
 * 直播房间线程
 * @author Administrator
 *
 */
public class BattleRoomLiveThread extends Thread{
	
	private static final Logger logger = LoggerFactory.getLogger(BattleRoomLiveThread.class);
	private Room room;
	private Map<Integer, Room> liveRooms=RoomManager.Instance.liveRooms;
	public int lastForwardFramId = -1;  //最后转发帧号
	public int sleepTime = Config.LiveIntervalForwardTime*1000; //休眠时间
	
	private LiveFrameResponse.Builder liveFrameResponseBuilder=LiveFrameResponse.newBuilder();
	private NetMessageResponse2.Builder response=NetMessageResponse2.newBuilder();
	private List<RepairFrame> rangeFrameList=new ArrayList<>();
	
	public BattleRoomLiveThread(Room room) {
		this.room=room;
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(sleepTime);  
				int start = lastForwardFramId+1;
				//游戏未结束最新帧减1，因为最新帧可能在采集中，防止未采集结束下发就会出现不同步
				int end = (!room.isGameOver ? room.currentFramId - 1 : room.currentFramId);   
				if(start > end) {
					continue;
				}
				//直播转发策略，可在这里实现
				Room liveRoom=this.liveRooms.get(room.id);
				if(liveRoom != null) {
					List<User> liveUsers = liveRoom.users;
					if(liveUsers != null && liveUsers.size() > 0) {
						//获取范围帧数据
						rangeFrameList.clear();
						room.GetRangeFrame(start, end, rangeFrameList);
						lastForwardFramId = end;  //更新最后转发帧
						
						liveFrameResponseBuilder.clear();
						liveFrameResponseBuilder.addAllLiveFrames(rangeFrameList);
						response.setLiveFrameRes(liveFrameResponseBuilder);
						
						for (User user: liveUsers) {
							NetConnection conn=ConnectionManager.getConnection(user.id);
							if(conn==null) {
								continue;
							}
							conn.sendLiveFrameRes(response);
						}
					}
				}
				if(room.isGameOver && room.overFramId == lastForwardFramId) { //游戏结束退出线程
					logger.error("房间id:"+room.id+"，游戏正常结束");
					return;
				}
			} catch (Exception e) {
				logger.error("异常",e);
			}
		}
	}
}
