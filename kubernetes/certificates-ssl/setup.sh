helm upgrade --install cert-manager jetstack/cert-manager --namespace funixproductions --set installCRDs=true --values=config.yml

sed -i "s/SETUP_YOUR_CLOUDFLARE_TOKEN_HERE/${CLOUDFLARE_DNS_ACCESS_TOKEN}/g" secret-cloudflare.yml

kubectl apply -f secret-cloudflare.yml
kubectl apply -f letsencrypt.yml
kubectl apply -f certificate.yml
