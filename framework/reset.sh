while true; do
  sleep 24h
  pkill -f "node --max-old-space-size=1000 --expose-gc build/index.js"
done