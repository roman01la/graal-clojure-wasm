1. Install sdkman `curl -s "https://get.sdkman.io" | bash`
2. Install GraalMV 25 `sdk install java 25.ea.18-graal`
3. Enable GraalMV 25 `sdk use java 25.ea.18-graal`
4. Build wasm executable `clojure -A:native-image`
5. Verify that it runs `node helloworld`
6. Start web server `python3 -m http.server 8000` go to `http://localhost:8000/` in your browser, open browser console and see if the message gets printed out
