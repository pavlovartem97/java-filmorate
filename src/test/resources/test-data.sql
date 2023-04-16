INSERT INTO director(director_name) VALUES 'First Director';
INSERT INTO director(director_name) VALUES 'Second Director';

INSERT INTO filmorate_user(email, name, login, birthday)
VALUES ('email1', 'name1', 'login1', '2000-10-31'),
       ('email2', 'name2', 'login2', '2010-10-31'),
       ('email3', 'name3', 'login3', '2015-10-31');

INSERT INTO film(name, description, release_date, duration, mpa_id)
VALUES ( 'name1', 'description1',  '2000-10-31', 100, 1 ),
       ( 'name2', 'description2',  '2010-10-31', 50, 3 ),
       ( 'name3', 'description3',  '2015-10-31', 200, 4 );

INSERT INTO favourite(film_id, user_id)
VALUES ( 1, 2 ),
       ( 1, 3 ),
       ( 3, 1 );

INSERT INTO film_genre(film_id, genre_id)
VALUES ( 1, 1 ),
       ( 1, 2 );

INSERT INTO film_director(film_id, director_id)
VALUES (1, 2),
       (2, 2),
       (3, 2);

INSERT INTO friend(user_id, friend_id, status)
VALUES ( 1, 2, false ),
       ( 3, 2, false );
