mvn clean && mvn package -DskipTests
docker build . -t series-service