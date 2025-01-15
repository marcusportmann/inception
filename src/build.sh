#!/bin/sh

rm -rf ~/.m2/repository/digital/inception

time mvn -T 1C clean package

