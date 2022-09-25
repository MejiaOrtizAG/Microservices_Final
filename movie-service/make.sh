mvn clean && mvn package -DskipTests
docker build . -t movie-service