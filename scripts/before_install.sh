#!/usr/bin/env bash

if [ -e ${HOME}/.buckdir/.git ];
then
  pushd ${HOME}/.buckdir && git pull origin master && popd
else
  git clone --depth 10 https://github.com/facebook/buck.git ${HOME}/.buckdir
fi
