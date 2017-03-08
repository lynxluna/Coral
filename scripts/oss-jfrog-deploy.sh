#!/bin/bash

if [ "${TRAVIS_PULL_REQUEST}" == "false" ] && [ "${TRAVIS_BRANCH}" == "master" ];
then
  echo "Deploying Maven Snapshots..." && mvn deploy
else
  echo "This is pull request, not deploying snapshots..."
fi