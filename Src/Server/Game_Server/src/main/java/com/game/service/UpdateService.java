package com.game.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public interface UpdateService {
    // millisecond   毫秒执行一次
    // 主要负责 定时刷怪
    void update(int millisecond);


}
