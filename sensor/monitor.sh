#!/bin/bash
until ./google_spreadsheet.py; do
    echo "'google_spreadsheet.py' crashed with exit code $?. Restarting..." >&2
    sleep 2
done
