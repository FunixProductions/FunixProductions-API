helm repo update

#Setup funixproductions namespace
kubectl apply -f config/namespace-config.yml

#Setup service secrets for encryption
sed -i "s/SETUP_ENCRYPTION_IV_KEY_REPLACEME/${ENCRYPTION_IV}/g" config/encryption-config.yml
sed -i "s/SETUP_ENCRYPTION_KEY_REPLACEME/${ENCRYPTION_KEY}/g" config/encryption-config.yml
sed -i "s/SETUP_JWT_KEY_REPLACEME/${JWT_KEY}/g" config/encryption-config.yml
kubectl apply -f config/encryption-config.yml

#Setup Google secrets
sed -i "s/SETUP_RECAPTCHA_SITE_KEY_REPLACEME/${GOOGLE_RECAPTCHA_SITE_KEY}/g" config/google-secret.yml
sed -i "s/SETUP_RECAPTCHA_SECRET_KEY_REPLACEME/${GOOGLE_RECAPTCHA_SECRET_KEY}/g" config/google-secret.yml
kubectl apply -f config/google-secret.yml

#Setup Paypal secrets
sed -i "s/SETUP_PAYPAL_CLIENT_ID_REPLACEME/${PAYPAL_CLIENT_ID}/g" config/paypal-secret.yml
sed -i "s/SETUP_PAYPAL_CLIENT_SECRET_REPLACEME/${PAYPAL_CLIENT_SECRET}/g" config/paypal-secret.yml
kubectl apply -f config/paypal-secret.yml

#Setup Twitch secrets
sed -i "s/SETUP_TWITCH_CLIENT_ID_REPLACEME/${TWITCH_CLIENT_ID}/g" config/twitch-secret.yml
sed -i "s/SETUP_TWITCH_CLIENT_SECRET_REPLACEME/${TWITCH_CLIENT_SECRET}/g" config/twitch-secret.yml
sed -i "s/SETUP_TWITCH_HMAC_SECRET_KEY_REPLACEME/${TWITCH_HMAC_SECRET}/g" config/twitch-secret.yml
kubectl apply -f config/twitch-secret.yml

#Setup database credentials
sed -i "s/SETUP_POSTGRES_USER_REPLACEME/${DB_USER}/g" postgres/postgres-secret.yml
sed -i "s/SETUP_POSTGRES_PASSWORD_REPLACEME/${DB_PASSWORD}/g" postgres/postgres-secret.yml
kubectl apply -f postgres/postgres-config.yml
kubectl apply -f postgres/postgres-secret.yml

#Deploy microservices
ACTUAL_TIME=$(date +%s)
sed -i "s/TIME-UPDATE-BUILD-FNG/${ACTUAL_TIME}/g" microservices/*.yml
kubectl apply -f microservices

#Setup certificates ssl
cd certificates-ssl
./setup.sh
cd ..

#Deploy ingress
kubectl apply -f ingress.yml