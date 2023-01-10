package com.back.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.reactivestreams.Publisher;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.micronaut.http.server.netty.encoders.HttpResponseEncoder;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller("/image")
@Tag(name = "Image")
public class Image {

	@Value("${files.path}")
	private String filesPath;
	
	@Value("${files.allowed-extensions}")
	private List<String> allowedFileExtensions;

	@Post(value = "/", consumes = {MediaType.MULTIPART_FORM_DATA}, produces = {MediaType.TEXT_PLAIN}) 
	public Single<HttpResponse<String>> imgUpload (StreamingFileUpload file) throws IOException {

		String[] splitName = file.getFilename().split("\\.", 2);
		String extension = splitName[1];
		String fileName = UUID.randomUUID().toString();
		
		if (!allowedFileExtensions.contains(extension)) {
			return Single.error(new HttpStatusException(HttpStatus.BAD_REQUEST, "extensionNotAllowed"));			
		}
		
		File myObj = new File(filesPath +"/"+ fileName + "." + extension);
		Publisher<Boolean> uploadPublisher = file.transferTo(myObj);
		
		return Single.fromPublisher(uploadPublisher)  
				.map(success -> {
						if (success) {
								return HttpResponse.ok(fileName+"."+extension);
						} else {
								return HttpResponse.<String>status(HttpStatus.CONFLICT)
									.body("uploadFailed");
						}
				});
	}
	
}
