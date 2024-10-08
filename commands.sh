docker exec -it kafka-cluster bash

kafka-topics --list --bootstrap-server localhost:9092

echo 'userId456:ACTIVE' | kafka-console-producer --bootstrap-server localhost:9092 --topic  session-state-update-events --property key.separator=: --property parse.key=true
echo 'userId456:IDLE' | kafka-console-producer --bootstrap-server localhost:9092 --topic  session-state-update-events --property key.separator=: --property parse.key=true
echo 'userId456:INACTIVE' | kafka-console-producer --bootstrap-server localhost:9092 --topic  session-state-update-events --property key.separator=: --property parse.key=true

(echo 'userId456:ACTIVE'; echo 'userId456:IDLE'; echo 'userId456:INACTIVE') | kafka-console-producer --bootstrap-server localhost:9092 --topic  session-state-update-events --property key.separator=: --property parse.key=true

echo 'userId456:{"userId":"userId456","timestamp":"2024-10-08T12:45:00.123Z","ipAddress":"1.1.1.1","deviceType":"DESKTOP","browser":"Firefox","loginMethod":"PASSWORD","sessionId":"sessionId456"}' \
 | kafka-console-producer --bootstrap-server localhost:9092 --topic logged-in-events --property key.separator=: --property parse.key=true

echo 'userId456:{"userId":"userId456","timestamp":"2024-10-08T17:00:00.456Z","ipAddress":"1.1.1.1","deviceType":"DESKTOP","sessionId":"sessionId456","logoutReason":"USER_INITIATED","browser":"Firefox"}' \
 | kafka-console-producer --bootstrap-server localhost:9092 --topic logged-out-events --property key.separator=: --property parse.key=true

