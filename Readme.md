## Telepítés utáni első teendők - Amelyek az üzemeltetéshez elengedhetetlenek

**IP cím alapú védelem**:

A programot úgy írtam meg, hogy a Load Balanceren a cloud init alapú telepítésnél úgy konfigurálja a Apache servert, hogy csak egy bizonyos IP címről, vagy IP cím tartományból lehet elérni. Erre azért van szükség, mert ha publikus IP címre teszem a Load Balancert akkor sok kártékony behatolási kísérlet keletkezik, amelyeket a log fájl is regisztrál és tévesen megzavarhatják a mérési eredményeket - ezért ezek szűrése és kizárása szükséges.

Ugyanakkor ezáltal a telepítés után szükség van arra, hogy ebben a konfiugációs fájlban manuálisan felülírjuk azt az IP címet amelyen keresztül el akarjuk érni a szolgáltatást. Ha ez a saját gépünk és dinamikus IP cím kiosztással rendelkező internet szolgáltatón keresztül érjük el az internetet akkor ez időnként változhat. Ezért ellenőrizni kell a saját IP címunket és ezt megadni a megfelelő konfigurációs fájlban amely:

```
/etc/apache2/sites-available/loadbalancer.conf
```

elérési úton található és az alábbi részt kell benne átírni, hogy kívűlről is elérhető legyena  szolgáltatás

```
<Location />
    Order Deny,Allow
    Deny from all
    Allow from 87.41.23.231
</Location>
```

**Workerek hozzáadogatása**:

Bár meg tudnám csinálni, hogy a Workerek maguk jelezzék a Load Balancer felé, hogy léteznek és működnek, és a Load Balancer regisztrálja be öket a Klaszterba, itt most nem ez volt az elsődleges cél, ezért a Workereket manuálisan kell a rendszerhez adni IP cím alapján a

```
/etc/apache2/sites-available/loadbalancer.conf
```

fájlban.

```
        <Proxy balancer://backend-cluster>
                # Ide kell beírni a Workerek belső hálózati ip címét
                BalancerMember http://192.168.0.200:8080
                BalancerMember http://192.168.0.86:8080
                ...
        </Proxy>
```

**.htaccess védedelem**:

A `http://ip-address/server-status` oldalt .htaccess, .htpasswd kombinációval védtem le.

## A Load Balancer belépési pontjai

`http://ip-address/server-status`

`http://ip-address/balancer-manager`

`http://ip-address/actuator/health`

További információért bele kell nézni a <a href="https://github.com/JoDeMiro/Micado-Optimizer-Test/blob/main/loadbalancer.conf">loadbalancer.conf</a> filéba.


## Spring Boot File Upload - Download Rest API Example

**Description**:

This is my first Java Spring Boot application, which can be used in a distributed way. I wrote this application for testing my PhD thesis - Autoscaling with Deep Reinforcment and Semi-Supervised Learning - A comparision - Theory and Practice.

The App can be used as a stand-alone app or can be run behind a loadbalancer.

## Setup with Apache2 loadbalancer

**Note**:
Prefork vs Worker multi process modul distictions, differences:
https://stackoverflow.com/questions/13883646/apache-prefork-vs-worker-mpm

**1. Create Worker VMs with cloud_init**

cloud_init_for_workers.txt is a good starting point. The cloud_init install, setup every dependecies and run the Java SpringBoot App. The App is avaliable at the given URI address (for example.: http://localhost:8080 or http://your-ip-address:8080)

**2. Setup the Apache2 loadbalacer**

Create a VM with cloud_init_loadbalancer.txt

Write your worker ip addresses into the /etc/apache2/sites-available/loadbalancer.conf file

```bash
sudo nano /etc/apache2/sites-available/loadbalancer.conf
```

Restart the Apache2 service
```bash
sudo systemctl restart apache2
```

To get the metrics from worker if the cloud_init_worker install was succed
```
ssh -A ubuntu@192.168.0.xxx tail -n 10 mylog.log | grep '[0-9]' | sed 's/ \+/ /g' | cut -d ' ' -f '2-5,8-' | awk '{for (i=1;i<=NF;i++){a[i]+=$i;}} END {for (i=1;i<=NF;i++){printf "%f ", a[i]/NR;}}'
```
The UI of the Loadbalancer is available at http://host/balancer-manager or http://host-ip/balancer-manager

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
java -Xms512m -Xmx1024m -jar target/file-demo-0.0.1-SNAPSHOT.jar --server.tomcat.max-threads=1
java -Xms512m -Xmx1024m -jar target/file-demo-0.0.1-SNAPSHOT.jar --server.tomcat.max-threads=1000
```

If WaveFront is installed - otherwise it will not work - Api token should be replaced!

```shell
java -Xms512m -Xmx1024m -jar target/file-demo-0.0.1-SNAPSHOT.jar ^
 --server.port=8080 ^
 --name="MyBean is multiplied" ^
 --server.tomcat.max-threads=1 ^
 --wavefront.application.name="micadoSecond" ^
 --wavefront.application.service="EndpointSecond" ^
 --management.metrics.export.wavefront.api-token=fdd01257-fdb4-4cad-a9d3-70682*******
```

## Monitoring

git clone https://github.com/aristocratos/bashtop
cd bashtop
bash bashtop


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

**Health check**

```bash
curl -v localhost:8080/actuator

curl -v localhost:8080/actuator/health
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

**Directory size [io] {string dir_path}{int number}**

number is the number of the executed iteration

```bash
curl -v http://localhost:8080/io/2/{dir_path}/{number}
```

**Copy files [io] {int times}**

Copy and save a new file n times

```bash
curl -v http://localhost:8080/io/copy/{times}
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

**Queue [queue] (experimental)**

Implements queue theory. It will not responses until previous request has not been processed.
- queue length, - max thread, - threadPoolSize, keepAliveTime will be adjustable.


**Queue LongPolling [queue] (experimental)**

This method can handle request parallel until the request number reach max.thread set in Tom Cat conf.

```bash
curl -v http://localhost:8080/queue/bake4/{string bakedGood}/{int bakeTime}
```

This method does not handle the request parallel but standing them into a queue.

```bash
curl -v http://localhost:8080/queue/bake4/{string bakedGood}/{int bakeTime}
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

Spring AWS with Boxfuse [link](https://boxfuse.com/blog/spring-boot-ec2.html)

Spring Actuator - Super Useful [link](https://howtodoinjava.com/spring-boot/actuator-endpoints-example/)

Spring Endpoints plus Prometheus [link](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints)

Spring Boot Deployment on Cloud [link](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment.html#deployment)

Spring Boot Maven Plugin [link](https://docs.spring.io/spring-boot/docs/2.5.5/maven-plugin/reference/htmlsingle/)

Spring point to resources folder tutorial [link](https://stackoverflow.com/questions/44399422/read-file-from-resources-folder-in-spring-boot)

Spring this is the only one good solution for JAR Resource problem [link](https://stackoverflow.com/questions/25869428/classpath-resource-not-found-when-running-as-jar)

Spring where is the resource folder after packaging JAR - explanation [link](https://www.baeldung.com/java-classpath-resource-cannot-be-opened)

## Monitoring

Wavefront Dashboard 30-day trial [link](https://docs.wavefront.com/wavefront_springboot.html)

Get API Access - https://longboard.wavefront.com/userprofile/apiaccess

Wavefront SpringBoot setup [link](https://longboard.wavefront.com/integration/springboot/setup)

Java Queue [link](http://tutorials.jenkov.com/java-collections/queue.html)

## Prometeus

Connect with Spring [link](https://docs.spring.io/spring-metrics/docs/current/public/prometheus)

Woooow [link](https://www.callicoder.com/spring-boot-actuator-metrics-monitoring-dashboard-prometheus-grafana/)


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


## Cloud

