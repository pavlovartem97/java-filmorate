package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;
import ru.yandex.practicum.filmorate.validators.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserValidationTest {

    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    private static final LocalDate birthDate = LocalDate.of(1980, 10, 10);

    @Test
    void userCreateTest() {
        User user = User.builder().name("Artem").login("Art").birthday(birthDate).email("artem@gmail.com").build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());

        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void userCreateFailLoginTest() {
        User user = User.builder().name("Artem").login("Artem Pavlov").birthday(birthDate).email("artem@gmail.com").build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());

        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    void userCreateFailEmailTest() {
        User user = User.builder().name("Artem").login("art").birthday(birthDate).email("gmail.com").build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());

        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void userCreateFailBirthdayTest() {
        User user = User.builder().name("Artem").login("art").birthday(LocalDate.of(2050, 10, 10))
                .email("artem@gmail.com").build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());

        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void userCreateWithEmptyNameTest() {
        User user = User.builder().login("art").birthday(birthDate)
                .email("artem@gmail.com").build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());

        assertDoesNotThrow(() -> UserValidator.validate(user));

        assertEquals(user.getName(), user.getLogin());
    }
}
