#!/bin/bash
API=http://localhost:4567/api


send_task() {
    curl -X POST "$API/devices/$1/tasks" --data "$2"
}

#DEVICE_ID=Device_1
DEVICE_ID_1=`curl -s $API/devices | jq -r .[0].deviceId`
DEVICE_ID_2=`curl -s $API/devices | jq -r .[1].deviceId`
DEVICE_ID_3=`curl -s $API/devices | jq -r .[2].deviceId`

#curl -X DELETE http://localhost:4567/api/results

send_task "$DEVICE_ID_1" '{
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
        "name":"checkLayout",
        "parameters": {
            "path": "specs/welcomePage.gspec",
            "tags": ["mobile"]
        }
    }]
}' | jq .

send_task "$DEVICE_ID_2" '{
    "name":"Test on tablet", 
    "commands": [{
        "name":"openUrl", 
        "parameters": {
            "url": "http://testapp.galenframework.com"
        }
    }, { 
        "name":"resize",
        "parameters": {
            "width": 650,
            "height": 600
        }
    }, { 
        "name":"checkLayout",
        "parameters": {
            "path": "specs/welcomePage.gspec",
            "tags": ["tablet"]
        }
    }]
}' | jq .

send_task "$DEVICE_ID_3" '{
    "name":"Test on desktop", 
    "commands": [{
        "name":"openUrl", 
        "parameters": {
            "url": "http://testapp.galenframework.com"
        }
    }, { 
        "name":"resize",
        "parameters": {
            "width": 1024,
            "height": 700
        }
    }, { 
        "name":"checkLayout",
        "parameters": {
            "path": "specs/welcomePage.gspec",
            "tags": ["desktop"]
        }
    }]
}' | jq .


