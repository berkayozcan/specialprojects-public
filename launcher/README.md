# Pi Launchpad

A small, self-hosted home page that ties the *specialprojects* tools together.
It lists every project as a card — with its one-line install command, its start
command, the port it serves on, and the security notes worth knowing — and gives
you an **Open** button that jumps straight to whichever web service is running on
your Pi.

![Projects: Remote Desktop · Webcam Streamer · File Browser · Facebook Terminal · Mass Install](index.html)

## What's inside

| Project | Category | Serves on | Open in browser |
|---|---|---|---|
| Remote Desktop | Access | `:80` (noVNC → VNC `:5901`) | yes |
| Webcam Streamer | Media | `:80` (MJPEG) | yes |
| File Browser | Files | `:80` | yes |
| Facebook Terminal | Terminal | `:80` (webhook) | via Messenger |
| Mass Install | Fleet | — | provisioning hook |

## Install

Run the installer on the Pi as root, from this folder:

```sh
sudo lua install_launcher.lua
```

It installs `python3`, copies `index.html` to `/opt/launcher/`, and writes a
`start` script.

## Run

```sh
sudo /opt/launcher/start
```

This serves the launcher on port `80` with Python's built-in HTTP server. Open
your Pi's address in a browser (for example `http://raspberrypi.local`).

Type your device's address into the **Device** field in the header once — it is
remembered per browser — and the **Open** buttons will point at it.

## Notes

- Every web project here binds to port `80`, so run one at a time, or give each
  its own Pi.
- The launcher is a single static `index.html`: no build step, no dependencies
  beyond `python3`, works offline, and adapts to your system's light or dark
  theme (with a manual toggle in the header).
- On the public internet, reach these services through your Dataplicity wormhole
  or a reverse proxy with TLS rather than opening port `80` directly.
