#!/usr/bin/env bash

if [ -z "${SONATYPE_PASSWORD}" ]; then
    read -p "SONATYPE_PASSWORD=" SONATYPE_PASSWORD
fi

if [ -z "${GPG_PASSWORD}" ]; then
    read -p "GPG_PASSWORD=" GPG_PASSWORD
fi

travis encrypt "SONATYPE_PASSWORD='${SONATYPE_PASSWORD}'" -a
travis encrypt "GPG_PASSWORD='${GPG_PASSWORD}'" -a


travis encrypt-file gpg/paradoxical-io-private.gpg gpg/paradoxical-io-private.gpg.enc \
    -w gpg/paradoxical-io-private.gpg -p

echo "use \$GPG_PRIVATE_KEY_ENCRYPTION_KEY and \$GPG_PRIVATE_KEY_ENCRYPTION_IV for the file encryption command instead"


read -p "GPG_PRIVATE_KEY_ENCRYPTION_KEY=" GPG_PRIVATE_KEY_ENCRYPTION_KEY
read -p "GPG_PRIVATE_KEY_ENCRYPTION_IV=" GPG_PRIVATE_KEY_ENCRYPTION_IV

travis encrypt "GPG_PRIVATE_KEY_ENCRYPTION_KEY=${GPG_PRIVATE_KEY_ENCRYPTION_KEY}" -a
travis encrypt "GPG_PRIVATE_KEY_ENCRYPTION_IV=${GPG_PRIVATE_KEY_ENCRYPTION_IV}" -a

