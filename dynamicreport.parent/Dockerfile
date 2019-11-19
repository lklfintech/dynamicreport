# DOCKER-VERSION 1.2.0
FROM 192.168.0.70:5000/custom/jdk1_8_centos:v2
# ADD JDK
ADD ./dynamicreport.feature.provider/target/dynamicreport.feature.provider.tar.gz /home/rpt/
USER root:root
RUN echo "192.168.12.100 bdpnode1.rpt.com" >> /etc/hosts
RUN echo "192.168.12.101 bnpnode2.rpt.com" >> /etc/hosts
RUN echo "192.168.12.102 bnpnode3.rpt.com" >> /etc/hosts
RUN chown -R rpt:rpt /home/rpt
USER rpt:rpt
WORKDIR /home/rpt/dynamicreport.feature.provider
EXPOSE 8080
ENV APP_ACTIVE_PROFILE vsit
CMD ./bin/start.sh;tail -f /home/rpt/applog/dynamicreport.feature.provider/logs/stdout.log