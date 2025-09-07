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

type TectonicCheckoutKitPlugin = {
  present: (
    options: TectonicCheckoutKitPresentOptions
  ) => Promise<TectonicCheckoutKitPresentResult>;

  preload: (
    options: TectonicCheckoutKitPreloadOptions
  ) => Promise<TectonicCheckoutKitPreloadResult>;
};

export type {
  TectonicCheckoutKitPlugin,
  TectonicCheckoutKitPreloadOptions,
  TectonicCheckoutKitPreloadResult,
  TectonicCheckoutKitPresentOptions,
  TectonicCheckoutKitPresentResult
};
