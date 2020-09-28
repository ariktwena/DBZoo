package dbzoo.domain.user;

public interface UserRepository extends UserFactory {
    User findUser(String name);
    Iterable<User> findAllUsers();
}
