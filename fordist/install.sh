#!/bin/bash
set -e

if [ "$(id -u)" != "0" ]; then
    echo "You should run this script as root: sudo $0" 
    exit 1
fi

DEST=/opt

while getopts "d:" OPT; do
    case $OPT in
        d)
            DEST=$OPTARG
            ;;
    esac
done


mkdir -p  $DEST/galen-ide
cp galen-ide.jar $DEST/galen-ide/.
cp galen-ide $DEST/galen-ide



ln -sf $DEST/galen-ide/galen-ide /usr/local/bin/galen-ide

echo "Galen IDE is successfully installed"

