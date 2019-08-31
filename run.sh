#!/bin/sh
sudo docker run -it --rm \
  -v data:/var/lib/mysql \
  -v `pwd`/server/credential.json:/credential.json \
  -v `pwd`/tape:/tape \
  -e GOOGLE_APPLICATION_CREDENTIALS=/credential.json \
  -v `pwd`/server/src/server:/server \
  -v `pwd`/server/host.conf:/etc/nginx/sites-enabled/default \
  -p 80:80 \
  server bash

