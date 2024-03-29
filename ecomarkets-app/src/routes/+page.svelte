<script>
  let counter = 0;
  function handleClick() {
    counter += 1;
  }
</script>
<div >
    <div>
      <div class="counter">Count: {counter}</div>
      <button on:click={handleClick}>Increment</button>
    </div>
</div>
