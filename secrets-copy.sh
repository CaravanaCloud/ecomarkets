#!/bin/bash

# Define the source and target files
source_file="../ecomarkets-secrets/.envrc"
target_file="./.envrc"

# Check if the target file does not exist
if [ ! -f "$target_file" ]; then
    # Target file does not exist, so copy from source
    if [ -f "$source_file" ]; then
        cp "$source_file" "$target_file"
        echo ".envrc file copied from ../secrets directory."
    else
        echo "Source .envrc file does not exist in ../secrets directory."
    fi
else
    echo ".envrc file already exists in the current directory."
fi
