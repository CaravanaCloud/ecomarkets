package main

import (
    "log"
    "net/http"
)

func main() {
    http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
        w.Write([]byte("Ola Feiras e Organicos!!!"))
    })

    log.Fatal(http.ListenAndServe(":9090", nil))
}
