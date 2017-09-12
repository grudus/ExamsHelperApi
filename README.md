# ExamsHelperApi
[![Build Status](https://travis-ci.org/grudus/ExamsHelperApi.svg?branch=master)](https://travis-ci.org/grudus/ExamsHelperApi)

REST api for [angularjs website](https://github.com/grudus/ExamsHelperWebsite)

ExamsHelper is an application which helps you to store information about all your incoming and already written exams.

Here is the api for that app - all informations are stored on the server and client can synchronize all data for many devices.

**ExamsHelperWeb** uses Spring framework with Spring Boot basic configuration. It works using MySQL database (via JOOQ framework).

## Deployment

1) Download project 
````bash
git clone https://github.com/grudus/ExamsHelperApi.git 
````

2) Create 2 MySQL databases - `examshelper` and `examshelper_test`

3) Copy values from `templates` directory

- Copy values from `ExamsHelperApi/src/main/resources/templates/settings.xml.template` into your `.m2/settings.xml` and set your db variables, e.g.

````xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <profiles>
    <profile>
      <id>PROFILE</id>
      <properties>
        <db.url>jdbc:mysql://localhost:3306/examshelper</db.url>
        <db.username>root</db.username>
        <db.password>root</db.password>
      </properties>
    </profile>
  </profiles>

  <activeProfiles>
    <activeProfile>PROFILE</activeProfile>
  </activeProfiles>

</settings>
````
- Copy file `ExamsHelperApi/src/main/resources/templates/email.properties.template` into `ExamsHelperApi/src/main/resources/email.properties` and fill `username` and `password` fields (it will be necessary to send confirmation emails)

- Copy file `ExamsHelperApi/src/main/resources/templates/application.properties.template` into  `ExamsHelperApi/src/main/resources/application.properties`, e.g.

- Replace values in `ExamsHelperApi/src/test/resources/test.properties` with your own db data

````properties
token.secret=some secret token
url.base=http://localhost:8080

spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.url=jdbc:mysql://localhost:3306/examshelper
````

4) Go to downloaded directory and install maven project
````cd ExamsHelperApi && mvn clean install````
5) You can fire `setup.sh` script to get some basic users / exams
````bash 
./src/main/resourcesscripts/setup.sh DB_USERNAME DB_PASSWORD
````
(replace `DB_USERNAME` and `DB_PASSWORD` with your actual data)

6) Run it via your IDE or from maven
````bash
mvn spring-boot:run
````

Voilà! Your app is working now.

