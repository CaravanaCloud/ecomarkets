<script>
    let title = "Welcome to EcoMarkets!"
    let time = "";
    const getTime = () => { 
        time = "" + new Date().toLocaleTimeString()
        console.debug("Time is", time)
    }
    getTime()
</script>

<div class="index">
    <h2>{title}</h2>
    <p>Eat better, live better.</p>
    <p>Time is {time}</p>
    <button on:click={getTime}>Time is {time}</button>
</div>

<style>
    .index {
        text-align: center;
        display: block;
        margin: 20px auto;
    }
</style>
