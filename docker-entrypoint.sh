#!/bin/sh
set -e

# Render gives postgres://user:pass@host[:port]/db
# Spring needs jdbc:postgresql://host:port/db + separate username/password.
if [ -z "$JDBC_DATABASE_URL" ] && [ -n "$DATABASE_URL" ]; then
  RAW="$DATABASE_URL"
  WITHOUT_SCHEME="${RAW#postgres://}"
  WITHOUT_SCHEME="${WITHOUT_SCHEME#postgresql://}"

  USERINFO="${WITHOUT_SCHEME%%@*}"
  HOSTPART="${WITHOUT_SCHEME#*@}"

  DB_USER="${USERINFO%%:*}"
  DB_PASS="${USERINFO#*:}"

  HOSTPORT="${HOSTPART%%/*}"
  DB_PATH="/${HOSTPART#*/}"

  case "$HOSTPORT" in
    *:*) ;;
    *) HOSTPORT="${HOSTPORT}:5432" ;;
  esac

  export JDBC_DATABASE_URL="jdbc:postgresql://${HOSTPORT}${DB_PATH}"
  export DATABASE_USERNAME="$DB_USER"
  export DATABASE_PASSWORD="$DB_PASS"
fi

exec ./build/install/app/bin/app --spring.profiles.active=production
