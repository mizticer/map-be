#!/bin/sh

REPO_URL=$(jq -r '.pullrequest.source.repository.full_name' $TRIGGER_PAYLOAD)

PR_ID=$(jq -r '.pullrequest.id' $TRIGGER_PAYLOAD)
PR_TITLE=$(jq -r '.pullrequest.title' $TRIGGER_PAYLOAD)


TARGET_BRANCH_NAME=$(jq -r '.pullrequest.destination.branch.name' $TRIGGER_PAYLOAD)

SOURCE_BRANCH_NAME=$(jq -r '.pullrequest.source.branch.name' $TRIGGER_PAYLOAD)

CLOSE_SOURCE_BRANCH=$(jq -r '.pullrequest.close_source_branch' $TRIGGER_PAYLOAD)

MERGE_STATUS=$(jq -r '.pullrequest.state' $TRIGGER_PAYLOAD)

if [ "$MERGE_STATUS" != "MERGED" ]; then

    if [ "$SOURCE_BRANCH_NAME" == "null" ]; then

        SOURCE_BRANCH_NAME=$(jq -r '.push.changes[0].old.name' $TRIGGER_PAYLOAD)
    fi
    if [ "$SOURCE_BRANCH_NAME" == "null" ]; then
        SOURCE_BRANCH_NAME=$CI_COMMIT_REF_NAME
    fi
else
    SOURCE_BRANCH_NAME=$TARGET_BRANCH_NAME
fi

SOURCE_COMMIT_SHA=$(jq -r '.pullrequest.source.commit.hash' $TRIGGER_PAYLOAD)

function status() {
    STATUS=$1
    echo "{\"state\":\"$STATUS\",\"key\":\"gitlab-pipeline\",\"name\":\"Build & deploy\",\"url\":\"$CI_PIPELINE_URL\",\"description\":\"PR $PR_ID: $SOURCE_BRANCH_NAME → $TARGET_BRANCH_NAME\"}" > build.json

    curl --request POST \
        --header 'Accept: application/json' \
        --header 'Content-Type: application/json' \
        --header "Authorization: Bearer $BEARER_TOKEN" \
        --url https://api.bitbucket.org/2.0/repositories/$REPO_URL/commit/$SOURCE_COMMIT_SHA/statuses/build/ \
        -d @build.json
}


