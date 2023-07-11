#Deploy microservices
ACTUAL_TIME=$(date +%s)

sed -i "s/TIME-UPDATE-BUILD-FNG/${ACTUAL_TIME}/g" microservices/*.yml

kubectl apply -f microservices
