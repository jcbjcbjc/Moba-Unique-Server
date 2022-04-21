package com.game.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.game.manager.MatchManager;
import com.game.service.UpdateService;
import com.game.util.TimeUtil;

@Service
public class UpdateServiceImpl implements UpdateService {

	@Override
	@Async
	public void update(int millisecond) {

		MatchManager matchManager = MatchManager.Instance;
		while (true) {
			try {
				TimeUtil.Tick();
				Thread.sleep(millisecond);

				matchManager.Update();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
