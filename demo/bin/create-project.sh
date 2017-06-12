#!/bin/bash

NAME="${1,,}"
VERSION=0.0.1-SNAPSHOT

if [ -z "$NAME" ];then 
	echo "Usage: create-project <name>"
	exit 1
fi

mvn archetype:generate -B -DarchetypeGroupId=com.jbcnconf -DarchetypeArtifactId=project-archetype -Darchetype-version=$VERSION -DgroupId=com.jbcnconf -DartifactId=$NAME -Dversion=$VERSION -Dpackage=com.jbcnconf.$NAME
