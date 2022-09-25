mvn clean && mvn package -DskipTests
docker build . -t api-gateway