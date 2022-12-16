package com.back.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller("/image")
@Tag(name = "Image")
public class Image {
	
	@Get("/upload")
	public Single<Boolean> upload() {
		return Single.just(true);
	}
	
}
