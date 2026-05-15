INSERT INTO titles (id, type, name, description, poster_url, trailer_url, external_ref)
VALUES (1, 'SHOW', 'Kefawatch Demo',
        'Sample series for TBL324 demo.',
        'https://placehold.co/300x450?text=Kefawatch',
        'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        'seed-1'),
       (2, 'MOVIE', 'Demo Film',
        'Sample movie entry.',
        'https://placehold.co/300x450?text=Movie',
        NULL,
        'seed-2');

INSERT INTO episodes (title_id, season_number, episode_number, name)
VALUES (1, 1, 1, 'Pilot'),
       (1, 1, 2, 'Second Episode'),
       (1, 2, 1, 'Season Two Opener');

SELECT setval(pg_get_serial_sequence('titles', 'id'), (SELECT MAX(id) FROM titles));
SELECT setval(pg_get_serial_sequence('episodes', 'id'), (SELECT MAX(id) FROM episodes));
