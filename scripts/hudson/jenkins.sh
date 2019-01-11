#!/bin/bash
set -x

function fatal {
  comment_on_pull "Tests failed ($BUILD_URL): $1"
  echo "$1"
  exit 1
}

function comment_on_pull
{
    if [ "$COMMENT_ON_PULL" = "" ]; then return; fi

    PULL_NUMBER=$(echo $GIT_BRANCH | awk -F 'pull' '{ print $2 }' | awk -F '/' '{ print $2 }')
    if [ "$PULL_NUMBER" != "" ]
    then
        JSON="{ \"body\": \"$1\" }"
        curl -d "$JSON" -ujbosstm-bot:$BOT_PASSWORD https://api.github.com/repos/$GIT_ACCOUNT/$GIT_REPO/issues/$PULL_NUMBER/comments
    else
        echo "Not a pull request, so not commenting"
    fi
}


export GIT_ACCOUNT=jbosstm
export GIT_REPO=performance

PULL_NUMBER=$(echo $GIT_BRANCH | awk -F 'pull' '{ print $2 }' | awk -F '/' '{ print $2 }')
PULL_DESCRIPTION=$(curl -ujbosstm-bot:$BOT_PASSWORD -s https://api.github.com/repos/$GIT_ACCOUNT/$GIT_REPO/pulls/$PULL_NUMBER)
if [[ $PULL_DESCRIPTION =~ "\"state\": \"closed\"" ]]; then
  echo "pull closed"
  exit 0
fi

PATH=$WORKSPACE/tmp/tools/maven/bin/:$PATH

comment_on_pull "Started testing this pull request: $BUILD_URL"

JVM_ARGS="-DMAX_ERRORS=10" ./narayana/scripts/hudson/benchmark.sh "ArjunaJTA/jta" "org.jboss.narayana.rts.TxnTest.*" 2
[ $? = 0 ] || fatal "RTS benchmark failed"
mv benchmark-output.txt benchmark-rts-output.txt
mv benchmark.png benchmark-rts.png

