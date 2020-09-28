package dbzoo.api;

import dbzoo.domain.animal.Animal;
import dbzoo.domain.animal.AnimalRepository;
import dbzoo.domain.animal.AnimalType;
import dbzoo.domain.user.User;
import dbzoo.domain.user.UserExists;
import dbzoo.domain.user.UserRepository;

import java.time.LocalDate;

public class DBZoo {
    private final AnimalRepository animals;
    private final UserRepository users;

    public DBZoo(AnimalRepository animals, UserRepository users) {
        this.animals = animals;
        this.users = users;
    }

    public Iterable<Animal> findAllAnimals() {
        return animals.findAllAnimals();
    }

    public AnimalType findTypeOfAnimal(Animal animal) {
        return animals.findTypeOfAnimal(animal);
    }

    public Animal createAnimal(String name, LocalDate now, AnimalType animalType) {
        return animals.createAnimal(name, now, animalType);
    }

    public AnimalType findAnimalType(String type) {
        return animals.findAnimalType(type);
    }

    public User createUser(String name, String password) throws UserExists {
        byte[] salt = User.generateSalt();
        byte[] secret = User.calculateSecret(salt, password);
        return users.createUser(name, salt, secret);
    }

    public User login(String name, String password) throws InvalidPassword {
        User user = users.findUser(name);
        if (user.isPasswordCorrect(password)) {
            return user;
        } else  {
            throw new InvalidPassword();
        }
    }

}
