insert into good_types (name, url) values
    ("Печенье", "https://i.ibb.co/jTwKNnL/17.jpg"),
    ("Торты", "https://i.ibb.co/DfdW2v6/9.jpg"),
    ("Кексы", "https://i.ibb.co/5WX7yD1/15.jpg"),
    ("Пончики", "https://i.ibb.co/hcYCNLL/16.jpg");

insert into good_subtypes (name, parent_id) values
    ("Песочное", 1),
    ("Макаруны", 1),
    ("Маффин", 3),
    ("Донат", 4),
    ("Берлинер", 4),
    ("Бисквитный", 2);

insert into goods (article, cost, count, name, descript, url, subtype_id) values
    (123043, 120, 405, "Шоколадный торт без взбитых сливок", "Вкусно", "https://i.ibb.co/s3zsZD9/8.jpg", 6),
    (109043, 120, 405, 'Самокритичное печенье "для тупого меня"', "Слишком сладко, но сладкое это хорошо, очень хорошо, просто чудесно, я чувствую, что скоро умру от того как это сладко или просто умру, посмотрим", "https://i.ibb.co/Ybz0fY7/img-not-exist.png", 1);

insert into order_statuses (name) values
    ("Не оформлен"),
    ("Оформлен"),
    ("В доставке"),
    ("Доставлен"),
    ("Выполнен");

insert into method_types (name) values
    ("Самовывоз из магазина"),
    ("Самовывоз из пункта выдачи"),
    ("Доставка почтой РФ"),
    ("Доставка курьером");

insert into ingredients (name) values
    ("яйца"),
    ("молоко"),
    ("мука"),
    ("казах"),
    ("эмульгаторы"),
    ("шоколад"),
    ("посыпка"),
    ("миндаль");

insert into ing_structures (good_id, ing_id) values
    (1, 1),
    (2, 1),
    (1, 4),
    (1, 5),
    (2, 2),
    (2, 3);
