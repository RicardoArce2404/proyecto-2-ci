#!/usr/bin/env bash
cd "$(dirname "$0")"
./genlex.sh && echo -e && ./genpar.sh && echo -e && ./runpar.sh
