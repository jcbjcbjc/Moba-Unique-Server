package com.game;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;


import com.game.service.UpdateService;
import com.game.util.Config;
import com.game.util.TimeUtil;
import com.game.netty.KCPServer;
//@MapperScan("com.game.dao")
// 以下注解,需要在上服务器时  注释!!!
// 开启 ServletContextListener 监听器
@ServletComponentScan("com.game")
@EnableAsync
@SpringBootApplication
public class BattleMobaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BattleMobaApplication.class, args);
		// 启动时间类
		TimeUtil.init();
		// 开启 多线程执行 update 逻辑
		UpdateService updateService = context.getBean(UpdateService.class);
		updateService.update(100);

	}

	@Async
	@Override
	public void run(String... args) throws Exception {
		/**
		 * 使用异步注解方式启动netty服务端服务
		 */
		new KCPServer().bind(Config.PORT);
	}

}
