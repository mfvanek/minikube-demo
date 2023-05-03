# Demo app for testing Liveness and Readiness Probes in minikube
See https://cloud.redhat.com/blog/liveness-and-readiness-probes

## Custom readiness probe
```shell
curl -X GET http://localhost:8090/actuator/health
```

```shell
curl -X GET http://localhost:8080/readyz
```

## Set custom delay for health check response
```shell
curl -X PUT http://localhost:8080/delay -H "Content-Type: application/json" -d '{"newDelayInSeconds": 2}'
```

## Build docker image
## With curl
```shell
./gradlew dockerBuildImage
```

### Lightweight image
```shell
./gradlew bootBuildImage --imageName=io.github.mfvanek/minikube-demo:0.0.1
```

## Run in Docker
```shell
docker run -d -p 8080:8080 -t io.github.mfvanek/minikube-demo:latest
```

## Prepare minikube
https://minikube.sigs.k8s.io/docs/start/

### Config
```shell
minikube config view
```

```shell
minikube config set memory 10000
minikube config set cpus 5
minikube config set driver docker
```

### Start
```shell
minikube start --force --extra-config=kubelet.cgroup-driver=systemd --container-runtime=docker
```

```shell
minikube status
```

### Clean
```shell
minikube delete --all
```

```shell
minikube delete --all --purge
```

### Dashboard
```shell
minikube dashboard
```

## Load image to minikube
```shell
minikube image load io.github.mfvanek/minikube-demo:0.0.8
```

```shell
minikube image ls --format table
```

## Run pod
```shell
minikube kubectl -- run minikube-demo --image=io.github.mfvanek/minikube-demo:0.0.6 --port=8080 --image-pull-policy Never
minikube kubectl -- get pods
minikube kubectl -- delete pod minikube-demo
```

## Deploy to minikube
```shell
minikube kubectl -- create -f k8s-deployment.yaml
```

```shell
minikube kubectl -- apply -f k8s-deployment.yaml
```
