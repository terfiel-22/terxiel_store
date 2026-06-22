package com.terxiel.store.controllers;

import com.terxiel.store.entities.Message;
import com.terxiel.store.repositories.ProductRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public Message index(Model model)
    {
        return new Message("SpringBoot");
    }
}
