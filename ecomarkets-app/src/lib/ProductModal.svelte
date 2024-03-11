<script>
  import { show_info } from "../stores";
  import closeIcon from '$lib/assets/close-icon.svg';

  export let product;
  let img = new URL(product.imagePath, import.meta.url).href;
</script>

<div aria-hidden=true on:click|self={() => show_info.set(false)} class="flex flex-col justify-center backdrop-blur-sm items-center bg-black/[0.35] w-[100%] h-[100%] absolute top-0 left-0">
  <div class="relative flex flex-col justify-center items-start bg-white w-[40%] h-[64%] rounded-lg">
    <div id="cover-image" class="absolute top-0 left-0 w-[100%] h-[35%] brightness-[1.0] z-0 bg-cover bg-center bg-no-repeat bg-clip-content rounded-t-lg" style="background-image: url({img})">
    </div>

    <button on:click={() => show_info.set(false)} class="absolute top-0 right-0 z-10"><img src={closeIcon} alt="Icone fechar" /></button>

    <h2 class="text-5xl font-bold mt-12 mb-2 z-10">{product.name}</h2>
    <div class="flex z-10">
      <p class="text-4xl">{`R$${product.price.unit},${product.price.cents}`}</p>
      <p class="text-lg self-end">/ {product.measureUnit}</p>
    </div>
    
    {#if product.description}
    <details class="mt-8 w-[100%]">
      <summary class="text-2xl">Descrição</summary>
      <p class="text-lg">{product.description}</p>
    </details>
    {/if}

    {#if product.recipeIngredients}
    <details class="mt-8 w-[100%]">
      <summary class="text-2xl">Ingredientes</summary>
      <p class="text-lg">{product.recipeIngredients}</p>
    </details>
    {/if}

    <button class="absolute bottom-2 right-1 bg-lime-500 hover:bg-lime-600 p-1 rounded-md">Adicionar à cesta</button>
  </div>
</div>
