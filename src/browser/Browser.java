package browser;

import org.graalvm.webimage.api.JS;
import org.graalvm.webimage.api.JSObject;

public class Browser {
    public static void main() {
        try {
            // TODO GR-62854 Here to ensure handleEvent and run is generated. Remove once
            // objects passed to @JS methods automatically have their SAM registered.
            sink(Callback.class.getDeclaredMethod("run", JSObject.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @JS("")
    private static native void sink(Object o);

    @JS.Coerce
    @JS("return globalThis;")
    public static native JSObject globalThis();

    @JS.Coerce
    @JS("return document.querySelector(selector);")
    public static native JSObject querySelector(String selector);

    @JS.Coerce
    @JS("return document.createElement(tag);")
    public static native JSObject createElement(String tag);

    @JS.Coerce
    @JS("return parent.appendChild(child);")
    public static native void appendChild(JSObject parent, JSObject child);

    @JS.Coerce
    @JS("element.addEventListener(eventType, handler);")
    public static native void addEventListener(JSObject element, String eventType, Callback handler);
}