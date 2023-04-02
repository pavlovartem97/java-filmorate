# java-filmorate
Template repository for Filmorate project.

## ER diogramm:
<image src="https://github.com/pavlovartem97/java-filmorate/blob/add-database/resources/er_diagram.png" alt="Описание картинки">

### SELECT examples:


1. Get film by id:
~~~~sql
SELECT *  
FROM film  
WHERE film_id=3;  
~~~~
2. Get user by id:
~~~~sql
SELECT *  
FROM filmorate_user  
WHERE user_id=2;  
~~~~
3. Get all films:
~~~~sql
SELECT *  
FROM film; 
~~~~ 
4. Get top 10 popular films:
~~~~sql
SELECT *
FROM (SELECT f.FILM_ID, f.Name, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, COUNT(fv.USER_ID) rating
      FROM film f
      LEFT JOIN FAVOURITE fv ON f.FILM_ID = fv.FILM_ID
      GROUP BY f.FILM_ID
      ORDER BY rating desc, f.FILM_ID
      LIMIT 10) fl
JOIN mpa m ON fl.mpa_id = m.mpa_id
LEFT JOIN film_genre as gf ON fl.film_id = gf.film_id
LEFT JOIN genre g ON gf.GENRE_ID = g.genre_id;
~~~~
5. Get all friends for user with id=3:
~~~~sql
SELECT *  
FROM filmorate_user
WHERE user_id IN (SELECT friend_id  
                  FROM friend 
                  WHERE user_id=3);  
~~~~
6. Get common friends for user with id=3 and id = 4:
~~~~sql
SELECT *
FROM filmorate_user
WHERE user_id IN (SELECT friend_id
                  FROM friend
                  WHERE user_id = 3
                        AND NOT friend_id = 4
                        AND friend_id IN (SELECT friend_id
                                          FROM friend
                                          WHERE user_id = 4));
~~~~




