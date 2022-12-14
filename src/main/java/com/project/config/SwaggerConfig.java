package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

   /**
    * Method to get paths to be included through swagger
    *
    * @return Docket
    */

   @Bean
   public Docket api() {
      return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiDetails())
            .select()
            .paths(PathSelectors.any())
            .apis(RequestHandlerSelectors.basePackage("com.project"))
            .build();
   }

   /**
    * Method to set swagger info
    *
    * @return ApiInfoBuilder
    */

   private ApiInfo apiDetails() {
      return new ApiInfoBuilder()
            .title("Swagger Petstore REST API Documentation - Demo")
            .description("Swagger REST API Demo - School for Java Assignment")
            .version("1.0")
            .build();
   }
}
