package com.example.pathshala_plus_backend.supabase;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class SupabaseClientConfig {

    private final SupabaseProperties properties;

    @Bean
    public WebClient supabaseWebClient() {
        return WebClient.builder()
                .baseUrl(properties.getUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getServiceRoleKey())
                .defaultHeader("apikey", properties.getServiceRoleKey())
                .build();
    }
}