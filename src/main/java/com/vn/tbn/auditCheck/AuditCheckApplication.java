package com.vn.tbn.auditCheck;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableConfigurationProperties
@Configuration
@EnableAsync
public class AuditCheckApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuditCheckApplication.class, args);
	}
	
	@Bean
	public ThreadPoolTaskExecutor otherExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(100);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("defaul_task_executor_thread");
		executor.setRejectedExecutionHandler(new RejectedExecutionHandlerImpl());
		executor.initialize();
		return executor;
	}
	
	public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			executor.getQueue();
		}
	}
}
