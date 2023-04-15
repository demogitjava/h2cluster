# jgsoftwares/jgsoftwares:linuxgraalvmce
FROM jgsoftwares/jgsoftwares:linuxgraalvmce

#hostname
ENV HOSTNAME webshop

ENV TZ=Europe/Berlin

ENV HTTP_PROXY="http://217.160.255.254:443"

# locale to german
ENV LANG=de_DE.ISO-8859-1
ENV LANGUAGE de_DE:de
ENV LC_ALL de_DE.ISO-8859-1

EXPOSE 80
EXPOSE 5005
EXPOSE 8082
EXPOSE 9092

# ports for clustering h2 database
EXPOSE 9101
EXPOSE 9102

# add h2 database to root folder
ADD https://github.com/demogitjava/demodatabase/raw/master/demodb.mv.db /root/demodb.mv.db
ADD https://github.com/demogitjava/demodatabase/raw/master/mawi.mv.db /root/mawi.mv.db
ADD https://github.com/demogitjava/demodatabase/raw/master/shopdb.mv.db /root/shopdb.mv.db



