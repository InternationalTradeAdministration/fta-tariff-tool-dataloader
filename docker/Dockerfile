FROM openjdk

COPY ["fta-tariff-tool-dataloader-0.0.1-SNAPSHOT.jar", "/temp/"]

EXPOSE 80

CMD java -jar /temp/fta-tariff-tool-dataloader-0.0.1-SNAPSHOT.jar --spring.profiles.active=staging --server.port=80