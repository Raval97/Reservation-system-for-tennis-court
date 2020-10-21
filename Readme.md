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
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/1.PNG?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/2.PNG?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/3.PNG?raw=true)

    Register & Login page
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/4.PNG?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/20.PNG?raw=true)


### User: Client  (for a user who is logged in)
    Tournaments & Events
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/5.PNG?raw=true)

    Reservation Page 
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/6.PNG?raw=true)

    Page of summary of Reservation before confirm it.
    Here, the user specifies whether to rent additional equipment and the form of payment
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/7.PNG?raw=true)

    User Account: Personal Data 
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/8.PNG?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/9.PNG?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/10.PNG?raw=true)

    User Account: Reservation of user
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/11.PNG?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/12.PNG?raw=true)

    User Account: Payment
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/13.PNG?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/14.PNG?raw=true)

    User Account: Club Association
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/15.PNG?raw=true)


### User: Admin (user who has administrations permissions)
![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/16.PNG?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/17.PNG?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/18.PNG?raw=true)

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/19.PNG?raw=true)


### Databse Structure (Diagram ERD) 

![Image description](https://raw.githubusercontent.com/Raval97/Reservation-system-for-tennis-court/master/src/main/resources/images/readmeImages/database.PNG?raw=true)
