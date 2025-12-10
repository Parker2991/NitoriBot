echo "starting reset process at $HOME/keepalive"

dtach -n $HOME/keepalive sh ./framework/reset.sh

while true; do
  sh ./framework/bot.sh
  sleep 5
done
