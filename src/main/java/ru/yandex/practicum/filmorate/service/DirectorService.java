package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public List<Director> getAllDirectors() {
        List<Director> directors = directorStorage.getAllDirectors();
        log.info("Size of all directors list: {}", directors.size());
        return directors;
    }

    public Director getDirectorByID(int id) {
        Director director = directorStorage.getDirectorByID(id)
                .orElseThrow(() -> {
                    throw new DirectorNotFoundException("Director with ID " + id + " not found");
                });
        log.info("Find director with ID {} his name {}", director.getId(), director.getName());
        return director;
    }

    public Director addDirector(Director director) {
        directorStorage.addDirector(director);
        log.info("New director created, his ID {} and name {}", director.getId(), director.getName());
        return director;
    }

    public Director updateDirector(Director director) {
        checkDirector(director.getId());
        directorStorage.updateDirector(director);
        log.info("Director with ID {} updated", director.getId());
        return director;
    }

    public void deleteDirector(int id) {
        checkDirector(id);
        directorStorage.deleteDirector(id);
        log.info("Director with ID {} removed", id);
    }

    private void checkDirector(int directorID) {
        if (!directorStorage.contains(directorID)) {
            throw new DirectorNotFoundException("Director with ID " + directorID + " not found");
        }
    }
}
