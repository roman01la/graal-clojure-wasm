1. Install sdkman `curl -s "https://get.sdkman.io" | bash`
2. Install GraalVM 25 `sdk install java 25.ea.18-graal`
3. Enable GraalVM `sdk use java 25.ea.18-graal`
4. Install binaryen `brew install binaryen`
5. Build wasm executable `clojure -M:native-image`
6. Verify that it runs `node core`
7. Start web server `python3 -m http.server 8000` go to `http://localhost:8000/` in your browser, open browser console and see if the message gets printed out
