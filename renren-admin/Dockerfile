FROM java:8
EXPOSE 8080

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo "Asia/Shanghai" > /etc/timezone

VOLUME /tmp
VOLUME ["/user/local/imgs","/imgs","/excel"]
ADD target/renren-admin.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
