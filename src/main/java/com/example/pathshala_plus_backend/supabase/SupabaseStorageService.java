package com.example.pathshala_plus_backend.supabase;

import com.example.pathshala_plus_backend.exceptions.StorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupabaseStorageService {

    private final WebClient supabaseWebClient;
    private final SupabaseProperties properties;

    public String upload(StorageBucket bucket, String path, byte[] content, String contentType) {
        URI uri = URI.create(properties.getUrl() + "/storage/v1/object/"
                + bucket.getBucketName() + "/" + path);

        supabaseWebClient.post()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header("x-upsert", "true")
                .bodyValue(content)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> new StorageException("Supabase upload failed: " + body)))
                .toBodilessEntity()
                .block();

        return properties.getUrl() + "/storage/v1/object/public/" + bucket.getBucketName() + "/" + path;
    }

    public void deleteByPublicUrl(StorageBucket bucket, String publicUrl) {
        String marker = "/storage/v1/object/public/" + bucket.getBucketName() + "/";
        int idx = publicUrl.indexOf(marker);
        if (idx == -1) {
            return;
        }
        String path = publicUrl.substring(idx + marker.length());
        URI uri = URI.create(properties.getUrl() + "/storage/v1/object/"
                + bucket.getBucketName() + "/" + path);

        supabaseWebClient.method(HttpMethod.DELETE)
                .uri(uri)
                .retrieve()
                .toBodilessEntity()
                .onErrorResume(e -> {
                    log.warn("Failed to delete old file at path {} in bucket {}: {}",
                            path, bucket.getBucketName(), e.getMessage());
                    return Mono.empty();
                })
                .block();
    }
}