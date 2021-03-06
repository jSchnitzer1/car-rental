# Car Rental Manager
Infor Car Rental Manager - With System Testing

The assignment technical file is [here](https://github.com/jSchnitzer1/car-rental/blob/master/Assignment.pdf "Assignment file")


## Dockerized
The project is also dockerized at my dockerhub. You can pull the image using ``docker pull jschnitzer1/car-rental-api:latest``

The **'Build & Run Docker Image'** section describes more....

## Requirements
(Open) JDK 14

Spring Boot 5

Maven 3 (https://maven.apache.org/)

Docker Engine (https://docs.docker.com/engine/install/)

An IDE (VS Code or IntelliJ)

## System composition 

### car-rental-api (Backend)
#### Controller Package
contains:
- CustomerController: has the API endpoint manages customers using ```/customers``` API with all required http validations
- CarController: has the API endpoint that manage cars using ```/cars``` API with all required http validations
- BookingController: has the API to manage bookings and select different reports using ```/bookings``` API with all required http validations

Supported reports including: 
- Find All Bookings Between Two DateTimes
- Find All Bookings Between Two DateTime Per Hour
- Find All Bookings Total Payment Per Each Booking
- Find Available Cars To Be Rented

#### Exception Package
contains:
- AppExceptionHandler: Application level exception handler, which handles also bad requests and internal server errors and all remaining exceptions
- BadRequestException: Exception handler that catches the invalid requests 
- EntityExistsException: Exception handler that handles creating an entity which already exists.
- EntityNotFoundException: Exception handler that handles wrongly selected entities. 
- LongPeriodException: Exception handler that handles searching for long period.
- RootException: An abstract class that works as a strategy design pattern for other Exception classes
- ErrorCode: Enumerator that produces error code and types
- ErrorMessage: A class for error message used in previous exception handlers 

#### Model Package
H2 database entities including required validations. This package contains: 
- Customer entity: that contains Id, firstName, lastName, and socialSecurityNumber properties.
- Car entity: that contains plate number, car model, year, and price per hour.
- Booking entity: that contains Id, Custoemr entity, Car entity, fromDateTime, toDateTime properties.  

#### DTO Package
contains:
- BookingDto: Simulation for booking entity to be transferred between Client and API
- CarDto: Simulation for Car entity object to be transferred between Client and API
- CustomerDto: Simulation for Customer entity object to be transferred between Client and API

#### Repository Package
This package contains all HQL queries as they extend from JpaRepository. 
This package includes:
- CustomerRepository
- CarRepository
- BookingRepository

#### Service Package
contains
- CustomerService: a business logic to find customers (also by their id), create and delete customers including all required validations
- CarService: a business logic to find car (also by their plate number, model, year, price, available cars to be rented), create and delete cars including all required validations.
- BookingService: a business logic to find all bookings, generate different kind of reports including:
  - Report of booked cars from date/time, to date/time
  - Report of number of booked cars per hour from date, to date
  - Report of total payment from date, to date

### H2 Database
![Database](https://github.com/jSchnitzer1/car-rental/blob/master/data_structure/car-rental.jpg)

Note: H2 Database is selected for this project as it can run in the memory without any prerequisite such as installing SQL engine.

The database is designed and implemented using H2 in-memory database. In the resources folder, there are two files:
1.  schema.sql: a file to be executed by Spring boot to create and inject the database into the project.
2. data.sql: a file to initialize some data into Customer, Car and Booking tables.

Note: the source design is [here](https://github.com/jSchnitzer1/car-rental/blob/master/data_structure/car-rental.drawio "draw.io DB file") 


### Unit Testing
There are **45 unit tests** distributed into three testing packages:

#### Controller (API) Testing
- CustomerControllerTest: which tests the ```/customers``` API correctness using MockMvc. It also tests different scenarios and exception handling. 
- CarControllerTest: which tests all ```/cars``` endpoints correctness including object creation using MockMvc. It also tests different scenarios and exception handling. 
- CarControllerTest: which tests different report generation in ```/bookings``` endpoints and their correctness. Furthermore, it tests creating a new booking using MockMvc. It also tests different scenarios and exception handling. 

#### Repository Testing
Tests the H2 database and HQL queries. 
- CustomerRepositoryTest
- CarRepositoryTest
- BookingRepositoryTest

#### Services Testing
This package tests all business logic of the renting car system. 
- CustomerServiceTest
- CarServiceTest
- BookingServiceTest

### system-test (System Test)
The system test uses WebClient (in Spring Boot 5) as a reactive, non-blocking interface to call ```car-rental-api``` endpoints so that we can ensure there is no blocking occurred while executing the code.

Furthermore, the project uses concurrent method calls (as tasks) using ```ExecutorService``` to simulate real world scenarios 

## Run the application
Make sure you are in the project home folder path ``` car-rental/ ```

### Running Backend Car API
```
$ sh run-backend.sh
```

### Running System Test
```
$ sh run-system-test.sh
```

### Running All Unit Tests (45 Tests)
```
$ sh run-all-tests.sh
```

Notes:
- Please follow the execution as shown previously. 
- Each shell command should be executed in a different terminal tab (one for running the web service and the second one for running the Angular App).

## Build & Run Docker Image
First of all, you need to ensure docker engine is installed in your OS. Please check: ``https://docs.docker.com/engine/install/`` for how to install it.
Then go to the project home directory using ``cd car-rental/``

**NOTE:** if you just want to pull the image and consume it, you can ignore the next two steps and move directly to **'Pull & Run API Image'** section

To be able to build the image, we need also to have the Dockerfile for the API module. 

### Building the docker image
In the ``docker_shells`` folder, just run the ``build-docker-image-for-api.sh`` file as the following:

```
$ sh docker_shells/build-docker-image-for-api.sh
```

### Push Into DockerHub
If you want to deploy this API as image in your DockerHub, you can check ``push-api-image-in-dockerhub`` file. 
Do not forget to change ``jschnitzer1`` (my DockerHub Account) to yours and then run the file:
```
$ sh docker_shells/push-api-image-in-dockerhub.sh
```

### Pull & Run API Image
As the image is pushed into DockerHub, you can pull and run the image:
```
$ sh docker_shells/download-and-run-dockerhub-api-image.sh
```

Now we can test the running image from PostMan (the image is running on external port of 9090 and the internal tomcat port is 8080)

### Run Docker Compose
You can also run docker compose file ``docker-compose.yaml`` as the following:
```
$ sh docker_shells/run-docker-compose.sh 
```

#### Testing Working Docker Image (at PostMan)
![Testing Dockerized API](https://github.com/jSchnitzer1/car-rental/blob/master/screenshots/test_api_from_running_docker.png)

## Nice-to-have for later (not implemented due to limited time)
- API access protection via api keys
- Deploy into Kubernetes
- Professional Frontend using Angular
- More tests such as integration and performance tests

## Screenshots

### All Testings 

#### Lunching All Tests
![Lunching All Tests](https://github.com/jSchnitzer1/car-rental/blob/master/screenshots/all_tests_1.png)

#### All Tests Results
![All Tests Results](https://github.com/jSchnitzer1/car-rental/blob/master/screenshots/all_tests_2.png)

### System Test

#### Concurrent Threads & Nonblocking Requests
![Example_1](https://github.com/jSchnitzer1/car-rental/blob/master/screenshots/system_test_1.png)

![Example_2](https://github.com/jSchnitzer1/car-rental/blob/master/screenshots/system_test_2.png)

