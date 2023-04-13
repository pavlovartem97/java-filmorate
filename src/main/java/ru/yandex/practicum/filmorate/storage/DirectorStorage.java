package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {

    Director addDirector(Director director);

    void updateDirector(Director director);

    void deleteDirector(int directorID);

    List<Director> getAllDirectors();

    Optional<Director> getDirectorByID(int directorID);

    boolean contains(int directorID);
}
