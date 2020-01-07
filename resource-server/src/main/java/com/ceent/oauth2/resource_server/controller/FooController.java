package com.ceent.oauth2.resource_server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

@RestController
public class FooController {

    @PreAuthorize("#oauth2.hasScope('read-foo')")
    @GetMapping("/foo")
    public Integer findById() {
        return new SecureRandom().nextInt();
    }
}
