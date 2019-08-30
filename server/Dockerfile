FROM python:3

#
# Google Cloud Sdk 설치
#

# Downloading gcloud package
RUN curl https://dl.google.com/dl/cloudsdk/release/google-cloud-sdk.tar.gz > /tmp/google-cloud-sdk.tar.gz

# Installing the package
RUN mkdir -p /usr/local/gcloud \
  && tar -C /usr/local/gcloud -xvf /tmp/google-cloud-sdk.tar.gz \
  && /usr/local/gcloud/google-cloud-sdk/install.sh

# Adding the package path to local
ENV PATH $PATH:/usr/local/gcloud/google-cloud-sdk/bin

#
# 음성인식 Python 패키지 설치
#

# Install python packages
RUN pip install \
  pydub \
  argparse \
  google-cloud-speech

RUN apt update
RUN apt install -y ffmpeg

#
# 웹 서버 설치
#

# Apache2 & PHP7 & MySql 설치
RUN apt install -y \
  php \
  php-mysql \
  php-common \
  mariadb-server

RUN service apache2 start \
  && service mysql start

EXPOSE 80 443
