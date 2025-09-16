import Foundation
import Capacitor
import ShopifyCheckoutSheetKit

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(TectonicCheckoutKitPlugin)
public class TectonicCheckoutKitPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "TectonicCheckoutKitPlugin"
    public let jsName = "TectonicCheckoutKit"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "present", returnType: CAPPluginReturnPromise)
    ]
    // private let implementation = TectonicCheckoutKit()
    private lazy var implementation = TectonicCheckoutKit(plugin: self)

    @objc func present(_ call: CAPPluginCall) {
        guard let checkoutUrl = call.getString("checkoutUrl"),
              let url = URL(string: checkoutUrl) else {
            call.reject("Invalid checkout URL")
            return
        }
        
        DispatchQueue.main.async {
            guard let viewController = self.bridge?.viewController else {
                call.reject("Unable to get view controller")
                return
            }
            
            print("TectonicCheckoutKit: Presenting checkout with URL: \(url)")
            print("TectonicCheckoutKit: Using view controller: \(viewController)")
            
            ShopifyCheckoutSheetKit.present(checkout: url, from: viewController, delegate: self.implementation)
            
            // Resolve immediately - the presentation itself should work
            call.resolve([
                "status": "presented"
            ])
        }
    }
}
