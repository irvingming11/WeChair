package com.at.wechair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: Annoming
 * @Date: 2020/5/1
 * @Time: 15:24
 * @Description
 */

@SpringBootApplication
@EnableTransactionManagement
public class WeChairApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeChairApplication.class, args);
	}

}
