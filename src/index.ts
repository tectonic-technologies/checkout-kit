import { registerPlugin } from '@capacitor/core';

import type { TectonicCheckoutKitPlugin } from './definitions';

const TectonicCheckoutKit = registerPlugin<TectonicCheckoutKitPlugin>(
  'TectonicCheckoutKit',
  {
    web: () => import('./web').then(m => new m.TectonicCheckoutKitWeb())
  }
);

export * from './definitions';

export { TectonicCheckoutKit };
