package com.funixproductions.api.core.configs;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("funixproductions.api.front-applications-domains")
public class FrontApplicationsDomainsConfig {

    /**
     * https://dashboard.funixproductions.com
     */
    private String dashboardDomain = "https://dashboard.funixproductions.com";

    /**
     * https://pacifista.fr
     */
    private String pacifistaFrontDomain = "https://pacifista.fr";

}
