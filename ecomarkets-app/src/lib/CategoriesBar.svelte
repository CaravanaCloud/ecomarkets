<script lang="ts">
  import { cat_store } from "../stores";

  export let categories: any;

  cat_store.set(categories)
  cat_store.subscribe(updated_categories => categories = updated_categories)

  const handleClick = (e: any) => {
    unselectAll()
    select($cat_store.findIndex(el => el.name == e.srcElement.innerText))
    $cat_store = $cat_store
  }

  const unselectAll = () => {
    $cat_store.map(category  => category.is_selected = false)
  }

  const select = (idx: number) => {
    $cat_store[idx].is_selected = true;
  }
</script>

<nav class="bg-white p-6 mb-8 w-[100%] flex flex-row items-center justify-between">
  {#each categories as category}
    <button class="m-1 {category.is_selected ? 'font-bold text-lg' : 'text-slate-400'}" on:click={handleClick}>{category.name}</button>
  {/each}
</nav>