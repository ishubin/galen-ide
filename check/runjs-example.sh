#!/bin/bash
API=http://localhost:4567/api


send_task() {
    curl -X POST "$API/devices/$1/tasks" --data "$2"
}

DEVICE_ID=`curl -s $API/devices | jq -r .[0].deviceId`

#curl -X DELETE http://localhost:4567/api/results

send_task "$DEVICE_ID" '{
    "name":"Test on mobile", 
    "commands": [{
        "name":"openUrl", 
        "parameters": {
            "url": "http://testapp.galenframework.com"
        }
    }, { 
        "name":"resize",
        "parameters": {
            "width": 450,
            "height": 600
        }
    }, { 
        "name":"runJs",
        "parameters": {
        "path": "test-scripts/gotoLogin.js"
        }
    }, { 
        "name":"checkLayout",
        "parameters": {
            "path": "specs/loginPage.gspec",
            "tags": ["mobile"]
        }
    }]
}' | jq .


