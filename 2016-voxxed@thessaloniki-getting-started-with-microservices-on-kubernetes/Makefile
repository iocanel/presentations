clean:
	rm -rf bin
	
build-in-linux:	
	docker build -t iocanel/voxxedthess-builder -f Dockerfile.linux .
	docker run --privileged -v /usr/bin/docker:/usr/bin/docker -v /var/run/docker.sock:/var/run/docker.sock iocanel/voxxedthess-builder
build:
	CGO_ENABLED=0 GOOS=linux go build -ldflags "-s" -a -installsuffix cgo -o bin/voxxedthess ./pkg/voxxedthess/voxxedthess.go
package:
	docker build -t iocanel/voxxedthess:2016 .
deploy:
	kubectl create -f kubernetes/deployment.yaml
	kubectl create -f kubernetes/service.yaml
undeploy:
	kubectl delete -f kubernetes/deployment.yaml
	kubectl delete -f kubernetes/service.yaml

