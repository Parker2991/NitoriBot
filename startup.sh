while true; do
  XAUTHORITY=/var/lib/lightdm/.Xauthority DISPLAY=:0 x0vncserver -rfbauth $HOME/.config/tigervnc/passwd
  sleep 1
done
