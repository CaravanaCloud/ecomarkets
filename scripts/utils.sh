#!/bin/bash
# Utility functions for bash scripts
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Define colors
readonly RED="\033[0;31m"
readonly GREEN="\033[0;32m"
readonly YELLOW="\033[0;33m"
readonly BLUE="\033[0;34m"
readonly NO_COLOR="\033[0m"

# Generic log function
log() {
    local level="$1"; shift
    local message="$1"; shift
    local color="$NO_COLOR"

    # Determine color based on log level
    case "$level" in
        INFO) color="$GREEN" ;;
        DEBUG) color="$BLUE" ;;
        WARN) color="$YELLOW" ;;
        ERROR) color="$RED" ;;
    esac

    # Print the message in the determined color
    echo -e "${color}[${level}]${NO_COLOR} $message" >&2
}

# Specific log level functions
info() {
    log "INFO" "$@"
}

debug() {
    log "DEBUG" "$@"
}

warn() {
    log "WARN" "$@"
}

error() {
    log "ERROR" "$@"
}
add() {
    local result=$(($1 + $2))
    # Logs are sent to stderr
    info "$result"
    # Data is sent to stdout
    echo "$result"
    # Error code is returned
    return 0
}
awaitFile() {
    local filepath="$1"
    local delay="${2:-5}" # Delay between retries in seconds, default to 5 seconds
    local retries="${3:-12}" # Number of retries, defaulting to 12 (1 minute if delay is 5 seconds)

    echo "Waiting for file $filepath to exist..."

    for ((i=0; i<retries; i++)); do
        if [ -e "$filepath" ]; then
            echo "File $filepath exists."
            return 0
        else
            echo "File $filepath not found. Retrying in ${delay} seconds..."
            sleep $delay
        fi
    done

    echo "Error: File $filepath does not exist after $(($retries * $delay)) seconds."
    return 1
}
awaitTCP() {
    local port="${1:-80}"
    local host="${2:-127.0.0.1}"
    local delay="${3:-15}" # Delay between retries in seconds
    local retries="${4:-99}" # Number of retries, defaulting to 99

    for ((i=0; i<retries; i++)); do
        # Attempt to connect to the port
        if (echo > /dev/tcp/${host}/${port}) 2>/dev/null; then
            info "Success: Able to connect to ${host}:${port} on attempt $(($i + 1))"
            echo "Success: Able to connect to ${host}:${port}"
            return 0
        else
            info "Attempt $(($i + 1)) of ${retries} failed: Unable to connect to ${host}:${port}. Retrying in ${delay} seconds..."
            sleep $delay
        fi
    done

    error "Error: After ${retries} attempts, unable to connect to ${host}:${port}"
    echo "Error: After ${retries} attempts, unable to connect to ${host}:${port}"
    return 1
}
awaitURL() {
    local url="$1"
    local expected_status="${2:-200}"
    local delay="${3:-15}" # Delay between retries in seconds
    local retries="${4:-99}" # Number of retries, defaulting to 99

    echo "Checking URL: $url for status: $expected_status"

    for ((i=0; i<retries; i++)); do
        # Use curl to get the HTTP status code of the response
        response=$(curl -o /dev/null -s -w "%{http_code}" "$url")

        if [ "$response" -eq "$expected_status" ]; then
            echo "Success: Received expected status $expected_status from $url"
            return 0
        else
            echo "Attempt $(($i + 1)) of $retries failed: Received status $response from $url. Retrying in ${delay} seconds..."
            sleep $delay
        fi
    done

    echo "Error: After $retries attempts, did not receive expected status $expected_status from $url"
    return 1
}
checkEnv() {
    local missingVars=()

    for varName in "$@"; do
        if [[ -z ${!varName+x} ]]; then
            missingVars+=("$varName")
        fi
    done

    if [ ${#missingVars[@]} -ne 0 ]; then
        echo "Error: The following environment variables are missing or empty: ${missingVars[*]}"
        return 1
    else
        echo "Success: All variables are defined and not empty."
        return 0
    fi
}
