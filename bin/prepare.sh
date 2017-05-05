#!/bin/bash

minikube start --memory=8192 --vm-driver=kvm
kubectl config use-context demo &
minikube service fabric8 -n demo &
minikube service zipkin -n demo &
minikube service hystrix -n demo &
minikube service presentation -n presentation 

