INSERT INTO CUSTOMER VALUES (1, 'Jane', 'Doe', 198101021100);
INSERT INTO CUSTOMER VALUES (2, 'John', 'Doe', 199105072211);

ALTER SEQUENCE CUSTOMER_ID_SEQ RESTART WITH 3;

INSERT INTO CAR VALUES ('NPS151', 'BMW', 2018, 90);
INSERT INTO CAR VALUES ('TQS121', 'AUDI', 2017, 80);
INSERT INTO CAR VALUES ('MUV171', 'MAZDA', 2016, 70);
INSERT INTO CAR VALUES ('HQP191', 'VOLVO', 2020, 120);

INSERT INTO BOOKING VALUES (1, 2, 'MUV171', '2020-08-07 08:00:00', '2020-08-11 08:00:00');
INSERT INTO BOOKING VALUES (2, 1, 'TQS121', '2020-08-01 15:00:00', '2020-08-10 11:00:00');

ALTER SEQUENCE BOOKING_ID_SEQ RESTART WITH 7;
