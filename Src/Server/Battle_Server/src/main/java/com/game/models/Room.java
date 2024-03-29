package com.game.models;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import com.game.enums.UserStatus;
import com.game.manager.RoomManager;
import com.game.proto.C2BNet;
import com.game.proto.C2BNet.FrameHandle;
import com.game.proto.C2BNet.RepairFrame;
//import com.game.proto.Message.FrameHandle;
//import com.game.proto.Message.RepairFrame;
import com.game.thread.BattleRoomThread;
import com.game.util.CachedThreadPoolUtil;
import com.game.util.Config;

/**
 * @author 贾超博
 *
 * Entity Class
 *
 *
 *
 */
public class Room {

	public int id; // 房间id
	public List<User> users; // 用户集合
	public long createTime = 0; // 创建时间
	public int loadResOverMs = 200000; //加载资源超时毫秒 1分钟
	public int gameOverMs = 2 * 60*60*1000; // 游戏超时毫秒 2小时
	public float overNum = 1; // 游戏结束人数判断
	public int startSleepMs = 3 * 1000; // 开始休眠毫秒

	// 帧操作数据 key:帧id value：key:用户id value：帧操作
	public Map<Integer, Map<Integer,C2BNet.FrameHandlesFromClient>> frameHandles = Collections.synchronizedMap(new HashMap<>());
	public int currentFramId; // 当前帧号
	public boolean isCreateThread = false; // 是否创建线程
	public boolean isGameOver = false; // 是否游戏结束
	public int overFramId = -1; // 结束帧号

	private boolean isAllUserLoadSucess = false; // 是否所有用户资源都加载成功

	public Room(int id, List<User> users) {
		super();
		this.id = id;
		this.users = users;
		this.createTime = new Date().getTime();
		System.out.println("房间ID："+id+"目前帧号:"+currentFramId);
	}

	/**
	 * 添加用户帧操作
	 * 
	 * @param userId      用户id
	 * @param frameHandle 帧操作对象
	 */
	public void AddUserFrameHandle(int userId, C2BNet.FrameHandlesFromClient frameHandle) {
		Map<Integer,C2BNet.FrameHandlesFromClient> userFrameHandle = frameHandles.get(currentFramId);

		if (userFrameHandle == null) {
			userFrameHandle = new HashMap<>();
			frameHandles.put(currentFramId, userFrameHandle);
		}
		userFrameHandle.put(userId, frameHandle);
	}

	/**
	 * 效验所有用户资源是否加载成功
	 * 
	 * @return
	 */
	public synchronized boolean ValidateAllUserLoadSucess() {
		boolean isLoadSucess = true;
		// 判断是否超时

		if (!this.getMatchOverMsFlag()) { // 未超时
			// 判断用户资源是否加载成功
			for (User user : users) {
				if (user.userStatus == UserStatus.normal) {
					System.out.println("filuasidkaskdh");
					isLoadSucess = false;
					break;
				}
			}
		}
		if(isLoadSucess){
			System.out.println("success all user load");
		}
		this.isAllUserLoadSucess = isLoadSucess;
		return isLoadSucess;
	}

	/**
	 * 判断匹配是否超时
	 * 
	 * @return
	 */
	private boolean getMatchOverMsFlag() {
		// 判断是否超时
		long jg = new Date().getTime() - this.createTime;
		return jg >= this.loadResOverMs;
	}

	/**
	 * 效验游戏是否结束
	 * 
	 * @return
	 */
	public synchronized void ValidateGameIsOver() {
		int gameOverNum = 0; // 游戏结束人数
		//int playerNum= users.size();
		for (User user : users) {
			if (user.userStatus == UserStatus.GameOver) {
				gameOverNum++;
			}
		}
		// 游戏结束
		if( gameOverNum >= overNum && !this.isGameOver) {
			this.isGameOver=true;
		    //this.GameOver();
		}
	}

	/**
	 * 游戏结束
	 */
	public void GameOver() {
		RoomManager.Instance.GameOver(id);
	}

	/**
	 * 获取范围帧数据
	 */
	public void GetRangeFrame(int startFrame, int endFrame, List<RepairFrame> repairFrameList) {
//		if (endFrame == -1) {  //查询所有帧
//			endFrame=currentFramId-1;
//		}
		// 补帧集合
		for (int frame = startFrame; frame <= endFrame; frame++) {
			Map<Integer,C2BNet.FrameHandlesFromClient> userFrameHandle = frameHandles.get(frame);
			// 补帧对象
			RepairFrame.Builder repairFrameBuilder = RepairFrame.newBuilder();
			repairFrameBuilder.setFrame(frame);
			if (userFrameHandle != null && userFrameHandle.size() > 0) {
				//TODO FIX
				for(C2BNet.FrameHandlesFromClient value:userFrameHandle.values()){
					repairFrameBuilder.addAllFrameHandles(value.getFrameHandlesList());
				}
			}
			repairFrameList.add(repairFrameBuilder.build());
		}
	}

	public void Update(int millisecond) {
		// 超时或资源都加载成功并且都未开启线程
		if (!this.isCreateThread && (this.getMatchOverMsFlag() || this.isAllUserLoadSucess)) {
			this.isCreateThread = true;

			// 获取线程池对象
			ExecutorService cachedThreadPool = CachedThreadPoolUtil.instance();
			System.out.println("StartRoomThread");
			cachedThreadPool.execute(new BattleRoomThread(this));
		}
	}
}
