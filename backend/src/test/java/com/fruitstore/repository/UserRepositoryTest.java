package com.fruitstore.repository;

import com.fruitstore.domain.user.User;
import com.fruitstore.domain.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for UserRepository
 * Tests CRUD operations and custom query methods
 */
@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testCustomer;
    private User testAdmin;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        userRepository.deleteAll();

        // Create test customer
        testCustomer = new User();
        testCustomer.setUsername("customer1");
        testCustomer.setEmail("customer1@example.com");
        testCustomer.setPassword("hashedPassword123");
        testCustomer.setFullName("Customer User");
        testCustomer.setPhone("0123456789");
        testCustomer.setAddress("123 Customer Street");
        testCustomer.setRole(UserRole.CUSTOMER);

        // Create test admin
        testAdmin = new User();
        testAdmin.setUsername("admin1");
        testAdmin.setEmail("admin@example.com");
        testAdmin.setPassword("hashedPassword456");
        testAdmin.setFullName("Admin User");
        testAdmin.setPhone("0987654321");
        testAdmin.setAddress("456 Admin Avenue");
        testAdmin.setRole(UserRole.ADMIN);
    }

    @Test
    void testSaveUser() {
        // When
        User savedUser = userRepository.save(testCustomer);

        // Then
        assertNotNull(savedUser);
        assertNotNull(savedUser.getUserId());
        assertEquals("customer1", savedUser.getUsername());
        assertEquals("customer1@example.com", savedUser.getEmail());
        assertEquals(UserRole.CUSTOMER, savedUser.getRole());
    }

    @Test
    void testFindById() {
        // Given
        User savedUser = userRepository.save(testCustomer);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<User> foundUser = userRepository.findById(savedUser.getUserId());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("customer1", foundUser.get().getUsername());
        assertEquals("customer1@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByEmail() {
        // Given
        userRepository.save(testCustomer);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<User> foundUser = userRepository.findByEmail("customer1@example.com");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("customer1", foundUser.get().getUsername());
        assertEquals("customer1@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByEmail_NotFound() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindByUsername() {
        // Given
        userRepository.save(testCustomer);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<User> foundUser = userRepository.findByUsername("customer1");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("customer1", foundUser.get().getUsername());
        assertEquals("customer1@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByUsername_NotFound() {
        // When
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testExistsByEmail() {
        // Given
        userRepository.save(testCustomer);
        entityManager.flush();

        // When
        boolean exists = userRepository.existsByEmail("customer1@example.com");
        boolean notExists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void testExistsByUsername() {
        // Given
        userRepository.save(testCustomer);
        entityManager.flush();

        // When
        boolean exists = userRepository.existsByUsername("customer1");
        boolean notExists = userRepository.existsByUsername("nonexistent");

        // Then
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void testFindByRole() {
        // Given
        userRepository.save(testCustomer);
        userRepository.save(testAdmin);
        
        User anotherCustomer = new User();
        anotherCustomer.setUsername("customer2");
        anotherCustomer.setEmail("customer2@example.com");
        anotherCustomer.setPassword("hashedPassword789");
        anotherCustomer.setFullName("Customer Two");
        anotherCustomer.setRole(UserRole.CUSTOMER);
        userRepository.save(anotherCustomer);
        
        entityManager.flush();
        entityManager.clear();

        // When
        List<User> customers = userRepository.findByRole(UserRole.CUSTOMER);
        List<User> admins = userRepository.findByRole(UserRole.ADMIN);

        // Then
        assertEquals(2, customers.size());
        assertEquals(1, admins.size());
        assertTrue(customers.stream().allMatch(u -> u.getRole() == UserRole.CUSTOMER));
        assertTrue(admins.stream().allMatch(u -> u.getRole() == UserRole.ADMIN));
    }

    @Test
    void testFindByEmailOrUsername_ByEmail() {
        // Given
        userRepository.save(testCustomer);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<User> foundUser = userRepository.findByEmailOrUsername("customer1@example.com", "anyusername");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("customer1", foundUser.get().getUsername());
    }

    @Test
    void testFindByEmailOrUsername_ByUsername() {
        // Given
        userRepository.save(testCustomer);
        entityManager.flush();
        entityManager.clear();

        // When
        Optional<User> foundUser = userRepository.findByEmailOrUsername("any@email.com", "customer1");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("customer1@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByEmailOrUsername_NotFound() {
        // When
        Optional<User> foundUser = userRepository.findByEmailOrUsername("nonexistent@email.com", "nonexistent");

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testCountByRole() {
        // Given
        userRepository.save(testCustomer);
        userRepository.save(testAdmin);
        
        User anotherCustomer = new User();
        anotherCustomer.setUsername("customer2");
        anotherCustomer.setEmail("customer2@example.com");
        anotherCustomer.setPassword("hashedPassword789");
        anotherCustomer.setFullName("Customer Two");
        anotherCustomer.setRole(UserRole.CUSTOMER);
        userRepository.save(anotherCustomer);
        
        entityManager.flush();

        // When
        long customerCount = userRepository.countByRole(UserRole.CUSTOMER);
        long adminCount = userRepository.countByRole(UserRole.ADMIN);

        // Then
        assertEquals(2, customerCount);
        assertEquals(1, adminCount);
    }

    @Test
    void testUpdateUser() {
        // Given
        User savedUser = userRepository.save(testCustomer);
        entityManager.flush();
        entityManager.clear();

        // When
        savedUser.setFullName("Updated Name");
        savedUser.setPhone("1111111111");
        User updatedUser = userRepository.save(savedUser);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<User> foundUser = userRepository.findById(updatedUser.getUserId());
        assertTrue(foundUser.isPresent());
        assertEquals("Updated Name", foundUser.get().getFullName());
        assertEquals("1111111111", foundUser.get().getPhone());
    }

    @Test
    void testDeleteUser() {
        // Given
        User savedUser = userRepository.save(testCustomer);
        Long userId = savedUser.getUserId();
        entityManager.flush();
        entityManager.clear();

        // When
        userRepository.deleteById(userId);
        entityManager.flush();

        // Then
        Optional<User> foundUser = userRepository.findById(userId);
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindAll() {
        // Given
        userRepository.save(testCustomer);
        userRepository.save(testAdmin);
        entityManager.flush();
        entityManager.clear();

        // When
        List<User> allUsers = userRepository.findAll();

        // Then
        assertEquals(2, allUsers.size());
    }

    @Test
    void testCount() {
        // Given
        userRepository.save(testCustomer);
        userRepository.save(testAdmin);
        entityManager.flush();

        // When
        long count = userRepository.count();

        // Then
        assertEquals(2, count);
    }
}

