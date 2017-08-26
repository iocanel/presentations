package main

import (
	"fmt"
	"http"
)

func hello(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Hello, world!")
}

func main() {
	http.HandleFunc("/", hello)
	http.ListenAndServe(8080, nil)
}
