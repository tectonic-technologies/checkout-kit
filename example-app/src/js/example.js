import { TectonicCheckoutKit } from '@tectonic-technologies/checkout-kit';

// Setup event listeners
const setupEventListeners = () => {
    // Listen for checkout completion
    TectonicCheckoutKit.addListener('completed', (event) => {
        console.log('Checkout completed event:', event);
    });

    // Listen for checkout close/cancel
    TectonicCheckoutKit.addListener('close', (event) => {
        console.log('=== CLOSE EVENT RECEIVED ===');
        console.log('Checkout close event:', event);
        console.log('Event keys:', Object.keys(event));
        console.log('Raw event:', event.rawEvent);
        console.log('=== END CLOSE EVENT ===');
    });

    // Listen for checkout errors
    TectonicCheckoutKit.addListener('error', (event) => {
        console.log('Checkout error event:', event);
    });

    // Listen for pixel events
    TectonicCheckoutKit.addListener('pixel', (event) => {
        console.log('Checkout pixel event:', event);
    });
};

// Initialize event listeners when the module loads
setupEventListeners();

// Present checkout
globalThis.presentCheckout = async () => {
    // https://nutritionfaktory.com/checkouts/cn/hWN1R5q15Xg1MXslc0zXGpgS/en-us?auto_redirect=false&edge_redirect=true&skip_shop_pay=true
    // const checkoutUrl = document.getElementById("checkoutUrlInput").value;
    // if (!checkoutUrl) {
    //     logEvent('Please enter a checkout URL');
    //     return;
    // }

    try {
        const checkoutUrl = 'https://shop.vaaree.com/checkouts/cn/hWN328t2cl62Uv3mFt4BrUuq/en-in'
        const result = await TectonicCheckoutKit.present({ checkoutUrl });
        console.log('Present result:', result)
    } catch (error) {
        console.error('Present failed:', error);
    }
};