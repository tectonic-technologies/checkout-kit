import { WebPlugin } from '@capacitor/core';

import type { TectonicCheckoutKitPlugin } from './definitions';

export class TectonicCheckoutKitWeb extends WebPlugin implements TectonicCheckoutKitPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
