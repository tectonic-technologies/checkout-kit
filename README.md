# @tectonic-technologies/checkout-kit

A capacitor plugin for Shopify Checkout Kit

## Install

```bash
npm install @tectonic-technologies/checkout-kit
npx cap sync
```

## API

<docgen-index>

* [`invalidate()`](#invalidate)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### invalidate()

```typescript
invalidate() => Promise<void>
```

--------------------


### Type Aliases


#### TectonicCheckoutKitPresentOptions

<code>{ checkoutUrl: string; // TODO: add other support options here as needed. // https://github.com/Shopify/checkout-sheet-kit-react-native/tree/main#configuration }</code>


#### TectonicCheckoutKitPresentResult

<code>{ status: 'presented' | 'dismissed'; }</code>


#### TectonicCheckoutKitPreloadOptions

<code>{ checkoutUrl: string; }</code>


#### TectonicCheckoutKitPreloadResult

<code>{ status: 'preloaded' | 'ignored'; }</code>

</docgen-api>
