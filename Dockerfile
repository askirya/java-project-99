FROM eclipse-temurin:21-jdk

WORKDIR /

COPY / .

RUN chmod +x gradlew && ./gradlew --no-daemon installDist -x test

# Render provides postgres:// DATABASE_URL; Spring expects jdbc:postgresql:// JDBC_DATABASE_URL.
CMD ["sh", "-c", "if [ -z \"$JDBC_DATABASE_URL\" ] && [ -n \"$DATABASE_URL\" ]; then export JDBC_DATABASE_URL=$(echo \"$DATABASE_URL\" | sed -e 's|^postgres:|jdbc:postgresql:|' -e 's|^postgresql:|jdbc:postgresql:|'); fi; exec ./build/install/app/bin/app --spring.profiles.active=production"]
