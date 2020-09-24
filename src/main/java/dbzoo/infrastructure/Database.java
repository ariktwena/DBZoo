package dbzoo.infrastructure;

import dbzoo.domain.Animal;
import dbzoo.domain.AnimalFactory;
import dbzoo.domain.AnimalRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Database implements AnimalRepository {
    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/dbzoo";

    // Database credentials
    private static final String USER = "dbzoo";

    // Database version
    private static final int version = 2;

    public Database() throws ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        if (getCurrentVersion() != getVersion()) {
            throw new IllegalStateException("Database in wrong state");
        }
    }

    public Iterable<Animal> findAllAnimals() {
        ArrayList<Animal> animals = new ArrayList<>();
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, birthday, last_fed, type FROM animals;");
            while (rs.next()) {
                java.sql.Timestamp time = rs.getTimestamp("last_fed");
                Animal a = new Animal(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate(),
                        time != null ? time.toLocalDateTime() : null,
                        Animal.AnimalType.values()[rs.getInt("type")]);
                animals.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return animals;
        }
        return animals;
    }

    @Override
    public Animal createAnimal(Animal animal) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO animals (name, birthday, last_fed, type) VALUES (?,?,?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, animal.getName());
            ps.setDate(2, java.sql.Date.valueOf(animal.getBirthday()));
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(animal.getLastFed()));
            ps.setInt(4, animal.getType().ordinal());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return animal.withId(rs.getInt(1));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getVersion() {
        return version;
    }

    public static int getCurrentVersion() {
        try (Connection conn = getConnection()) {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT value FROM properties WHERE name = 'version';");
            if(rs.next()) {
                String column = rs.getString("value");
                return Integer.parseInt(column);
            } else {
                System.err.println("No version in properties.");
                return -1;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, null);
    }
}
