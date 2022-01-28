#!/bin/sh
JARFILE="SwtPra10-pc-1.3.2-all.jar"
BEOBACHTER_DESKTOP="maulwurf-pc-beobachter.desktop"
BEOBACHTER_DESKTOP_WAYLAND="maulwurf-pc-beobachter-wayland.desktop"
AUSRICHTER_DESKTOP="maulwurf-pc-ausrichter.desktop"
AUSRICHTER_DESKTOP_WAYLAND="maulwurf-pc-ausrichter-wayland.desktop"
BINDIR="/opt/"
DESKTOPDIR="$HOME/.local/share/applications/"

if [ -e $JARFILE ]; then
	echo "Kopiere $JARFILE nach $BINDIR"
	sudo mv $JARFILE $BINDIR
else
	echo "$JARFILE Datei konnte nicht in diesem Verzeichnis gefunden werden!"
fi

if [ -e $BEOBACHTER_DESKTOP ]; then
	echo "Kopiere $BEOBACHTER_DESKTOP nach $DESKTOPDIR"
	mv $BEOBACHTER_DESKTOP $DESKTOPDIR
else
	echo "$BEOBACHTER_DESKTOP Datei konnte nicht in diesem Verzeichnis gefunden werden!"
fi

if [ -e $BEOBACHTER_DESKTOP_WAYLAND ]; then
	echo "Kopiere $BEOBACHTER_DESKTOP_WAYLAND nach $DESKTOPDIR"
	mv $BEOBACHTER_DESKTOP_WAYLAND $DESKTOPDIR
else
	echo "$BEOBACHTER_DESKTOP_WAYLAND Datei konnte nicht in diesem Verzeichnis gefunden werden!"
fi


if [ -e $AUSRICHTER_DESKTOP ]; then
	echo "Kopiere $AUSRICHTER_DESKTOP nach $DESKTOPDIR"
	mv $AUSRICHTER_DESKTOP $DESKTOPDIR
else
	echo "$AUSRICHTER_DESKTOP Datei konnte nicht in diesem Verzeichnis gefunden werden!"
fi

if [ -e $AUSRICHTER_DESKTOP_WAYLAND ]; then
	echo "Kopiere $AUSRICHTER_DESKTOP_WAYLAND nach $DESKTOPDIR"
	mv $AUSRICHTER_DESKTOP_WAYLAND $DESKTOPDIR
else
	echo "$AUSRICHTER_DESKTOP_WAYLAND Datei konnte nicht in diesem Verzeichnis gefunden werden!"
fi
