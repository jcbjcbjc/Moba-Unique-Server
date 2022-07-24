package com.game.service.impl;

import java.util.Map.Entry;
import java.util.Set;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.game.manager.RoomManager;
import com.game.models.Room;
import com.game.service.UpdateService;
import com.game.util.TimeUtil;

@Service
public class UpdateServiceImpl implements UpdateService {

	@Override
	@Async
	public void update(int millisecond) {
		while (true) {
			try {
				TimeUtil.Tick();
				Thread.sleep(millisecond);
				Set<Entry<Integer, Room>> set = RoomManager.Instance.rooms.entrySet();
				for (Entry<Integer, Room> entry : set) {
					Room room = entry.getValue();
					room.Update(millisecond);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
