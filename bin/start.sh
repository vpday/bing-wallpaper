#!/bin/bash

java -Xmx128m -Xms128m -Xmn64m -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom -jar ./bing-wallpaper-latest.jar
