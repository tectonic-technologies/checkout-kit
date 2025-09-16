import type { Plugin } from '@capacitor/core';

type TectonicCheckoutKitPresentOptions = {
  checkoutUrl: string;
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
type TectonicCheckoutKitCompletedEvent = {
  orderDetails: string; // Serialized OrderDetails from Shopify checkout kit
  timestamp: number;
};

type TectonicCheckoutKitCloseEvent = {
  reason: string;
  timestamp: number;
};

type TectonicCheckoutKitFailedEvent = {
  error: {
    message: string;
    errorCode: string;
    timestamp: number;
    cause?: string;
  };
};

type TectonicCheckoutKitLinkClickedEvent = {
  uri: string;
  timestamp: number;
};

type TectonicCheckoutKitPixelEvent = {
  type: 'standard' | 'custom' | 'unknown'; // pixel event type
  timestamp: number; // event.timestamp
  name: string; // event.name
  details: string; // event data as JSON string
};

type TectonicCheckoutKitEventName =
  | 'close'
  | 'completed'
  | 'failed'
  | 'linkClicked'
  | 'pixel';

interface TectonicCheckoutKitPlugin extends Plugin {
  present: (
    options: TectonicCheckoutKitPresentOptions
  ) => Promise<TectonicCheckoutKitPresentResult>;

  // TODO: Implement preload and invalidate.
  preload: (
    options: TectonicCheckoutKitPreloadOptions
  ) => Promise<TectonicCheckoutKitPreloadResult>;

  invalidate(): Promise<void>;
}

export type {
  TectonicCheckoutKitCloseEvent,
  TectonicCheckoutKitCompletedEvent,
  TectonicCheckoutKitEventName,
  TectonicCheckoutKitFailedEvent,
  TectonicCheckoutKitLinkClickedEvent,
  TectonicCheckoutKitPixelEvent,
  TectonicCheckoutKitPlugin,
  TectonicCheckoutKitPreloadOptions,
  TectonicCheckoutKitPreloadResult,
  TectonicCheckoutKitPresentOptions,
  TectonicCheckoutKitPresentResult
};
