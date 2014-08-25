/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springdeveloper.demo;

import org.apache.hive.jdbc.HiveDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.velocity.VelocityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@EnableAutoConfiguration(exclude={VelocityAutoConfiguration.class})
@Configuration
//@PropertySource("classpath:/hive.properties")
public class Application implements CommandLineRunner {

	@Autowired
	private DataSource dataSource;

//	@Value("${hive.url}")
//	String hiveUrl;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	public void run(String... strings) throws Exception {
		System.out.println("Running Hive task with '" + dataSource + "' ...");
		String query =
				"select tweets.username, tweets.followers " +
				"from " +
				"  (select distinct " +
				"    get_json_object(t.value, '$.user.screen_name') as username, " +
				"    cast(get_json_object(t.value, '$.user.followers_count') as int) as followers " +
				"    from tweetdata t" +
				"  ) tweets " +
				"order by tweets.followers desc limit 10";
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		List<Map<String, Object>> results = jdbc.queryForList(query);
		System.out.println("Results: ");
		for (Map<String, Object> r : results) {
			System.out.println(r.get("tweets.username") + " : " + r.get("tweets.followers"));
		}
	}

//	@Bean
//	DataSource dataSource() {
//		return new SimpleDriverDataSource(new HiveDriver(), "jdbc:hive2://borneo:10000/");
//	}

//	@Bean
//	static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
//		return new PropertySourcesPlaceholderConfigurer();
//	}

}
