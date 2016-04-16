#!/bin/bash
API=http://localhost:4567/api


send_action() {
    curl -X POST "$API/devices/$1/actions/$2" --data "$3"
}

#DEVICE_ID=Device_1
DEVICE_ID=`curl -s $API/devices | jq -r .[0].deviceId`


send_action "$DEVICE_ID" "openUrl" '{"url": "http://testapp.galenframework.com"}'
send_action "$DEVICE_ID" "resize" '{"width": 450, "height": 600}'
send_action "$DEVICE_ID" "checkLayout" '{"path": "specs/welcomePage.gspec", "tags": ["mobile"]}'

send_action "$DEVICE_ID" "resize" '{"width": 650, "height": 600}'
send_action "$DEVICE_ID" "checkLayout" '{"path": "specs/welcomePage.gspec", "tags": ["tablet"]}'

send_action "$DEVICE_ID" "resize" '{"width": 1024, "height": 700}'
send_action "$DEVICE_ID" "checkLayout" '{"path": "specs/welcomePage.gspec", "tags": ["desktop"]}'

curl -s http://localhost:4567/api/devices/$DEVICE_ID/actions | jq .
