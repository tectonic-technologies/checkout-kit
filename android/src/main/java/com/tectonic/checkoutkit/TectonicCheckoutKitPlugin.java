package com.tectonic.checkoutkit;

import com.getcapacitor.JSObject;
import com.getcapacitor.Logger;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "TectonicCheckoutKit")
public class TectonicCheckoutKitPlugin extends Plugin {

    private TectonicCheckoutKit implementation = new TectonicCheckoutKit(this);

    @PluginMethod
    public void present(PluginCall call) {
        String checkoutUrl = call.getString("checkoutUrl");
        
        if (checkoutUrl == null || checkoutUrl.isEmpty()) {
            call.reject("Invalid checkout URL");
            return;
        }

        try {
            implementation.presentCheckout(checkoutUrl, getContext());
            JSObject ret = new JSObject();
            ret.put("status", "presented");
            call.resolve(ret);
        } catch (Exception e) {
            call.reject("Failed to present checkout: " + e.getMessage());
        }
    }
    
    public void forwardEvent(String eventName, JSObject eventData) {
        Logger.info("TectonicCheckoutKitPlugin", "forwardEvent called with eventName: " + eventName);
        notifyListeners(eventName, eventData);
        Logger.info("TectonicCheckoutKitPlugin", "notifyListeners completed for: " + eventName);
    }
}
