import { purgeCss } from 'vite-plugin-tailwind-purgecss';
import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
	preview: {
		port: 9093
	},
	base: '/app/',
	plugins: [sveltekit(), purgeCss()]
});