package browser;

import org.graalvm.webimage.api.JSObject;

@FunctionalInterface
public interface Callback {
    void run(JSObject event);
}