import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

export default defineConfig({
  define: {
    'process.env': {},
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // 后端服务器地址
        changeOrigin: true,
        configure(proxy,options){
          proxy.on("proxyReq",(proxyReq)=>{
            proxyReq.removeHeader("origin");
          });
        },
        rewrite: path => path.replace(/^\/api/, '')
      }
    }
  },
  plugins: [vue()],
  resolve: {
    alias: {
      "@": "/src", // 确保 `@/xxx` 解析正确
    },
  },
});
