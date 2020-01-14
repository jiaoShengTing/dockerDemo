FROM openjdk:8-jre-alpine
#设置变量
ARG PROJECT_FILE
COPY $PROJECT_FILE /opt/application.tar
#新建应用运行目录
RUN mkdir /opt/application
#解压应用压缩包到运行目录
RUN tar -xvf /opt/application.tar -C /opt/application
#清理压缩包
RUN rm -rf /opt/application.tar
RUN rm -rf /opt/application.tar

#更新Alpine的软件源为国内（清华大学）的站点，因为从默认官源拉取实在太慢了。。。
RUN echo "https://mirror.tuna.tsinghua.edu.cn/alpine/v3.4/main/" > /etc/apk/repositories
#安装命令支持
RUN apk update \
    && apk upgrade \
    #安装bash支持，原生只支持sh
    && apk add --no-cache bash \
    bash-doc \
    bash-completion \
    #安装ps支持，原生ps显示有问题
    && apk add --no-cache procps\
    procps-doc\
    && rm -rf /var/cache/* \
    && /bin/bash
#修改文件所属用户和用户组
RUN chown -R `whoami` /opt/application
RUN chgrp -R `whoami` /opt/application
#暴露端口，即应用端口
EXPOSE 8887
# docker启动时运行应用
ENTRYPOINT ["bash","/opt/application/bin/startup.sh"]