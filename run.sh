#!/bin/sh
sudo docker run -it --rm \
  -v data:/var/lib/mysql \
  -v `pwd`/server/credential.json:/credential.json \
  -v `pwd`/tape:/tape_video_api \
  -e GOOGLE_APPLICATION_CREDENTIALS=/credential.json \
  -v `pwd`/server/src/server:/server \
  -p 80:8000 \
  server bash

