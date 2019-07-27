package com.firecode.elktest.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EntityScan(basePackages={"com.firecode.elktest.biz.domain"})
@EnableJpaRepositories(basePackages={"com.firecode.elktest.biz.dao"})
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)//spring在多长时间后强制使redis中的session失效,默认是1800.(单位/秒)
public class Application {
	
	public static void main(String[] args) {
        //防止netty与初始化Elasticsearch client冲突时的异常
        System.setProperty("es.set.netty.runtime.available.processors", "false");
		SpringApplication.run(Application.class, args);
	}
}
