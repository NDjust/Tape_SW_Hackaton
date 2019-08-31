#!/bin/sh
sudo docker run -it --rm \
  -v `pwd`/credential.json:/credential.json \
  -v `pwd`/../test:/test_api \
  -e GOOGLE_APPLICATION_CREDENTIALS=/credential.json \
  -v `pwd`/src/server:/server \
  -p 80:8000 \
  server bash

