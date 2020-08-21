DROP TABLE IF EXISTS Booking;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Car;

DROP SEQUENCE IF EXISTS CUSTOMER_ID_SEQ;
DROP SEQUENCE IF EXISTS BOOKING_ID_SEQ;

CREATE SEQUENCE CUSTOMER_ID_SEQ;
CREATE SEQUENCE BOOKING_ID_SEQ;

CREATE TABLE Customer (ID IDENTITY NOT NULL PRIMARY KEY,
                                         FIRST_NAME VARCHAR(35) NOT NULL,
                                         LAST_NAME VARCHAR(35) NOT NULL,
                                         SOCIAL_SECURITY_NUMBER LONG NOT NULL UNIQUE
);

CREATE TABLE Car (PLATE_NUM VARCHAR(10) PRIMARY KEY,
                                    MODEL VARCHAR(15) NOT NULL,
                                    YEAR INT NOT NULL,
                                    PRICE DECIMAL(6,2) NOT NULL
);

CREATE TABLE Booking (ID IDENTITY NOT NULL PRIMARY KEY,
                                        CUSTOMER_ID INT NOT NULL,
                                        PLATE_NUM VARCHAR(10) NOT NULL,
                                        FROM_DATETIME TIMESTAMP NOT NULL,
                                        TO_DATETIME TIMESTAMP NOT NULL,
                                        CONSTRAINT FK_Booking_Customer FOREIGN KEY (CUSTOMER_ID) REFERENCES Customer(ID) ON DELETE CASCADE,
                                        CONSTRAINT FK_Booking_Car FOREIGN KEY (PLATE_NUM) REFERENCES Car(PLATE_NUM) ON DELETE CASCADE
);

