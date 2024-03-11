FROM openjdk:17-alpine AS BUILDER
COPY gradle gradle/
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY . .
RUN ./gradlew --no-daemon -i  clean bootJar

FROM openjdk:17-alpine
COPY --from=BUILDER build/libs/*.jar proxy-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "proxy-app.jar"]