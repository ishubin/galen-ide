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
    "name":"Restart device", 
    "commands": [{
        "name":"restart"
    }]
}' | jq .

send_task "$DEVICE_ID_2" '{
    "name":"Restart device", 
    "commands": [{
        "name":"restart"
    }]
}' | jq .

send_task "$DEVICE_ID_3" '{
    "name":"Restart device", 
    "commands": [{
        "name":"restart"
    }]
}' | jq .

