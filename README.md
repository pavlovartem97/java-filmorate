# java-filmorate
Template repository for Filmorate project.

## ER diogramm:
<image src="https://github.com/pavlovartem97/java-filmorate/blob/main/resources/er_digram.png" alt="Описание картинки">

### SELECT examples:


1. Get film by id:
~~~~sql
SELECT *  
FROM films  
WHERE film_id=3;  
~~~~
2. Get user by id:
~~~~sql
SELECT *  
FROM users  
WHERE user_id=2;  
~~~~
3. Get all films:
~~~~sql
SELECT *  
FROM films; 
~~~~ 
4. Get top 10 popular films:
~~~~sql
SELECT *  
FROM films  
WHERE film_id IN (SELECT film_id  
                  FROM likes  
                  GROUP BY film_id  
                  ORDER BY COUNT(film_id) DESC
                  LIMIT 10);  
~~~~
5. Get all friends for user with id=3:
~~~~sql
SELECT *  
FROM users  
WHERE user_id IN (SELECT friend_id  
                  FROM friends  
                  WHERE user_id=3
                        AND status=TRUE);  
~~~~
6. Get common friends for user with id=3 and id = 4:
~~~~sql
SELECT *  
FROM users  
WHERE user_id IN (SELECT friend_id
                  FROM friends
                  WHERE user_id = 3
                        AND NOT friend_id = 4
                        AND status = TRUE
                        AND friend_id IN (SELECT friend_id
                                          FROM friends
                                          WHERE user_id = 4
                                                AND status = TRUE));
~~~~




