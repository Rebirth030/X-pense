package com.laborsoftware.xpense;

import org.apache.catalina.filters.CorsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class XPenseApplication {
	private static final Logger logger = LoggerFactory.getLogger(XPenseApplication.class);
	private static final List<String> requiredOptions = Arrays.asList("dbUrl", "dbUser", "dbPwd");


	public static void main(String[] args) {
		SpringApplication.run(XPenseApplication.class, args);
	}

	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder, ApplicationArguments args) {
		logger.info("Application started with the following arguments:");
		for (String name : args.getOptionNames()) {
			logger.info(name + " -> " + args.getOptionValues(name));
		}

		if (!args.getOptionNames().containsAll(requiredOptions)) {
			logger.error(String.format("Not all required arguments were supplied.  Required arguments are: %s", requiredOptions));
			System.exit(1);
		}

		Map<String, Object> properties = new HashMap<>();
		for (String requiredOption : requiredOptions) {
			properties.put(requiredOption, args.getOptionValues(requiredOption));
		}

		if (args.getOptionNames().contains("dropFirst")) {
			properties.put("liquibase.drop-first", args.getOptionValues("dropFirst"));
		}

		return builder
				.properties(properties)
				.sources(XPenseApplication.class);
	}


	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter();
	}


}
