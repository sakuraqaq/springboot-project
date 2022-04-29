FROM java:8

RUN mkdir /work
RUN mkdir /work/app
RUN mkdir /work/log
WORKDIR /work/app
COPY ./jar/app.jar ./app.jar

ENV JAVA_OPTS="\
-Xms512m \
-Xmx512m \
-Xmn256m \
-Xss256k \
-XX:+PrintGC \
-XX:+PrintGCDetails \
-XX:+PrintGCDateStamps \
-XX:+UserConcMarkSweepGC \
-XX:+UseParNewGC \
-XX:CMSInitiatingOccupancyFraction=70 \
-XX:+UseCMSInitiatingOccupancyOnly \
-XX:+DisableExplicitGC"
ENTRYPOINT java ${JAVA_OPTS} -jar /work/app/app.jar