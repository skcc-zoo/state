package com.example.state.controller;

import com.example.state.State;
import com.example.state.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/state")
public class Controller {

    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/")
    public Iterable<State> getStates() {
        return stateRepository.findAll();
    }
}
