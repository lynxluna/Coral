#!/bin/bash

if [ "${TRAVIS_PULL_REQUEST}" == "false" ] && [ "${TRAVIS_BRANCH}" == "master" ];
then
  echo "Deploying Maven Snapshots..." && mvn deploy
else
  echo "Not deploying snapshots, because this is not master branch..."
fi