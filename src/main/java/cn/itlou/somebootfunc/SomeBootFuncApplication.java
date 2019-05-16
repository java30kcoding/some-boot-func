package cn.itlou.somebootfunc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SomeBootFuncApplication {

	public static void main(String[] args) {
		SpringApplication.run(SomeBootFuncApplication.class, args);
	}

}
