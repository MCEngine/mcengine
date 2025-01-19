#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

# Install GPG if not already installed
if ! command -v gpg &> /dev/null; then
  echo "GPG not found. Installing GPG..."
  if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    sudo apt-get update && sudo apt-get install -y gnupg
  elif [[ "$OSTYPE" == "darwin"* ]]; then
    brew install gnupg
  else
    echo "Unsupported OS. Please install GPG manually."
    exit 1
  fi
fi

# Check if the required environment variables are set
if [ -z "$GPG_USERNAME" ] || [ -z "$GPG_EMAIL" ]; then
  echo "Error: GPG_USERNAME and GPG_EMAIL environment variables must be set."
  exit 1
fi

# Generate the GPG key
echo "Generating GPG key..."
gpg --batch --generate-key <<EOF
Key-Type: default
Key-Length: 2048
Name-Real: $GPG_USERNAME
Name-Email: $GPG_EMAIL
Expire-Date: 1d
%no-protection
EOF

# Retrieve the GPG key ID
GPG_KEY_ID=$(gpg --list-keys --with-colons | grep '^pub' | awk -F':' '{print $5}' | tail -1)

if [ -z "$GPG_KEY_ID" ]; then
  echo "Error: Failed to retrieve GPG key ID."
  exit 1
fi

echo "GPG key generated with ID: $GPG_KEY_ID"

# Retrieve the GPG key details
GPG_DETAILS=$(gpg --list-keys --with-colons "$GPG_KEY_ID" | grep '^pub')

KEY_TYPE=$(echo "$GPG_DETAILS" | awk -F':' '{print $2}')
KEY_LENGTH=$(echo "$GPG_DETAILS" | awk -F':' '{print $3}')
KEY_EXPIRE=$(echo "$GPG_DETAILS" | awk -F':' '{print $7}')
KEY_NAME="$GPG_USERNAME"
KEY_EMAIL="$GPG_EMAIL"

# Check if gpg.txt exists
if [ ! -f gpg.txt ]; then
  echo "Creating gpg.txt..."
  cat <<EOF > gpg.txt
Type: $KEY_TYPE
Length: $KEY_LENGTH
Name: $KEY_NAME
Id: $GPG_KEY_ID
Email: $KEY_EMAIL
Expire: $KEY_EXPIRE
EOF
  echo "GPG data saved in gpg.txt."
else
  echo "gpg.txt already exists. Skipping creation."
fi

# Send the GPG key to the keyserver
echo "Sending GPG key to keyserver.ubuntu.com..."
gpg --keyserver keyserver.ubuntu.com --send-keys "$GPG_KEY_ID"

echo "GPG key successfully sent to the keyserver."
echo "GPG data saved in gpg.txt."
