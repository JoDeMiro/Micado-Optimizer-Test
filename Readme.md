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
java -Xms512m -Xmx1024m -jar target/file-demo-0.0.1-SNAPSHOT.jar --server.port=8080 --name="MyBeanName"
```

Override the Apache Tom Cat Web Server Thread Pool Size

```bash
java -Xms512m -Xmx1024m -jar target/file-demo-0.0.1-SNAPSHOT.jar --server.port=8080 --name="MyBeanName" --server.tomcat.max-threads=1
```

```bash
java -Xms512m -Xmx1024m -jar target/file-demo-0.0.1-SNAPSHOT.jar --server.port=8080 --name="MyBeanName" --server.tomcat.max-threads=1000
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

**String length size [memory] {int number}**

```bash
curl -v http://localhost:8080/memory/string/1/{number}
```

**MyBean list filler [memory] {int number}/{boolean withGC}**

Every time creates a new instance when GET request is called

```bash
curl -v http://localhost:8080/memory/beans/1/{number}/{boolean}
```

Create a final singleton instance when application started

```bash
curl -v http://localhost:8080/memory/beans/2/{number}/{boolean}
```

**GenerateNetworkTraffic [network] {int number}/{boolean withGC}**

Every time creates a new instance when GET request is called

```bash
curl -v http://localhost:8080/network/1/{number}/{boolean}
```

Create a final singleton instance when application started

```bash
curl -v http://localhost:8080/network/2/{number}/{boolean}
```

## REST API - Singleton data access

**Network**

```bash
curl -v http://localhost:8080/network/2/
```


## REST API

**Memory Status (in MB)**

```bash
curl -v localhost:8080/memory-status
```

**Spring Info - See the maximum available threads in tomcat web server**

```bash
curl -v http://localhost:8080/info
```



## JMeter from command line

```bash
jmeter -n -t Localhost8.jmx -l testresult.jtl
```

**Super useful dashboard - testresult<id>.jtl should be unique from time to time**

```bash
jmeter -n -t Localhost9.jmx -l testresultXX.jtl -e -o C:\Users\lpds\Desktop\apache-jmeter-5.2.1\bin\export
```



## Good combination

java -Xms512m -Xmx1024m -jar target/file-demo-0.0.1-SNAPSHOT.jar --server.port=8080 --name="MyBeanName" --server.tomcat.max-threads=1

jmeter -n -t Localhost9.jmx -l testresultXX.jtl

Threshold_up = 100~300ms

jmeter -n -t Localhost8Ultimate.jmx -l testresultXX.jtl

Threshold_up = 100~300ms



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

Cikkhez - [link](https://programmer.group/two-concurrent-types-of-java-computing-intensive-and-io-intensive.html)

Spring Boot Setting a Request Timeout [link](https://www.baeldung.com/spring-rest-timeout)

Spring Boot Disable Caching [link](https://www.yawintutor.com/spring-boot-how-to-enable-and-disable-cache)

Spring Boot Set Memory [link](https://www.baeldung.com/spring-boot-heap-size)

Spring Boot Control the threads [link](https://spring.io/blog/2015/12/10/spring-boot-memory-performance)

JMeter run command line and read the results [link](https://blog.e-zest.com/how-to-run-jmeter-in-non-gui-mode/)

Spring Boot Memory Performance [link](https://spring.io/blog/2015/12/10/spring-boot-memory-performance)

JMeter command line tricks and tipps [link](https://www.blazemeter.com/blog/9-easy-solutions-jmeter-load-test-%E2%80%9Cout-memory%E2%80%9D-failure)

Spring Threads - ! - Super Useful [link](https://stackoverflow.com/questions/39002090/spring-boot-limit-on-number-of-connections-created)

Spring Application Properties [link](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)

Spring Kubernetes Install [link](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#deployment.cloud.kubernetes)


## Kubernetes

Read this [link](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#deployment.cloud.kubernetes)

```
spec:
  containers:
  - name: example-container
    image: example-image
    lifecycle:
      preStop:
        exec:
          command: ["sh", "-c", "sleep 10"]
```


## AWS

Read this [link](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#deployment.cloud.aws)

Read this [link](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#deployment.cloud.aws.beanstalk)

Test on AWS single jar [test it](https://exampledriven.wordpress.com/2017/01/09/spring-boot-aws-elastic-beanstalk-example)

