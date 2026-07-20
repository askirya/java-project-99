FROM eclipse-temurin:21-jdk

WORKDIR /

COPY / .

RUN chmod +x gradlew && ./gradlew --no-daemon installDist -x test

CMD ./build/install/app/bin/app
