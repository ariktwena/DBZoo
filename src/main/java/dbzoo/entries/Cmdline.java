package dbzoo.entries;

import dbzoo.api.DBZoo;
import dbzoo.api.InvalidPassword;
import dbzoo.domain.animal.Animal;
import dbzoo.domain.animal.AnimalType;
import dbzoo.domain.user.User;
import dbzoo.domain.user.UserExists;
import dbzoo.infrastructure.Database;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Cmdline {
    private final DBZoo zoo;
    
    public Cmdline() {
        Database d = new Database();
        zoo = new DBZoo(d, d);
    }

    public void parseArgs (String[] args) {
        if ("animals".equals(args[0])) {
            if ("list".equals(args[1])) {
                listAnimals();
            } else if ("add".equals(args[1])) {
                addAnimal(args[2], args[3]);
            }
        } else if ("users".equals(args[0])) {
            if ("create".equals(args[1])) {
                createUser(args[2], args[3]);
            } else if ("login".equals(args[1])) {
                loginUser(args[2], args[3]);
            }
        } else {
            System.err.println("Bad command: " + args[0]);
        }
    }

    private void loginUser(String name, String password) {
        System.out.println("Trying to login as: " + name);
        try {
            User user = zoo.login(name, password);
            System.out.println("Successfully logged in.");
        } catch (InvalidPassword invalidPassword) {
            System.out.println("Rejected.");
        }
    }

    public void listAnimals() {
        for (Animal a : zoo.findAllAnimals()) {
            System.out.printf("%-15s (%s) - %s%n", a.getName(), a.getBirthday().format(DateTimeFormatter.ISO_DATE),
                    zoo.findTypeOfAnimal(a).getName()
                    );
        }

    }

    public void addAnimal(String name, String type) {
        AnimalType animalType = zoo.findAnimalType(type);
        Animal a = zoo.createAnimal(name, LocalDate.now(), animalType);
        System.out.println(a);
    }

    public static void main(String[] args) {
        new Cmdline().parseArgs(args);
    }

    private void createUser(String name, String password) {
        try {
            System.out.println(zoo.createUser(name, password));
        } catch (UserExists userExists) {
            userExists.printStackTrace();
        }
    }
    }
