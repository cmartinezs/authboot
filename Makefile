mvn-grant:
	chmod +x mvnw
mvn-compile:
	./mvnw compile
mvn-install:
	./mvnw install
mvn-run: mvn-install
	cd app && ./../mvnw spring-boot:run