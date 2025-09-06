import { TectonicCheckoutKit } from '@tectonic-technologies/checkout-kit';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    TectonicCheckoutKit.echo({ value: inputValue })
}
