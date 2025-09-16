import type { Plugin } from '@capacitor/core';

type TectonicCheckoutKitPresentOptions = {
  checkoutUrl: string;
  // TODO: add other support options here as needed.
  // https://github.com/Shopify/checkout-sheet-kit-react-native/tree/main#configuration
};

type TectonicCheckoutKitPreloadOptions = {
  checkoutUrl: string;
};

type TectonicCheckoutKitPresentResult = {
  status: 'presented' | 'dismissed';
};

type TectonicCheckoutKitPreloadResult = {
  status: 'preloaded' | 'ignored';
};

// Event types with structured data
type CheckoutCompletedEvent = {
  orderDetails: string; // Serialized OrderDetails from Shopify checkout kit
  timestamp: number;
};

type CheckoutCanceledEvent = {
  reason: string;
  timestamp: number;
};

type CheckoutFailedEvent = {
  error: {
    message: string;
    errorCode: string;
    timestamp: number;
    cause?: string;
  };
};

type CheckoutLinkClickedEvent = {
  uri: string;
  timestamp: number;
};

type CheckoutPixelEvent = {
  type: 'standard' | 'custom' | 'unknown'; // pixel event type
  timestamp: number; // event.timestamp
  name: string; // event.name
  details: string; // event data as JSON string
};

type ShopifyCheckoutEventName =
  | 'canceled'
  | 'completed'
  | 'failed'
  | 'linkClicked'
  | 'pixel';

type ShopifyCheckoutEventMap = {
  canceled: CheckoutCanceledEvent;
  completed: CheckoutCompletedEvent;
  failed: CheckoutFailedEvent;
  linkClicked: CheckoutLinkClickedEvent;
  pixel: CheckoutPixelEvent;
};

type ShopifyCheckoutEventListener<T extends ShopifyCheckoutEventName> = (
  event: ShopifyCheckoutEventMap[T]
) => void;

type TectonicCheckoutKitEventSubscription = {
  remove: () => void;
};

interface TectonicCheckoutKitPlugin extends Plugin {
  present: (
    options: TectonicCheckoutKitPresentOptions
  ) => Promise<TectonicCheckoutKitPresentResult>;

  preload: (
    options: TectonicCheckoutKitPreloadOptions
  ) => Promise<TectonicCheckoutKitPreloadResult>;

  invalidate(): Promise<void>;
}

export type {
  CheckoutCanceledEvent,
  CheckoutCompletedEvent,
  CheckoutFailedEvent,
  CheckoutLinkClickedEvent,
  CheckoutPixelEvent,
  ShopifyCheckoutEventListener,
  ShopifyCheckoutEventMap,
  ShopifyCheckoutEventName,
  TectonicCheckoutKitEventSubscription,
  TectonicCheckoutKitPlugin,
  TectonicCheckoutKitPreloadOptions,
  TectonicCheckoutKitPreloadResult,
  TectonicCheckoutKitPresentOptions,
  TectonicCheckoutKitPresentResult
};
