package io.payworks.labs.tcpmocker.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api(final ServletContext servletContext) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(getClass().getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(URI.class, String.class)
                .genericModelSubstitutes(Optional.class, ResponseEntity.class)
                .apiInfo(new ApiInfo(
                        "Payworks TCP Mocker REST API",
                        "Use the 'TCP Mocker' interface to retrieve successful recorded requests and their replies",
                        "2018.1",
                        "urn:tos",
                        new Contact("Enrique Alonso-Martin", "https://payworks.com/", "enrique.alonso-martin@payworks.com"),
                        "Copyright (C) 2018 Payworks - All Rights Reserved",
                        "",
                        new ArrayList<>()));
    }
}
