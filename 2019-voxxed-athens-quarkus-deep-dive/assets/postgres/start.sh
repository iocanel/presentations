#!/bin/bash

docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name quarkus_test \
            -e POSTGRES_USER=root \
            -e POSTGRES_PASSWORD=password\
            -e POSTGRES_DB=demo \
            -v "$(pwd)"/init:/docker-entrypoint-initdb.d/ \
            -p 5432:5432 postgres:10.5

# Or using the command line
#PGPASSWORD=quarkus_test psql -p 5432 -h localhost -U quarkus_test -f greetings-db.sql 
