# CLIENT MANAGEMENT SYSTEM 
During my time at a non-profit, I observed that our client data system had a serious backlog and inconsistencies in data entry because we were manually entering their data from an intake form.

To resolve this issue, I developed a client and employee management system with role-based authentication so that clients have the agency to update their own information from their end,
and employees can approve the status of the client's profile if the client has filled out their intake correctly. 

A client has privileges to access their profile and modify their information. An employee can get a list of all clients and update the status of a client’s profile. 
The admin user can create an employee, get a list of all employees, and update an employee password. 

## Installation

For this project, I used spring intialzr to install the necessary spring-web-starter dependencies to create my project. I also set up Maven in my IntelliJ to help automate the building process.

I then installed MySQL Workbench to create a database called CMS. 

After initializing the project, in the application properties folder file under resources, I put the correct url to connect to the MySQL database. 

<img width="619" alt="Screenshot 2024-04-26 at 11 52 50 AM" src="https://github.com/anusham9/cms-backend/assets/148721336/0b4647c6-7794-4f83-a623-e94ea3880251">

After creating the database, I created a role table with three roles: ROLE_ADMIN, ROLE_EMPLOYEE, and ROLE_CLIENT. 

Each role is mapped to a different id, as so:

<img width="321" alt="Screenshot 2024-04-26 at 11 44 44 AM" src="https://github.com/anusham9/cms-backend/assets/148721336/872bc9e7-0107-488c-8222-198d202dbf01">

I ran the Spring Boot application, which created all the schema and tables for my database from the entity classes. 

The Employee service layer creates the admin user in the @Post Configuration annotation. The user_roles table should map the user id (1) to the role id (1), like so:
<img width="328" alt="Screenshot 2024-04-26 at 11 47 39 AM" src="https://github.com/anusham9/cms-backend/assets/148721336/39d7ab94-4e10-49d7-b09e-8d275e6207ce">

If the table is not updated, you can insert these values directly into it. Make sure that the admin's user ID is mapped to the role ID. 


## Technologies
In this project, I used Java Spring Boot and MySQL to store client and employee information and map each user to their respective roles. 

### Dependencies / Libraries
The pom.xml file should include all necessary dependencies, but if not, use the spring initialzr to install the Spring Security dependency, Spring Security Test, Spring Web dependency, Spring Data JPA dependency, and MySQL driver dependency. 
Be sure to install Lombok as well, as I have used those annotations to reduce boilerplate code. 


