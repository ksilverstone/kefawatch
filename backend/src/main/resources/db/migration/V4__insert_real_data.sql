INSERT INTO titles (type, name, description, poster_url, trailer_url, external_ref) VALUES 
('MOVIE', 'The Dark Knight', 'Batman, Joker ile olan amansız mücadelesinde Gotham şehrini kurtarmak zorundadır. Yılların efsanesi, aksiyonun zirvesi.', 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/qJ2tW6WMUDux911r6m7haRef0WH.jpg', '', ''),
('MOVIE', 'Inception', 'Rüyalar içinde rüyalar. Bir hırsız, kurbanının bilinçaltına girerek bir fikri yerleştirmekle görevlendirilir. Christopher Nolan imzalı bilim kurgu şaheseri.', 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg', '', ''),
('MOVIE', 'Interstellar', 'Dünya yaşanmaz hale geldiğinde, bir grup astronot insanlık için yeni bir yuva bulmak üzere uzayın derinliklerine yolculuğa çıkar. Kara deliklerin gizemi.', 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg', '', ''),
('SERIES', 'Breaking Bad', 'Kansere yakalanan sıradan bir kimya öğretmeni, ailesinin geleceğini güvence altına almak için efsanevi bir uyuşturucu baronu "Heisenberg"e dönüşür.', 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/ggFHVNu6YYI5L9pCfOacjizRGt.jpg', '', ''),
('SERIES', 'Stranger Things', 'Küçük bir kasabada kaybolan bir çocuk, paralel evrenler ve ortaya çıkan doğaüstü güçler.', 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/x2LSRK2CJZHo311zO02E6sO2r21.jpg', '', ''),
('MOVIE', 'The Matrix', 'Gerçeklik algınızı sorgulatacak, distopik bir gelecekte makinelerle insanların savaşı.', 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg', '', ''),
('MOVIE', 'The Lord of the Rings: The Fellowship of the Ring', 'Orta Dünya''nın kaderi küçük bir Hobbit''in elindedir.', 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg', '', ''),
('SERIES', 'Game of Thrones', 'Yedi krallık, Demir Taht için savaşıyor.', 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/1XS1oqL89opfnbLl8WnZY1O1uC.jpg', '', '')
ON CONFLICT DO NOTHING;
