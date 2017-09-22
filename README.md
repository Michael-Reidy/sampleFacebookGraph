

In one terminal do :

- java -Djava.library.path=./dynamoDB/DynamoDBLocal_lib -jar ./dynamoDB/DynamoDBLocal.jar -sharedDb -inMemory
- wait for dynamodb to start

In the second terminal do :
- for windows
    - set AWS_ACCESS_KEY_ID=1111111
    - set AWS_SECRET_ACCESS_KEY=2222222
- for unix
    - export AWS_ACCESS_KEY_ID=1111111
    - export AWS_SECRET_ACCESS_KEY=2222222

- mvn clean install

- java -jar target/plynkUserSample-0.1.0.jar

Swagger available at this URL : http://localhost:8080/v2/api-docs

Health check URL : http://localhost:8080/health

A sample user : http://localhost:8080/v1/user/sample

A sample Facebook enhanced user : http://localhost:8080/v1/facebookuser/sample