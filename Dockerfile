# the first stage of our build will use a maven parent image
FROM maven:3.8-eclipse-temurin-8-alpine AS maven_build

# install ca-certificates to fix SSL/TLS issues with Maven Central
RUN apk add --no-cache ca-certificates

# set MAVEN_OPTS to enable TLS 1.2
ENV MAVEN_OPTS="-Dhttps.protocols=TLSv1.2 -Dmaven.wagon.http.ssl.insecure=false -Dmaven.wagon.http.ssl.allowall=false"

# copy the pom and src code to the container
COPY ./ ./

# package our application code
RUN mvn package -DskipTests


# the second stage of our build will use eclipse temurin jre 8 on alpine
FROM eclipse-temurin:8-jre-alpine AS application

RUN apk add --no-cache tzdata
ENV TZ=America/Lima

RUN mkdir -p /ti-is-admin/templates
RUN chown root /ti-is-admin/templates

WORKDIR /ti-is-admin

# copy only the artifacts we need from the first stage and discard the rest
COPY --from=maven_build siv-admin/target/classes/templates/. /ti-is-admin/templates
COPY --from=maven_build siv-admin/target/siv-admin-2.0.jar /siv-admin-2.0.jar

ADD ./newrelic/newrelic.jar /opt/newrelic/newrelic.jar
ADD ./newrelic/newrelic.yml /opt/newrelic/newrelic.yml

# set the startup command to execute the jar
CMD ["java","-javaagent:/opt/newrelic/newrelic.jar", "-Dspring.profiles.active=${ENVIRONMENT}", "-jar", "/siv-admin-2.0.jar"]


# the third stage of our build will use sonar cli
FROM sonarsource/sonar-scanner-cli:latest AS sonarqube

WORKDIR /app

COPY . .

COPY --from=maven_build siv-admin/target/classes siv-admin/target/classes
COPY --from=maven_build siv-common/siv-common-dto/target/classes siv-common/siv-common-dto/target/classes
COPY --from=maven_build siv-common/siv-common-persistence/target/classes siv-common/siv-common-persistence/target/classes
COPY --from=maven_build siv-common/siv-common-util/target/classes siv-common/siv-common-util/target/classes