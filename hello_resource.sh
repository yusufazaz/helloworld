#!/usr/bin/env bash
echo "Creating resources in local stack"
aws --endpoint-url=http://localhost:4566 s3 mb s3://hello-world-bucket
aws --endpoint-url http://localhost:4566 sns create-topic --name  HELLO_WORLD_TPC
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name HELLO_WORLD_Q
aws --endpoint-url=http://localhost:4566 sns subscribe --topic-arn  arn:aws:sns:us-east-1:000000000000:HELLO_WORLD_TPC  --protocol sqs --notification-endpoint http://localhost:4566/000000000000/HELLO_WORLD_Q

