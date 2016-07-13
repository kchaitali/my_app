FROM java:8
MAINTAINER chaitali
EXPOSE 4567 
# Install maven
#RUN apt-get update  
#RUN apt-get install maven

WORKDIR /home/bigdata_coe/chaitali_workspace/mRapidMFToHadoop

# Prepare by downloading dependencies
#ADD pom.xml /home/bigdata_coe/chaitali_workspace/mRapidMFToHadoop/pom.xml 
#RUN ["maven", "dependency:resolve"]  
#RUN ["maven", "verify"]


CMD java -jar mRapidMFToHadoop-0.0.1-SNAPSHOT.jar
ADD target/ target/mRapidMFToHadoop-0.0.1-SNAPSHOT.jar

#Adding source, compile and package into a fat jar
#ADD src /home/bigdata_coe/chaitali_workspace/mRapidMFToHadoop/src  
#RUN mvn package

#EXPOSE 4567  
#CMD ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "target/mRapidMFToHadoop-0.0.1-SNAPSHOT.jar"]

