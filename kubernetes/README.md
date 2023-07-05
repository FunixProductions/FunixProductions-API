# Deploy FunixProductionsAPI to Kubernetes

jetstack for cert manager
```bash
helm repo add jetstack https://charts.jetstack.io
```

setup app
```bash
kubectl create secret generic gmail-credentials --from-file=gmail-credentials.json --namespace=funixproductions
./deploy.sh
```
