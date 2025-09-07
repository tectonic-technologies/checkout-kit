import { WebPlugin } from '@capacitor/core';

import type {
  TectonicCheckoutKitPlugin,
  TectonicCheckoutKitPreloadOptions,
  TectonicCheckoutKitPreloadResult,
  TectonicCheckoutKitPresentOptions,
  TectonicCheckoutKitPresentResult
} from './definitions';

class TectonicCheckoutKitWeb
  extends WebPlugin
  implements TectonicCheckoutKitPlugin
{
  present = async ({
    checkoutUrl
  }: TectonicCheckoutKitPresentOptions): Promise<TectonicCheckoutKitPresentResult> => {
    window.location.href = checkoutUrl;
    return { status: 'presented' };
  };

  preload = async (
    _options: TectonicCheckoutKitPreloadOptions
  ): Promise<TectonicCheckoutKitPreloadResult> => {
    return { status: 'ignored' };
  };
}

export { TectonicCheckoutKitWeb };
