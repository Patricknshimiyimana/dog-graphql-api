package com.example.DogGraphqlApi.mutator;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.example.DogGraphqlApi.entity.Dog;
import com.example.DogGraphqlApi.exception.BreedNotFoundException;
import com.example.DogGraphqlApi.exception.DogNotFoundException;
import com.example.DogGraphqlApi.repository.DogRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Mutation implements GraphQLMutationResolver {
    private DogRepository dogRepository;

    public Mutation(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public Dog newDog(String name, String breed, String origin) {
        Dog newDog = new Dog(name, breed, origin);
        return dogRepository.save(newDog);
    }

    public boolean deleteDogBreed(String breed) {
        boolean deleted = false;
        Iterable<Dog> allDogs = dogRepository.findAll();
        // Loop through all dogs to check their breed
        for (Dog d : allDogs) {
            if (d.getBreed().equals(breed)) {
                // Delete if the breed is found
                dogRepository.delete(d);
                deleted = true;
            }
        }
        // Throw an exception if the breed doesn't exist
        if (!deleted) {
            throw new BreedNotFoundException("Breed Not Found", breed);
        }
        return deleted;
    }

    public boolean deleteDog(Long id) {
        Optional<Dog> optionalDog = dogRepository.findById(id);
        boolean deleted = false;
        if (optionalDog.isPresent()) {
            Dog d = optionalDog.get();
            dogRepository.delete(d);
            deleted = true;
        } else {
            throw new DogNotFoundException("Dog Not Found", id);
        }
        return deleted;
    }

    public Dog updateDogName(String newName, Long id) {
        Optional<Dog> optionalDog = dogRepository.findById(id);

        if (optionalDog.isPresent()) {
            Dog dog = optionalDog.get();
            // Set the new name and save the updated dog
            dog.setName(newName);
            dogRepository.save(dog);
            return dog;
        } else {
            throw new DogNotFoundException("Dog Not Found", id);
        }
    }

    public Dog updateDog(Long id, String name, String breed, String origin) {
        Optional<Dog> optionalDog = dogRepository.findById(id);
        if (optionalDog.isPresent()) {
            Dog dog = optionalDog.get();
            dog.setName(name);
            dog.setBreed(breed);
            dog.setOrigin(origin);
            dogRepository.save(dog);
            return dog;
        } else {
            throw new DogNotFoundException("Dog Not Found", id);
        }
    }
}