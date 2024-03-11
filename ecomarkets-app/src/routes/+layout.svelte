<script lang="ts">
	import '../app.postcss';
	import logo from '$lib/assets/logo.png'

	// Floating UI for Popups
	import { computePosition, autoUpdate, flip, shift, offset, arrow } from '@floating-ui/dom';
	import { storePopup } from '@skeletonlabs/skeleton';
	storePopup.set({ computePosition, autoUpdate, flip, shift, offset, arrow });
</script>

<nav class="flex justify-between items-center bg-orange-100">
	<a href="/" class="block w-[196px]">
		<img src={logo} alt="Logo da Rede Bem Viver" />
	</a>

	<a href="/produtos" class="bg-white p-2 mr-4">Produtos</a>
</nav>
<slot />
