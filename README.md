# Robotics
Jayway Robotics Manager

The assignment technical file is 

## Requirements
(Open) JDK 14

Maven 3 (https://maven.apache.org/)

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


### Unit Testing
There are 27 unit tests distributed into three testing packages:

#### Controller Testing
- CustomerControllerTest: which tests the API correctness using MockMvc. It also tests exception handling. 








