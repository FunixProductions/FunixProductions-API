package com.funixproductions.api.service.core.configs;

import com.funixproductions.core.tools.network.IPUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("funixproductions.api")
public class FunixProductionsAppConfiguration {

    /**
     * Is the api running behind a proxy ? Like cloudflare or nginx
     */
    private boolean appProxied;

    /**
     * The domain for the dashboard, for example: https://dashboard.funixproductions.com
     */
    private String funixproductionsWebDashboardDomain;

    /**
     * The domain for the pacifista websote, for example: https://pacifista.fr
     */
    private String pacifistaWebPublicWebsiteDomain;

    @Bean
    public IPUtils ipUtils() {
        return new IPUtils(appProxied);
    }

}
