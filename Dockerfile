FROM java:8

RUN mkdir /work
RUN mkdir /work/app
RUN mkdir /work/log
WORKDIR /work/app
COPY ./jar/app.jar ./app.jar

ENV JAVA_OPTS="\
-Xms512m \
-Xmx512g \
-Xmn256m \
-Xss256k \
-XX:PermSize=256m \
-XX:MaxPermSize=256m \
-XX:+PrintGC \
-XX:PrintGCDetails \
-XX:PrintGCDateStamps \
-XX:UserConcMarkSweepGC \
-XX:UseParNewGC \
-XX:CMSInitiatingOccupancyFraction=70 \
-XX:+UseCMSInitiatingOccupancyOnly \
-XX:+UseCMSCompactAtFullCollection \
-XX:+DisableExplicitGC \
-Xnoclassgc"
ENTRYPOINT java ${JAVA_OPTS} -jar /work/app/app.jar