<script>
  import CategoriesBar from '$lib/CategoriesBar.svelte';
  import ProductCard from '$lib/ProductCard.svelte';

  export let data; 
</script>

<CategoriesBar categories={data.categories} />

{#each data.products as product}
  <ProductCard {product} imageSrc=undefined/>
{/each}