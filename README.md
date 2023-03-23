# Demo app for testing Liveness and Readiness Probes in minikube
See https://cloud.redhat.com/blog/liveness-and-readiness-probes

## Custom readiness probe
curl -X GET http://localhost:8080/actuator/health

## Set custom delay for health check response
curl -X PUT http://localhost:8080/delay -H "Content-Type: application/json" -d '{"newDelayInSeconds": 2}'

## Build docker image
## With curl
./gradlew docker

### Lightweight image
./gradlew bootBuildImage --imageName=io.github.mfvanek/minikube-demo:0.0.1

## Run in Docker
docker run -d -p 8080:8080 -t io.github.mfvanek/minikube-demo:0.0.2

## Load image to minikube
minikube image load io.github.mfvanek/minikube-demo:0.0.4
minikube image ls --format table

## Run pod 
minikube kubectl -- run minikube-demo --image=io.github.mfvanek/minikube-demo:0.0.3 --port=8080 --image-pull-policy Never
minikube kubectl -- get pods
minikube kubectl -- delete pod minikube-demo

## Deploy to minikube
minikube kubectl -- create -f k8s-deployment.yaml

minikube kubectl -- apply -f k8s-deployment.yaml
