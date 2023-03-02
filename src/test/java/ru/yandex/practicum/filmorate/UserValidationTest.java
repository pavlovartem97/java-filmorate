package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserValidationTest {

    private Validator validator;

    private static final LocalDate BIRTH_DATE = LocalDate.of(1980, 10, 10);

    @Autowired
    private UserValidator userValidator;

    @BeforeEach
    void beforeEach() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void userCreateTest() {
        User user = User.builder().name("Artem").login("Art").birthday(BIRTH_DATE).email("artem@gmail.com").build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());

        assertDoesNotThrow(() -> userValidator.validate(user));
    }

    @Test
    void userCreateFailLoginTest() {
        User user = User.builder().name("Artem").login("Artem Pavlov").birthday(BIRTH_DATE).email("artem@gmail.com").build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    void userCreateFailEmailTest() {
        User user = User.builder().name("Artem").login("art").birthday(BIRTH_DATE).email("gmail.com").build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());

        assertDoesNotThrow(() -> userValidator.validate(user));
    }

    @Test
    void userCreateFailBirthdayTest() {
        User user = User.builder().name("Artem").login("art").birthday(LocalDate.of(2050, 10, 10))
                .email("artem@gmail.com").build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());

        assertDoesNotThrow(() -> userValidator.validate(user));
    }

    @Test
    void userCreateWithEmptyNameTest() {
        User user = User.builder().login("art").birthday(BIRTH_DATE)
                .email("artem@gmail.com").build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());

        assertDoesNotThrow(() -> userValidator.validate(user));

        assertEquals(user.getName(), user.getLogin());
    }
}
