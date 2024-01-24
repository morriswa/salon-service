# EECS 447 Term Project
## Authors:
- William A. Morris (morriswa) [morris.william@ku.edu]
- Makenna B. Loewenherz (makennabrynn) [makennaloewenherz@ku.edu]
- Kevin Rivers (Kabuto1357) [kevin.rivers14832@ku.edu]

## Why Microservice?
The purpose of this application is to provide a RESTful Web Service
that can be consumed by a UI client, rather than accessing the 
MySQL Database directly. 

This was done for several reasons. <br>
All frameworks support HTTP requests so providing a web service
maximizes compatibility with front-end frameworks (Including Vanilla JS). 
Application stability and security can be enhanced by providing a 
layer between the UI and DB that abstracts away business logic 
and all sensitive configuration information.
Additionally, thanks to the rich Java community, this Microsevice
can take advantage of useful libraries to perform important operations
more easily.<br>
These libraries include:
- Java Database Connectivity Library (JDBC)
  - provides several classes to easily connect to, query, and update
    an SQL databse.
- Spring Framework
  - provides classes to create RESTful Web Services
  - provides a Security Filter that can be extensively configured
    to secure Web Services
  - provides a test framework to write Unit Tests
- Flyway Community by Redgate
  - automatically manages database migrations
  - maintains information about the database's history
  - ensures connected database is running up-to-date schema
  
## Purpose
### Business Owners
This service will provide an API for business owners to manage
their employees and provided services.
### Clients
This service will provide an API for clients to book services 
with employees and make payments.