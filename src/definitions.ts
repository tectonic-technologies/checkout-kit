export interface TectonicCheckoutKitPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
