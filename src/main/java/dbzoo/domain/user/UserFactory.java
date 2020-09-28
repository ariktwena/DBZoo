package dbzoo.domain.user;

public interface UserFactory {
    User createUser(String name, byte[] salt, byte[] secret) throws UserExists;
}
