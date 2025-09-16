package com.tectonic.checkoutkit;

import android.app.Activity;
import androidx.activity.ComponentActivity;
import android.content.Context;
import android.net.Uri;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebChromeClient.FileChooserParams;
import com.getcapacitor.Logger;
import com.getcapacitor.JSObject;
import com.google.gson.Gson;
import com.shopify.checkoutsheetkit.CheckoutException;
import com.shopify.checkoutsheetkit.DefaultCheckoutEventProcessor;
import com.shopify.checkoutsheetkit.ShopifyCheckoutSheetKit;
import com.shopify.checkoutsheetkit.lifecycleevents.CheckoutCompletedEvent;
import com.shopify.checkoutsheetkit.pixelevents.PixelEvent;
import com.shopify.checkoutsheetkit.pixelevents.StandardPixelEvent;
import com.shopify.checkoutsheetkit.pixelevents.CustomPixelEvent;

public class TectonicCheckoutKit {
    private TectonicCheckoutKitPlugin plugin;
    
    public TectonicCheckoutKit(TectonicCheckoutKitPlugin plugin) {
        this.plugin = plugin;
    }

    public void presentCheckout(String checkoutUrl, Context context) {
        if (!(context instanceof ComponentActivity)) {
            throw new IllegalArgumentException("Context must be a ComponentActivity");
        }
        
        ComponentActivity activity = (ComponentActivity) context;
        
        DefaultCheckoutEventProcessor processor = new DefaultCheckoutEventProcessor(activity) {
            @Override
            public void onCheckoutCompleted(CheckoutCompletedEvent checkoutCompletedEvent) {
                // Called when the checkout was completed successfully by the customer.
                // Use this to update UI, reset cart state, etc.
                Logger.info("TectonicCheckoutKit", "Checkout completed with order ID: " + checkoutCompletedEvent.getOrderDetails().getId());
                
                // Pass OrderDetails as JSON string using Gson
                JSObject eventData = new JSObject();
                Gson gson = new Gson();
                
                try {
                    // Convert OrderDetails to JSON string using Gson
                    String orderDetailsJson = gson.toJson(checkoutCompletedEvent.getOrderDetails());
                    eventData.put("orderDetails", orderDetailsJson);
                    eventData.put("timestamp", System.currentTimeMillis());
                } catch (Exception e) {
                    Logger.error("TectonicCheckoutKit", "Error serializing checkout completed event data", e);
                    // Fallback to basic data
                    try {
                        eventData.put("orderDetails", "{\"id\":\"" + checkoutCompletedEvent.getOrderDetails().getId() + "\"}");
                    } catch (Exception e2) {
                        eventData.put("orderDetails", "{}");
                    }
                    eventData.put("timestamp", System.currentTimeMillis());
                }
                
                plugin.forwardEvent("completed", eventData);
            }

            @Override
            public void onCheckoutCanceled() {
                // Called when the checkout was canceled by the buyer.
                // Note: This will also be received after closing a completed checkout
                Logger.info("TectonicCheckoutKit", "onCheckoutClosed called");
                
                // Create structured data for canceled event
                JSObject eventData = new JSObject();
                eventData.put("reason", "user_closed");
                eventData.put("timestamp", System.currentTimeMillis());
                
                Logger.info("TectonicCheckoutKit", "Forwarding close event to JS");
                plugin.forwardEvent("close", eventData);
                Logger.info("TectonicCheckoutKit", "Close event forwarded");
            }

            @Override
            public void onCheckoutFailed(CheckoutException error) {
                // Called when the checkout encountered an error and was aborted.
                Logger.error("TectonicCheckoutKit", "Checkout failed with error: " + error.getLocalizedMessage(), error);
                
                // Extract structured error data
                JSObject eventData = new JSObject();
                JSObject errorDetails = new JSObject();
                
                try {
                    errorDetails.put("message", error.getLocalizedMessage() != null ? error.getLocalizedMessage() : error.getMessage());
                    errorDetails.put("errorCode", error.getClass().getSimpleName());
                    errorDetails.put("timestamp", System.currentTimeMillis());
                    
                    // Add stack trace for debugging (optional)
                    if (error.getCause() != null) {
                        errorDetails.put("cause", error.getCause().getMessage());
                    }
                    
                    eventData.put("error", errorDetails);
                } catch (Exception e) {
                    Logger.error("TectonicCheckoutKit", "Error extracting checkout failed event data", e);
                    // Fallback to basic error data
                    errorDetails.put("message", "Checkout failed");
                    errorDetails.put("errorCode", "UNKNOWN_ERROR");
                    errorDetails.put("timestamp", System.currentTimeMillis());
                    eventData.put("error", errorDetails);
                }
                
                plugin.forwardEvent("failed", eventData);
            }

            @Override
            public void onCheckoutLinkClicked(Uri uri) {
                Logger.info("TectonicCheckoutKit", "Checkout link clicked: " + uri.toString());
                
                // Pass URI as is
                JSObject eventData = new JSObject();
                eventData.put("uri", uri.toString());
                eventData.put("timestamp", System.currentTimeMillis());
                
                plugin.forwardEvent("linkClicked", eventData);
                super.onCheckoutLinkClicked(uri);
            }

            private JSObject processStandardPixelEvent(StandardPixelEvent standardEvent) {
                JSObject eventData = new JSObject();
                Gson gson = new Gson();
                
                try {
                    eventData.put("type", "standard");
                    eventData.put("timestamp", System.currentTimeMillis());
                    eventData.put("name", standardEvent.getName() != null ? standardEvent.getName() : "unknown");
                    
                    // Serialize the complete StandardPixelEvent object to JSON
                    try {
                        String completeEventJson = gson.toJson(standardEvent);
                        eventData.put("details", completeEventJson);
                        Logger.info("TectonicCheckoutKit", "Successfully serialized complete standard pixel event");
                    } catch (Exception e) {
                        Logger.error("TectonicCheckoutKit", "Error serializing complete standard pixel event", e);
                        // Fallback to basic JSON
                        eventData.put("details", "{\"name\":\"" + (standardEvent.getName() != null ? standardEvent.getName() : "unknown") + "\"}");
                    }
                } catch (Exception e) {
                    Logger.error("TectonicCheckoutKit", "Error processing standard pixel event", e);
                    eventData.put("type", "standard");
                    eventData.put("timestamp", System.currentTimeMillis());
                    eventData.put("name", "unknown");
                    eventData.put("details", "{}");
                }
                
                return eventData;
            }
            
            private JSObject processCustomPixelEvent(CustomPixelEvent customEvent) {
                JSObject eventData = new JSObject();
                Gson gson = new Gson();
                
                try {
                    eventData.put("type", "custom");
                    eventData.put("timestamp", System.currentTimeMillis());
                    eventData.put("name", customEvent.getName() != null ? customEvent.getName() : "unknown");
                    
                    // Serialize the complete CustomPixelEvent object to JSON
                    try {
                        String completeEventJson = gson.toJson(customEvent);
                        eventData.put("details", completeEventJson);
                        Logger.info("TectonicCheckoutKit", "Successfully serialized complete custom pixel event");
                    } catch (Exception e) {
                        Logger.error("TectonicCheckoutKit", "Error serializing complete custom pixel event", e);
                        // Fallback to basic JSON
                        eventData.put("details", "{\"name\":\"" + (customEvent.getName() != null ? customEvent.getName() : "unknown") + "\"}");
                    }
                } catch (Exception e) {
                    Logger.error("TectonicCheckoutKit", "Error processing custom pixel event", e);
                    eventData.put("type", "custom");
                    eventData.put("timestamp", System.currentTimeMillis());
                    eventData.put("name", "unknown");
                    eventData.put("details", "{}");
                }
                
                return eventData;
            }

            @Override
            public void onWebPixelEvent(PixelEvent event) {
                // Called when a web pixel event is emitted in checkout.
                // Use this to submit events to your analytics system.
                Logger.info("TectonicCheckoutKit", "Web pixel event: " + event.getName());
                
                JSObject eventData;
                
                try {
                    // Handle different pixel event types using separate functions
                    if (event instanceof StandardPixelEvent) {
                        eventData = processStandardPixelEvent((StandardPixelEvent) event);
                    } else if (event instanceof CustomPixelEvent) {
                        eventData = processCustomPixelEvent((CustomPixelEvent) event);
                    } else {
                        // Fallback for unknown pixel event types
                        eventData = new JSObject();
                        Gson gson = new Gson();
                        eventData.put("type", "unknown");
                        eventData.put("timestamp", event.getTimestamp());
                        eventData.put("name", event.getName());
                        try {
                            eventData.put("details", gson.toJson(event));
                        } catch (Exception e) {
                            eventData.put("details", event.toString());
                        }
                    }
                } catch (Exception e) {
                    Logger.error("TectonicCheckoutKit", "Error extracting pixel event data", e);
                    eventData = new JSObject();
                    eventData.put("type", "unknown");
                    eventData.put("timestamp", System.currentTimeMillis());
                    eventData.put("name", "unknown");
                    eventData.put("details", "{}");
                }
                
                plugin.forwardEvent("pixel", eventData);
            }

            @Override
            public boolean onShowFileChooser(
                WebView webView,
                ValueCallback<Uri[]> filePathCallback,
                FileChooserParams fileChooserParams
            ) {
                // Called to tell the client to show a file chooser. This is called to handle HTML forms with 'file' input type,
                // in response to the customer pressing the "Select File" button.
                // To cancel the request, call filePathCallback.onReceiveValue(null) and return true.
                Logger.info("TectonicCheckoutKit", "File chooser requested");
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                // Called to tell the client to show a geolocation permissions prompt as a geolocation permissions
                // request has been made.
                // Invoked for example if a customer uses `Use my location` for pickup points
                Logger.info("TectonicCheckoutKit", "Geolocation permissions prompt requested for origin: " + origin);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onGeolocationPermissionsHidePrompt() {
                // Called to tell the client to hide the geolocation permissions prompt, e.g. as the request has been cancelled
                Logger.info("TectonicCheckoutKit", "Geolocation permissions prompt hidden");
                super.onGeolocationPermissionsHidePrompt();
            }

            @Override
            public void onPermissionRequest(PermissionRequest permissionRequest) {
                // Called when a permission has been requested, e.g. to access the camera
                // implement to grant/deny/request permissions.
                Logger.info("TectonicCheckoutKit", "Permission requested: " + permissionRequest.toString());
                super.onPermissionRequest(permissionRequest);
            }
        };

        // Ensure we're on the main thread
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ShopifyCheckoutSheetKit.present(checkoutUrl, activity, processor);
            }
        });
    }
}
