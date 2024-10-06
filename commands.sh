docker exec -it kafka-cluster bash

kafka-topics --list --bootstrap-server localhost:9092

kafka-console-producer --bootstrap-server localhost:9092 --topic session-state-update-events --property key.separator=: --property parse.key=true
