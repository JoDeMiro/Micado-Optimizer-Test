## Spring Boot File Upload / Download Rest API Example

**Description**: This is a small java rest api sevice which can be installed on the service side in order to test MiCado Optimizer Auto-Scaling Serice.

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
```

## Test it

**1. with Post Man** 

- Download and install PostMan [download](https://www.postman.com/downloads/)
- Follow the steps showed bellow

![alt text](https://github.com/[username]/[reponame]/blob/[branch]/Images/image.jpg?raw=true)
