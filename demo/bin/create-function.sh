#!/bin/bash

NAME="${1,,}"
NAME_CAPITALIZED=${NAME^}
VERSION=0.0.1-jbcnconf-001

if [ -z "$NAME" ];then 
	echo "Usage: create-function <name>"
	exit 1
fi

mvn archetype:generate -B -DarchetypeGroupId=com.jbcnconf -DarchetypeArtifactId=function-archetype -Darchetype-version=$VERSION -DgroupId=com.jbcnconf.func -DartifactId=$NAME -Dversion=$VERSION -Dpackage=com.jbcnconf.func.$NAME -Dname=$NAME_CAPITALIZED
