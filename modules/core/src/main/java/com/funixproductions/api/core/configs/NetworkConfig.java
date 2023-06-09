package com.funixproductions.api.core.configs;

import com.funixproductions.core.tools.network.IPUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("funixproductions.api.network")
public class NetworkConfig {

    private Boolean appProxied = true;

    @Bean
    public IPUtils ipUtils() {
        return new IPUtils(appProxied);
    }

}
