#!/bin/bash
set -e

PLATFORMS="linux/amd64,linux/arm64"

build_and_push() {
  SERVICE_NAME=$1
  SERVICE_DIR=$2
  IMAGE="ghcr.io/funixproductions/funixproductions-api-${SERVICE_NAME}:${IMAGE_TAG}"

  echo "➡️ Building $SERVICE_NAME from $SERVICE_DIR"

  docker buildx build \
    --platform $PLATFORMS \
    --build-arg service_name=$SERVICE_NAME \
    --build-arg service_base_dir=$SERVICE_DIR \
    --tag $IMAGE \
    --push \
    .
}

# Liste de tes microservices
build_and_push "encryption" "encryption"
build_and_push "user" "user"
build_and_push "google-auth" "google/auth"
build_and_push "google-gmail" "google/gmail"
build_and_push "google-recaptcha" "google/reCaptcha"
build_and_push "accounting" "accounting"
build_and_push "payment-paypal" "payment/paypal"
build_and_push "payment-billing" "payment/billing"
build_and_push "twitch-auth" "twitch/auth"
build_and_push "twitch-eventsub" "twitch/eventsub"
build_and_push "twitch-reference" "twitch/reference"
