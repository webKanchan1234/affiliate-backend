package com.blogdirectorio.affiliate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.imagekit.sdk.ImageKit;
import jakarta.annotation.PostConstruct;

@Configuration
public class ImageKitConfig {

    @Value("${imagekit.publicKey}")
    private String publicKey;

    @Value("${imagekit.privateKey}")
    private String privateKey;

    @Value("${imagekit.urlEndpoint}")
    private String urlEndpoint;

    @Bean
    public ImageKit imageKit() {
        ImageKit imageKit = ImageKit.getInstance();
        imageKit.setConfig(
            new io.imagekit.sdk.config.Configuration(
                publicKey,
                privateKey,
                urlEndpoint
            )
        );
        return imageKit;
    }

    @PostConstruct
    public void quickRuntimeTest() {
        System.out.println("✅ ImageKit Bean Loaded");
        System.out.println("🔗 ImageKit Endpoint: " + urlEndpoint);
    }
}
