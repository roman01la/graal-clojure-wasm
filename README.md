1. Install sdkman `curl -s "https://get.sdkman.io" | bash`
2. Install GraalVM 25 `sdk install java 25.ea.18-graal`
3. Enable GraalVM 25 `sdk use java 25.ea.18-graal`
4. Install binaryen `brew install binaryen`
5. Replace `$JAVA_HOME` in `deps.edn` with a path to your local GraalVM installation (`echo $JAVA_HOME`)
6. Compile Java classes `javac -parameters -cp $(clj -Spath):$JAVA_HOME/lib/svm/tools/svm-wasm/builder/svm-wasm-api.jar -d target/classes src/browser/Callback.java src/browser/Browser.java`
7. Build wasm executable `clojure -M:native-image`
8. Start web server `python3 -m http.server 8000` go to `http://localhost:8000/` in your browser, press a button on the page, should see an alert
