package com.example.DogGraphqlApi.repository;

import com.example.DogGraphqlApi.entity.Dog;
import org.springframework.data.repository.CrudRepository;

public interface DogRepository extends CrudRepository<Dog, Long> {
}