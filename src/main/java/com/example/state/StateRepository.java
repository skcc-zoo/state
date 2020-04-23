package com.example.state;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StateRepository extends CrudRepository<State, String> {
    Optional<State> findByKeeper(String keeper);
}
