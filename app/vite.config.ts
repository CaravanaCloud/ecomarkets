import { purgeCss } from 'vite-plugin-tailwind-purgecss';
import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
	preview: {
		host: "0.0.0.0",
		port: 9093
	},
	plugins: [sveltekit(), purgeCss()]
});