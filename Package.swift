// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "TectonicTechnologiesCheckoutKit",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "TectonicTechnologiesCheckoutKit",
            targets: ["TectonicCheckoutKitPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0"),
        .package(url: "https://github.com/Shopify/checkout-sheet-kit-swift", from: "3.0.0")
    ],
    targets: [
        .target(
            name: "TectonicCheckoutKitPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm"),
                .product(name: "ShopifyCheckoutSheetKit", package: "checkout-sheet-kit-swift")
            ],
            path: "ios/Sources/TectonicCheckoutKitPlugin"),
        .testTarget(
            name: "TectonicCheckoutKitPluginTests",
            dependencies: ["TectonicCheckoutKitPlugin"],
            path: "ios/Tests/TectonicCheckoutKitPluginTests")
    ]
)
