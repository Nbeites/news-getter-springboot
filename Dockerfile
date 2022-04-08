########Maven build stage########
FROM maven:3.6-jdk-11 as maven_build
WORKDIR /app

#copy pom
COPY pom.xml .
#copy source
COPY src ./src
COPY commands.sh /scripts/commands.sh

# build the app and download dependencies only when these are new (thanks to the cache)
#RUN mvn clean package -Dmaven.test.skip

# split the built app into multiple layers to improve layer rebuild
# RUN mkdir -p target/docker-packaging && cd target/docker-packaging && jar -xf ../my-app*.jar

EXPOSE 8082

#VOLUME /tmp
#ARG news-0.0.1-SNAPSHOT.jar
#COPY target/news-0.0.1-SNAPSHOT.jar news-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","news-0.0.1-SNAPSHOT.jar"]


RUN ["chmod", "+x", "/scripts/commands.sh"]
ENTRYPOINT ["/scripts/commands.sh"]

#copy built app layer by layer
# ARG DOCKER_PACKAGING_DIR=/app/target/docker-packaging
# COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/BOOT-INF/lib /app/lib
# COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/BOOT-INF/classes /app/classes
# COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/META-INF /app/META-INF

# ENTRYPOINT ["java","-cp","app:app/lib/*","com.aggregator.news.NewsApplication"]

#run the app
# CMD java -cp .:classes:lib/* \
#          -Djava.security.egd=file:/dev/./urandom \
#          com.aggregator.news.NewsApplication
