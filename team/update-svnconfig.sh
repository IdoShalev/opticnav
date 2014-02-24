#!/usr/bin/env sh

DIR=$(cd $(dirname $0) && pwd)

rm ~/.subversion/config
ln -s $DIR/config ~/.subversion/config
