cd eureka-server/
mvn clean && mvn package -DskipTests
docker build . -t eureka-server
cd ..

cd server-config/
sh make.sh
cd ..

cd movie-service/
sh make.sh
cd ..

cd catalog-service/
mvn clean && mvn package -DskipTests
docker build . -t catalog-service
cd ..

cd api-gateway/
sh make.sh
cd ..

cd serie-service/
sh make.sh
cd ..

docker-compose up