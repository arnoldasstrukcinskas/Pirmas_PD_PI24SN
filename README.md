# Practical assesment
## Table of Contents
- [About](#about)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Downloading project](#downloading-project)
- [Launching](#launching)
- [Database Testing](#database-testing)
- [Overall Testing](#overall-testing)
## About
  The application is developed as a practice project. The system is designed as drone warehouse/shop system management between H2 database and XML file.  
## Features
- Register/Login (Very basic)
- Manipulating data from and into XML file
- Manipulating data from and into H2 database
- Compatibility with Docker(There are exceptions)
## Tech Stack
| Technology | Purpose |
|------------|---------|
| Spring Boot 4.0.3(Maven) | System Framework |
| Java 17 | Programming language |
| Swagger | API documentation + JavaDoc |
| H2 | Database |
| Docker | Components containerization |

## Installation
1. Whole project can be containerized and created using containers so firstly you need to install docker engine:
!Important note if u want to use this application on docker there will be no funcitonality on writing files into XML.
For launching on docker you have:
  1. Uncomment docker and postgre dependencies.
  2. Change dabase configuration in application.properties(just comment H2 and uncomment postgre)
  3. And makes some comments/uncomments in ServerRepository(Marked lines what to do)
     
Link for docker: https://docs.docker.com/engine/install/

## Downloading project
For project to launch firstly need to download project or clone it from repository.
#### 1. Download
- Go to this repository: https://github.com/arnoldasstrukcinskas/Pirmas_PD_PI24SN
- Then press green button Code and and Download Zip.
- Unpack zip file
- Move to [Launching](#launching)
#### 2. Clone(If you have git)
- Open Terminal and go to directory you want to clone project(add your own directory)
  ```bash
  cd D:\example
  ```
- In terminal use this command:
  ```bash
  git clone https://github.com/arnoldasstrukcinskas/Pirmas_PD_PI24SN
  ```
- Move to [Launching](#launching)
  
## Launching (Bash)
If you do not have Maven installed into your computer you can launch it via powershell:
#### 1.Build project:
```bash
./mvwn clean package
```
#### 2. Build project: If you want to skip tests
```bash
./mvnw clean package -DskipTests
```
#### 3. Launch project(with logs)
```bash
./mvnw.cmd spring-boot:run
```
#### 4. Stop project
```bash
ctrl + c
```
## Launching (Docker)
#### 1. Firstly build project
```bash
./mvwn clean package
```
##### Without tests
```bash
./mvnw clean package -DskipTests
```
#### 2. Launch project
```bash
docker-compose up --build
```
#### 3. Stop project
```bash
ctrl + c
```
##### With deleting containers
```bash
docker-compose down
```

## Database Testing
(How to connect for testing)
#### 1. Open browser and go to:
```bash
http://localhost:8080/h2-console
```
#### 2. Connect to database(Enter container ID or name)
```bash
JDBC URL: http://localhost:8080/h2-console/login.jsp?jsessionid=3aaf8a0277f2faa50633cc880e28bcdb
--------------------------------
User name: sa
(Password must be clear)
```

## Overall Testing
(How to connect for testing)
### For testing - some demo data is created in XML file
#### 1. Links for testing via front/back
```bash
  Go to: [http://localhost:808/login](http://localhost:8080/swagger-ui/index.html)
```
##### FOR TESTINS FIRSTLY YOU HAVE TO START SERVER(/serverStart)
##### FOR FURTHER TESTING YUO WILL HAVE TO REGISTER(/register) AND THEN LOGIN(/login)
#### 3. Last bot not least
```bash
HAVE FUN!
```
