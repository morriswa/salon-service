# Salon Web Service
### EECS 447 Term Project
## Authors:
- William A. Morris (morriswa) [morris.william@ku.edu]
- Makenna B. Loewenherz (makennabrynn) [makennaloewenherz@ku.edu]
- Kevin Rivers (Kabuto1357) [kevin.rivers14832@ku.edu]

## Why Microservice?
The purpose of this application is to provide a RESTful Web Service
that can be consumed by a UI client, rather than accessing the 
MySQL Database directly. 

This was done for several reasons. <br>
All frontend frameworks support making HTTP requests so providing a web service
maximizes compatibility with front-end frameworks (Including Vanilla JS). 
Application stability and security can be enhanced by providing an additional
layer between the UI and DB that abstracts away business logic 
and all sensitive configuration information.
Additionally, thanks to the rich Java community, this Microservice
can take advantage of useful libraries to perform important operations
more easily.<br>
These libraries include:
- Java Database Connectivity Library (JDBC)
  - provides several classes to easily connect to, query, and update
    an SQL database.
- Spring Framework
  - provides classes to create RESTful Web Services
  - provides a Security Filter that can be extensively configured
    to secure Web Services
  - provides a test framework to write Unit Tests
- AWS SDK
  - provides an easy interface for interacting with AWS services
  - used in this application to store/retrieve/update data from S3 (Simple Storage Service)
  
## Purpose
### Business Owners
This service will provide an API for Salon owners to manage
their employees, manage the services they provide to clients, and utilize a (mock) payroll system.
### Employees
This service will provide an API for Salon employees to provide services to clients,
manage their schedule, and collect (mock) paychecks and tips.
### Clients
This service will provide an API for clients to book Salon services 
with employees and make (mock) payments.