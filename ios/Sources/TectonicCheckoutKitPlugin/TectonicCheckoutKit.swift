import Foundation
import ShopifyCheckoutSheetKit
import Capacitor

@objc public class TectonicCheckoutKit: NSObject {
    private weak var plugin: CAPPlugin?
    
    init(plugin: CAPPlugin) {
        self.plugin = plugin
        super.init()
    }
}

// MARK: - CheckoutDelegate
extension TectonicCheckoutKit: CheckoutDelegate {
    public func checkoutDidComplete(event: ShopifyCheckoutSheetKit.CheckoutCompletedEvent) {
        // Handle checkout completion
        print("Checkout completed with order ID: \(event.orderDetails.id)")
        
        // Create structured data for completed event to match Android implementation
        var eventData: [String: Any] = [
            "timestamp": Int(Date().timeIntervalSince1970 * 1000) // milliseconds
        ]
        
        // Serialize the complete OrderDetails object to JSON string using JSONEncoder
        do {
            let encoder = JSONEncoder()
            encoder.dateEncodingStrategy = .iso8601
            
            // Encode the entire OrderDetails object to JSON data
            let jsonData = try encoder.encode(event.orderDetails)
            
            if let jsonString = String(data: jsonData, encoding: .utf8) {
                eventData["orderDetails"] = jsonString
                print("Successfully serialized complete order details")
            } else {
                // Fallback to basic JSON if string conversion fails
                print("Failed to convert order details to string, using fallback")
                eventData["orderDetails"] = "{\"id\":\"\(event.orderDetails.id)\"}"
            }
        } catch {
            print("Error serializing complete order details: \(error)")
            // Fallback to basic JSON with just the ID
            eventData["orderDetails"] = "{\"id\":\"\(event.orderDetails.id)\"}"
        }
        
        plugin?.notifyListeners("completed", data: eventData)
    }
    
    public func checkoutDidCancel() {
        // Handle checkout cancellation
        print("TectonicCheckoutKit: checkoutDidCancel called")
        
        // Dismiss the checkout sheet
        DispatchQueue.main.async {
            if let viewController = self.plugin?.bridge?.viewController {
                if let presentedViewController = viewController.presentedViewController {
                    print("TectonicCheckoutKit: Dismissing presented view controller")
                    presentedViewController.dismiss(animated: true) {
                        print("TectonicCheckoutKit: Checkout sheet dismissed")
                    }
                } else {
                    print("TectonicCheckoutKit: No presented view controller found")
                }
            }
        }
        
        // Create structured data for canceled event to match Android implementation
        let eventData: [String: Any] = [
            "reason": "user_closed",
            "timestamp": Int(Date().timeIntervalSince1970 * 1000) // milliseconds
        ]
        
        print("TectonicCheckoutKit: Sending close event to JS")
        plugin?.notifyListeners("close", data: eventData)
        print("TectonicCheckoutKit: Close event sent")
    }
    
    public func checkoutDidFail(error: ShopifyCheckoutSheetKit.CheckoutError) {
        // Handle checkout error
        print("Checkout failed with error: \(error.localizedDescription)")
        
        // Pass error as-is as rawEvent
        let errorData: [String: Any] = [
            "rawEvent": error
        ]
        
        plugin?.notifyListeners("error", data: errorData)
    }
    
    public func checkoutDidEmitWebPixelEvent(event: ShopifyCheckoutSheetKit.PixelEvent) {
        // Handle pixel events
        print("Pixel event received")
        
        // Create structured data for pixel event to match Android implementation
        let pixelData = processPixelEvent(event)
        
        plugin?.notifyListeners("pixel", data: pixelData)
    }
    
    private func processPixelEvent(_ event: ShopifyCheckoutSheetKit.PixelEvent) -> [String: Any] {
        // Handle different pixel event types using enum pattern matching
        switch event {
        case .standardEvent(let standardEvent):
            return processStandardPixelEvent(standardEvent)
        case .customEvent(let customEvent):
            return processCustomPixelEvent(customEvent)
        @unknown default:
            // Fallback for unknown pixel event types
            return [
                "type": "unknown",
                "timestamp": Int(Date().timeIntervalSince1970 * 1000),
                "name": "unknown_event",
                "details": "{\"error\":\"unknown_pixel_event_type\"}"
            ]
        }
    }
    
    private func processStandardPixelEvent(_ standardEvent: ShopifyCheckoutSheetKit.StandardEvent) -> [String: Any] {
        var eventData: [String: Any] = [
            "type": "standard",
            "timestamp": Int(Date().timeIntervalSince1970 * 1000),
            "name": standardEvent.name ?? "unknown"
        ]
        
        do {
            // Serialize the complete StandardEvent object using JSONEncoder
            let encoder = JSONEncoder()
            encoder.dateEncodingStrategy = .iso8601
            
            let jsonData = try encoder.encode(standardEvent)
            if let jsonString = String(data: jsonData, encoding: .utf8) {
                eventData["details"] = jsonString
                print("Successfully serialized complete standard pixel event")
            } else {
                // Fallback to basic JSON if string conversion fails
                print("Failed to convert standard pixel event to string, using fallback")
                eventData["details"] = "{\"name\":\"\(standardEvent.name ?? "unknown")\"}"
            }
        } catch {
            print("Error serializing complete standard pixel event: \(error)")
            // Fallback to basic JSON
            eventData["details"] = "{\"name\":\"\(standardEvent.name ?? "unknown")\"}"
        }
        
        return eventData
    }
    
    private func processCustomPixelEvent(_ customEvent: ShopifyCheckoutSheetKit.CustomEvent) -> [String: Any] {
        var eventData: [String: Any] = [
            "type": "custom",
            "timestamp": Int(Date().timeIntervalSince1970 * 1000),
            "name": customEvent.name ?? "unknown"
        ]
        
        do {
            // Serialize the complete CustomEvent object using JSONEncoder
            let encoder = JSONEncoder()
            encoder.dateEncodingStrategy = .iso8601
            
            let jsonData = try encoder.encode(customEvent)
            if let jsonString = String(data: jsonData, encoding: .utf8) {
                eventData["details"] = jsonString
                print("Successfully serialized complete custom pixel event")
            } else {
                // Fallback to basic JSON if string conversion fails
                print("Failed to convert custom pixel event to string, using fallback")
                eventData["details"] = "{\"name\":\"\(customEvent.name ?? "unknown")\"}"
            }
        } catch {
            print("Error serializing complete custom pixel event: \(error)")
            // Fallback to basic JSON
            eventData["details"] = "{\"name\":\"\(customEvent.name ?? "unknown")\"}"
        }
        
        return eventData
    }
}
