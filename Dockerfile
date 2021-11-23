FROM java:8

RUN mkdir /work
RUN mkdir /work/app
RUN mkdir /work/log
WORKDIR /work/app
COPY ./sakura-web/sakura-web-exe/target1/app.jar ./app.jar

ENTRYPOINT java -jar /work/app/app.jar