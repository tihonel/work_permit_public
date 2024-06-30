insert into WORKER(name, dative_name, access_group, right_issuing, driver, type, operational_rights)
values ('Оперативному персоналу', 'Оперативному персоналу', 5, false, false, 'OW', true);
insert into WORKER(name, dative_name, access_group, right_issuing, driver, type, operational_rights)
values ('Не назначается', 'Не назначается', 5, false, false, 'UW', false);
insert into WORKER(name, dative_name, access_group, right_issuing, driver, type, operational_rights)
values ('Иванов И.А.', 'Иванову И.А.', 5, true, false, 'W', true);
insert into WORKER(name, dative_name, access_group, right_issuing, driver, type, operational_rights)
values ('Эльдарханов Э.К.', 'Эльдарханову Э.К.', 3, false, false, 'W', false);
insert into WORKER(name, dative_name, access_group, right_issuing, driver, type, operational_rights)
values ('Полуэктов Д.А.', 'Полуэктову Д.А.', 4, false, false, 'W', true);
insert into WORKER(name, dative_name, access_group, right_issuing, driver, type, operational_rights)
values ('Сударушкин Е.Ю.', 'Сударушкину Е.Ю.', 5, false, false, 'W', true);
insert into WORKER(name, dative_name, access_group, right_issuing, driver, type, operational_rights)
values ('Тихонин С.Н.', 'Тихонину С.Н.', 4, false, false, 'W', true);
insert into WORKER(name, dative_name, access_group, right_issuing, driver, type, operational_rights)
values ('Яшин В.В.', 'Яшину В.В.', 5, true, false, 'W', true);
insert into WORKER(name, dative_name, access_group, right_issuing, driver, type, operational_rights)
values ('Сергеев С.Ю.', 'Сергееву С.Ю.', 5, true, false, 'W', true);
insert into WORKER(name, dative_name, access_group, right_issuing, driver, type, operational_rights)
values ('Макаров Е.М.', 'Макарову Е.М.', 2, false, true, 'W', false);
insert into WORKER(name, dative_name, access_group, right_issuing, driver, type, operational_rights)
values ('Назарнц В.А.', 'Назарнц В.А.', 2, false, true, 'W', false);



insert into work_permit(number, start_date, end_date, issuing_date, short_name)
values ('7/100', '2024-01-01 09:00:00', '2024-01-01 09:30:00', '2024-01-15 17:00:00', 'РП Новоузенский ф.1048');
insert into work_permit(number, start_date, end_date, issuing_date, short_name)
values ('7/110', '2024-02-10 09:00:00', '2024-02-10 09:30:00', '2024-02-15 17:00:00', 'ТП-3440 - ТП-3401');
insert into work_permit(number, start_date, end_date, issuing_date,  short_name)
values ('7/120', '2024-03-01 09:00:00', '2024-03-01 09:30:00', '2024-03-11 17:00:00', 'Транзит ф.1048');
insert into work_permit(number, start_date, end_date, issuing_date,  short_name)
values ('7/130', '2024-04-11 09:00:00', '2024-04-12 10:30:00', '2024-04-15 17:00:00', 'РП Константа - КТП-3440');

insert into task(work_permit_id, string)
values (1, 'РП Новоузенский, РУ-10кВ, яч.№11,кабельный отсек яч.№11, КЛ-10кВ ф.1048');
insert into task(work_permit_id, string)
values (1, 'к ПС Новосоколовогорская, определение места повреждения КЛ-10кВ');
insert into task(work_permit_id, string)
values (2, 'ТП-3440, РУ-10кВ, яч.№5 КЛ-10кВ к ТП-3401, яч.№1, трассировка КЛ-6кВ');
insert into task(work_permit_id, string)
values (3, 'РП Транзит, РУ-10кВ, яч.№5 КЛ-10кВ ф.1048 к ТЭЦ-3, прокол и разрезание КЛ-10кВ');
insert into task(work_permit_id, string)
values (4, 'РП Констанка, РУ-10кВ, яч.№9,КЛ-10кВ к КТП-3440, определение места повреждения КЛ-10кВ');



insert into role_assigment(work_permit_id, worker_id, role)
values (1, 8, 'RESPONSIBLE');
insert into role_assigment(work_permit_id, worker_id, role)
values (1, 3, 'PRODUCER');
insert into role_assigment(work_permit_id, worker_id, role)
values (1, 3, 'ADMITTING');
insert into role_assigment(work_permit_id, worker_id, role)
values (1, 5, 'MEMBER');
insert into role_assigment(work_permit_id, worker_id, role)
values (1, 1, 'ISSUING');

insert into measure(work_permit_id, first_column, second_column, third_column)
VALUES (1, 'ТП-3340 РУ-10кВ', '1) Отключить ВН-10кВ', 'I,II с.ш-10кВ');
insert into measure(work_permit_id, second_column, third_column)
VALUES (1, '2) Включить ЗН-10кВ', 'яч.№1,3,4');
insert into measure(work_permit_id, first_column, second_column)
VALUES (1, 'ТП-3300 РУ-10кВ', '3) Отключить ВН-10кВ');
insert into measure(work_permit_id, second_column)
VALUES (1, '1) Отключить ВН-10кВ');

insert into technic(name)
values ('передвижная электролаборатория ГАЗ№А862ХР');
insert into technic(name)
values ('передвижная электролаборатория ГАЗ№Р580НР');
insert into technic(name)
values ('самоходная площадка обслуживания №В089ВА');

insert into price(name, cost, deleted)
values ('Определение места повреждения до 500м', 4700, false);
insert into price(name, cost, deleted)
values ('Трассировка КЛ-10кВ', 1800, false);
insert into price(name, cost, deleted)
values ('Испытание КЛ-10кВ', 2500, false);

insert into historical_price(price_id, cost, start_date)
values (1, 4700, '2024-01-01');
insert into historical_price(price_id, cost, start_date)
values (2, 1800, '2024-01-01');
insert into historical_price(price_id, cost, start_date)
values (3, 2500, '2024-01-01');

insert into report_entry(work_permit_id, historical_price_id, quantity, date_of_work)
VALUES (1, 1, 1, '2024-02-01');
insert into report_entry(work_permit_id, historical_price_id, quantity, date_of_work)
VALUES (1, 2, 1, '2024-02-02');
insert into report_entry(work_permit_id, historical_price_id, quantity, date_of_work)
VALUES (1, 3, 1, '2024-02-03');

INSERT INTO users(username, password, enable) VALUES('Marcelin', '$2a$10$IXYFEqw6zQ8g50uutN7kBOTaELEGaE6tY/lX24jpUSc5zLo4nyrxG', true);
INSERT INTO users(username, password, enable) VALUES('Bardonat', '$2a$10$O6zW6Lh2uvMwTfOWK09wAO66q4Bs8uCjA3jGRWswpWhiOJU9donCC', true);
INSERT INTO users(username, password, enable) VALUES('Jaguarde', '$2a$10$r/05Xb03ph4CR.u2IGhCZegdCNOWxOxBX4Dc6Vig/RyXf6bmbXSQ.', true);
INSERT INTO users(username, password, enable) VALUES('Persenan', '$2a$10$PDeXy1KcAazO3rC6gnSFK.euPwlaYxIQs9UAL8zfBA.EP9leS2LgG', true);
INSERT INTO users(username, password, enable) VALUES('Keenicen', '$2a$10$Kqr8BAiaevx6J62C/eejUOdiIpd76jzQGS4EgqJ5U9hh5nx1Slsjq', true);
INSERT INTO users(username, password, enable) VALUES('Umikomal', '$2a$10$IjC2FjndxU6ZkN8CWq9sreovZ3TjKxcES7F8br3lLktDxEAzxVGmu', true);
INSERT INTO users(username, password, enable) VALUES('Pyrilann', '$2a$10$rDxjElaPcmbhuMaR9HXvtexhNb/Vz.gp9QtRBpue2uVURL0ZhQ9hC', true);
INSERT INTO users(username, password, enable) VALUES('Tenerich', '$2a$10$l0O0IuGoPZJl/IwuvPQz3uj8Z5eJ3cSSP4Ik8MHDTUbYDJnEKPzFu', true);
INSERT INTO users(username, password, enable) VALUES('Colbyaak', '$2a$10$F/zVm/jWZD51IyBtJVEJ.eo2/rjZ1.12b505rOmf187EGJcsDxQiS', true);
INSERT INTO users(username, password, enable) VALUES('Liseldor', '$2a$10$Bvuou.ioOFyvR4xteHTZceHkxKjjZDf8/KFO43QRumpR6sNzPuqWa', true);
INSERT INTO users(username, password, enable) VALUES('Tonyeren', '$2a$10$2XeJXmbyPGfDgMHQ1xhK2.u4GE36OQDZkTgN6Ke8f.OQczCWC29ji', true);
INSERT INTO users(username, password, enable) VALUES('Syaakovi', '$2a$10$0PBNIy9s43QBP4ATGqlpLO0MuJ3hM8hhn5DIzHPRG0uVwhqQ7IlAG', true);

INSERT INTO authority(username, authority) VALUES('Marcelin','read');
INSERT INTO authority(username, authority) VALUES('Bardonat','read');
INSERT INTO authority(username, authority) VALUES('Jaguarde','read');
INSERT INTO authority(username, authority) VALUES('Persenan','read');
INSERT INTO authority(username, authority) VALUES('Keenicen','read');
INSERT INTO authority(username, authority) VALUES('Umikomal','read');
INSERT INTO authority(username, authority) VALUES('Pyrilann','read');
INSERT INTO authority(username, authority) VALUES('Tenerich','read');
INSERT INTO authority(username, authority) VALUES('Colbyaak','read');
INSERT INTO authority(username, authority) VALUES('Liseldor','read');
INSERT INTO authority(username, authority) VALUES('Tonyeren','read');
INSERT INTO authority(username, authority) VALUES('Syaakovi','read');




