FROM eclipse-temurin:21-jdk

WORKDIR /

COPY / .

RUN chmod +x gradlew docker-entrypoint.sh \
    && ./gradlew --no-daemon installDist -x test

CMD ["./docker-entrypoint.sh"]
