#!/bin/bash

# Step 1: Get FullNode process ID
pid=$(jps | grep FullNode | awk '{print $1}')

if [ -z "$pid" ]; then
  echo "FullNode process not found."
  exit 1
fi

echo "Found FullNode process with PID: $pid"

# Step 2: Send SIGTERM
kill -15 "$pid"
echo "Sent SIGTERM (kill -15) to process $pid"

# Step 3: Sleep for 3 second
sleep 3
echo "Waiting for 3 seconds to allow process to terminate..."

# Step 4: Check if process is terminated
if ps -p "$pid" > /dev/null; then
  echo "Process $pid is still running."
else
  echo "Process $pid has terminated successfully."
fi
