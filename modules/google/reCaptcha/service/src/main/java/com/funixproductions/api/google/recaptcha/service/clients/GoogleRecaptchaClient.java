package com.funixproductions.api.google.recaptcha.service.clients;


import com.funixproductions.api.google.recaptcha.service.dtos.GoogleCaptchaSiteVerifyResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "GoogleRecaptchaClient",
        url = "https://www.google.com",
        path = "/recaptcha/api"
)
public interface GoogleRecaptchaClient {

    @PostMapping("siteverify")
    GoogleCaptchaSiteVerifyResponseDTO verify(@RequestParam("secret") String secret,
                                              @RequestParam("response") String response,
                                              @RequestParam("remoteip") String remoteIp,
                                              @RequestBody String body);

}
