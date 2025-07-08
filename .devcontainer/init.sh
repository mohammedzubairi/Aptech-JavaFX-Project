#!/bin/sh
# Fix line endings and permissions for mvnw
if [ -f ./mvnw ]; then
  sed -i 's/\r$//' ./mvnw
  chmod +x ./mvnw
fi 