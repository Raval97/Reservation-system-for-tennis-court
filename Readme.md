# Web Application for: <br/> Reservation system for tennis court

## Heroku link:
   [https://tennis-court-reservation.herokuapp.com/ourTennis](https://tennis-court-reservation.herokuapp.com/ourTennis) 

## Technologies:

    -Java: Spring Boot
    -Frontend: HTML + CSS + Jquery with using Thymleaf
    -database: MySQL/PostgreSQL(*)
    
    -application use WebSecurityConfigurerAdapter to session 
     maintenance and recognition of the logged in user's access type
    * postgres database is used on branch heroku to compatibility with Heroku platform,
      on branch master is used MySQL

    
## Example Of Working

### User: Default (for a user who is not logged in)
    Homepage & Price-List & Events Page 
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/1.png?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/2.png?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/3.png?raw=true)

    Register & Login page
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/4.png?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/21.png?raw=true)


### User: Client  (available only after login verification))
    Tournaments & Events
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/5.png?raw=true)

    Reservation Page 
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/6.png?raw=true)

    Page of summary of Reservation before confirm it.
    Here, the user specifies whether to rent additional equipment and the form of payment
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/7.png?raw=true)

    User Account: Personal Data 
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/8.png?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/9.png?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/10.png?raw=true)

    User Account: Reservation of user
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/11.png?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/12.png?raw=true)

    User Account: Payment
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/13.png?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/14.png?raw=true)

    User Account: Club Association
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/15.png?raw=true)


### User: Admin
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/16.png?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/17.png?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/18.png?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/19.png?raw=true)


### Databse Structure (Diagram ERD) 

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/database.png?raw=true)
