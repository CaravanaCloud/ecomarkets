<script>
    let msg = "Hello World!"

    function doTheTrick() {
        msg = "You did the trick!"
    }
</script>
<div>
    <h1>{msg}</h1>
    <button type="button" 
        class="btn variant-filled"
        on:click={doTheTrick}>Do the Trick!</button>
</div>