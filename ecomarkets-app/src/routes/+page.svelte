<script>
	let title = 'EcoMarkets responsivo';
  let counter = 0;
  function handleClick() {
    counter += 1;
  }
</script>
<div class="flex flex-col h-screen justify-between items-center">
  <main class="text-xl">
    <div>
      <div class="counter">Count: {counter}</div>
      <button on:click={handleClick}>Increment</button>
    </div>
  </main>
</div>
