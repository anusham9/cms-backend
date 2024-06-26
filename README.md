# CLIENT MANAGEMENT SYSTEM 
During my time at a non-profit, I observed that our client data system had a serious backlog and inconsistencies in data entry because we manually entered their data from an intake form.

To resolve this issue, I developed a client and employee management system with role-based authentication so that clients have the agency to update their own information from their end,
and employees can approve the status of the client's profile if the client has filled out their intake correctly. 

A client has privileges to access their profile and modify their information. An employee can get a list of all clients and update the status of a client’s profile. 
The admin user can create an employee, get a list of all employees, and update an employee password. 

## Installation

For this project, I used spring intialzr to install the necessary spring-web-starter dependencies to create my project. I also set up Maven in my IntelliJ to help automate the building process.

I then installed MySQL Workbench to create a database called CMS. 

After initializing the project, in the application properties folder file under resources, I put the correct url to connect to the MySQL database in the application properties file.

I also recommend adding the following:

# /api-docs endpoint custom path
springdoc.api-docs.path=/api-docs

This is to generate the JSON representation of the API definitions. 

<img width="619" alt="Screenshot 2024-04-26 at 11 52 50 AM" src="https://github.com/anusham9/cms-backend/assets/148721336/0b4647c6-7794-4f83-a623-e94ea3880251">

After creating the database, I created a role table with three roles: ROLE_ADMIN, ROLE_EMPLOYEE, and ROLE_CLIENT. 

Each role is mapped to a different id:

<img width="321" alt="Screenshot 2024-04-26 at 11 44 44 AM" src="https://github.com/anusham9/cms-backend/assets/148721336/872bc9e7-0107-488c-8222-198d202dbf01">

I ran the Spring Boot application, which created all the schema and tables for my database from the entity classes. 

The Employee service layer creates the admin user in the @Post Configuration annotation. The user_roles table should map the user id (1) to the role id (1), like so:


<img width="328" alt="Screenshot 2024-04-26 at 11 47 39 AM" src="https://github.com/anusham9/cms-backend/assets/148721336/39d7ab94-4e10-49d7-b09e-8d275e6207ce">

If the table is not updated, you can insert these values directly into it. Make sure that the admin's user ID is mapped to the role ID. 


## Technologies
In this project, I used Java Spring Boot and MySQL to store client and employee information and map each user to their respective roles. 

To get a JSON representation of the API definition, you can go to http://localhost:8080/api-docs. This gives an easy accessible UI for understanding and visualizing the REST APIs in this project. 

You can then use a swagger editor and paste the JSON file for readability of the file: https://editor.swagger.io/.


<img width="1431" alt="Screenshot 2024-04-26 at 4 28 48 PM" src="https://github.com/anusham9/cms-backend/assets/148721336/33b803a0-fa9a-4563-b786-644c85a89b1e">



### Dependencies / Libraries

Please add the following dependency to the pom.xml file:

<img width="515" alt="Screenshot 2024-04-26 at 4 30 03 PM" src="https://github.com/anusham9/cms-backend/assets/148721336/b522ac3f-7764-409f-9e16-4cebdf8b30af">



Besides the OpenAPI dependency, the pom.xml file should include all other necessary dependencies, but if not, use the spring initializer to install the Spring Security, Spring Security Test, Spring Web, Spring Data JPA, and MySQL driver dependencies. 

Be sure to include Lombok as well, as I have used those annotations to reduce boilerplate code. 


