## Spring Boot File Upload - Download Rest API Example

**Description**:

## Setup

**1. Clone the repository**

```bash
git clone https://github.com/JoDeMiro/Micado-Optimizer-Test.git
```

**2. Specify the file uploads directory**

Open `src/main/resources/application.properties` file and change the property `file.upload-dir` to the path where you want the uploaded files to be stored.

```
file.upload-dir=/uploads
```

**2. Run the app using maven**

```bash
cd spring-boot-file-upload-download-rest-api
mvn spring-boot:run
```

The application can be accessed at `http://localhost:8080`.

You may also package the application in the form of a jar and then run the jar file like so -

```bash
mvn clean package
java -jar target/file-demo-0.0.1-SNAPSHOT.jar
java -jar target/file-demo-0.0.1-SNAPSHOT.jar --server.port=8080
java -jar target/file-demo-0.0.1-SNAPSHOT.jar --server.port=8080 --name="MyBeanName"
```

## New | Future | Notes

New application.properties 'name' has been connected to the the com.example.beans.MyBean name property field.
Via this procedure user can initialize class properties from command line. [this procedure will be developed
in the future]

Setup 'com.example.beans.MyBean.class' name field via command line
```bash
java -jar target/file-demo-0.0.1-SNAPSHOT.jar --server.port=8080 --name="MyBeanName"
```

This Bean is accessible via http://localhost:{port}/helloMyBean

## Test it

**1. with Post Man**

- Download and install PostMan [download](https://www.postman.com/downloads/)
- copy /images/image.jpg to C:\Users\[user]\Postman\files
- Please follow the steps listed below

![setup_postman](./images/PostManTest.png?raw=true)

**1. with Post Man for multiple files**

![setup_postman](./images/PostManMultipleTest.png?raw=true)


**2. with Apache JMeter**

- Download and install Apache JMeter [download](https://jmeter.apache.org/)
- copy /images/image.jpg to JMeter/bin folder
- Please follow the steps listed below or load the /jmeter/bin/Localhost1.jmx

![setup_jmeter](./images/JMeterTest.png?raw=true)

**2. with Apache JMeter for multiple files**

![setup_jmeter](./images/JMeterMultipleTest.png?raw=true)

**3. with cURL**

- For Windows donwnload the curl application [download](https://curl.se/windows/)
- copy /images/image.jpg to curl/bin folder
- cd curl/bin

```bash
curl -F "file=@image.jpg" localhost:8080/uploadFile
```

```bash
curl -F "files=@1.jpg" -F "files=@2.jpg" -F "files=@3.jpg" localhost:8080/uploadMultipleFiles
```

## REST API

**Wait (fix 1000 ms)**

```bash
curl -v localhost:8080/wait
```

**Wait {time in milisec}**

```bash
curl -v localhost:8080/wait/{time}
```

**Fibonacci [cpu] {int number}**

number should be less than 30 (it takes least one second to get the response).

```bash
curl -v http://localhost:8080/cpu/{number}
```

**Directory size [io] {int number}**

number is unconnected - but necessary for later use (ToDo: param is hardcoded).

```bash
curl -v http://localhost:8080/io/2/{number}
```


## ToDo

Hogyan lesz szabályozható az, hogy hány szálat engedélyezzen a spring thread.pool!




## Help

Spring Boot file upload rest api example [link](https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/)

JMeter JSON Extractor [link](https://www.blazemeter.com/blog/api-testing-with-jmeter-and-the-json-extractor)

JMeter JSON Extractor Plugin [link](https://www.blazemeter.com/blog/how-to-use-the-json-plugin-in-jmeter)

JMeter JSON Testing Example [link](https://octoperf.com/blog/2018/04/23/jmeter-rest-api-testing/#enabling-debug)

JMeter Understanding the results [link](https://chamikakasun.medium.com/rest-api-load-testing-with-apache-jmeter-a4d25ea2b7b6)

JMeter Timer Explained [link](https://www.blazemeter.com/blog/comprehensive-guide-using-jmeter-timers)

JMeter Constant Throughput Timer [link](http://jmeter.apache.org/usermanual/component_reference.html#Constant_Throughput_Timer)

JMeter Run Thread Groups sequentially - [link](https://newbedev.com/running-multiple-thread-groups-sequentially-in-jmeter)

SSH Connection With Java [link](https://www.baeldung.com/java-ssh-connection)

Spring Boot - Overriding one application.property from command line [link](https://docs.spring.io/spring-boot/docs/1.4.1.RELEASE/reference/html/boot-features-external-config.html)

Command-Line Arguments in Spring Boot