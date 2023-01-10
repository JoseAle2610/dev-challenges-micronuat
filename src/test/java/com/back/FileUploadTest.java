package com.back;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
// import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
class FileUploadTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void uploadOk() {
        try {
            File file= File.createTempFile("data", ".zip");
            MultipartBody requesBody = MultipartBody.builder()
                .addPart("file", file.getName(), MediaType.TEXT_PLAIN_TYPE, file)
                .build();
            HttpRequest request = HttpRequest.POST("/image", requesBody)
                .contentType(MediaType.MULTIPART_FORM_DATA_TYPE);
            HttpResponse<String> response = client.toBlocking().exchange(request, String.class);
            assertEquals(HttpStatus.OK, response.getStatus());
            assertEquals(MediaType.TEXT_PLAIN_TYPE, response.getContentType());
            assertEquals(response.body(), "Uploaded");
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    @Test
    public void uploadBad () throws IOException {
        File file= File.createTempFile("data", ".zip");
        MultipartBody requesBody = MultipartBody.builder()
            .addPart("file", file.getName(), MediaType.TEXT_PLAIN_TYPE, file)
            .build();
        HttpRequest request = HttpRequest.POST("/image", requesBody)
            .contentType(MediaType.MULTIPART_FORM_DATA_TYPE);
        HttpClientResponseException exception = Assertions.assertThrows(
            HttpClientResponseException.class,
            () -> client.toBlocking().exchange(request, String.class)
        );
        assertEquals(HttpStatus.BAD_REQUEST, exception.getResponse().getStatus());
        System.out.println(exception.getMessage());
    }

}
