import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import Icons from 'unplugin-icons/vite'
import svgLoader from 'vite-svg-loader'

// 本地https支持插件，插件启动的时候需要从Github下载文件，所以没有梯子可能无法正常工作
// import mkcert from 'vite-plugin-mkcert'

// https://vite.dev/config/
export default defineConfig({
  server: {
    host: '0.0.0.0',
    port: 3000,
    open: true,
    // 配置 HMR WebSocket，使用与主服务器相同的端口
    hmr: {
      protocol: 'ws',
      host: 'localhost',
      port: 3000
    },
    proxy: {
      // Local dev can bypass the unfinished gateway module and hit services directly.
      '/api/login': {
        changeOrigin: true,
        target: 'http://localhost:10200',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/homepage': {
        changeOrigin: true,
        target: 'http://localhost:10700',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/j1-sysc': {
        changeOrigin: true,
        target: 'http://localhost:10800',
        rewrite: (path) => path.replace(/^\/api\/j1-sysc/, '')
      },
      '/api/c1-systemparameters': {
        changeOrigin: true,
        target: 'http://localhost:10800',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/upload': {
        changeOrigin: true,
        target: 'http://localhost:10800',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/user/upload': {
        changeOrigin: true,
        target: 'http://localhost:10800',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/imy': {
        changeOrigin: true,
        target: 'http://localhost:10800',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/j2-store': {
        changeOrigin: true,
        target: 'http://localhost:10900',
        rewrite: (path) => path.replace(/^\/api\/j2-store/, '')
      },
      '/api/j3-purchase': {
        changeOrigin: true,
        target: 'http://localhost:11000',
        rewrite: (path) => path.replace(/^\/api\/j3-purchase/, '')
      },
      '/api/sale': {
        changeOrigin: true,
        target: 'http://localhost:11200',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/j5-reportmanagement': {
        changeOrigin: true,
        target: 'http://localhost:11400',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/crt': {
        changeOrigin: true,
        target: 'http://localhost:11400',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/basic_information': {
        changeOrigin: true,
        target: 'http://localhost:11500',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/account': {
        changeOrigin: true,
        target: 'http://localhost:11500',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/goods': {
        changeOrigin: true,
        target: 'http://localhost:11500',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/c2-sysbase': {
        changeOrigin: true,
        target: 'http://localhost:11500',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/customers': {
        changeOrigin: true,
        target: 'http://localhost:11500',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/suppliers': {
        changeOrigin: true,
        target: 'http://localhost:11500',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/warehouse/list': {
        changeOrigin: true,
        target: 'http://localhost:11500',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/j7-sysargs': {
        changeOrigin: true,
        target: 'http://localhost:11800',
        rewrite: (path) => path.replace(/^\/api\/j7-sysargs/, '')
      },
      '/api/finance': {
        changeOrigin: true,
        target: 'http://localhost:11900',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/report': {
        changeOrigin: true,
        target: 'http://localhost:12000',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api/wrf': {
        changeOrigin: true,
        target: 'http://localhost:12000',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/api': {
        changeOrigin: true,
        // Fallback for modules that still expect the gateway entry.
        target: 'http://localhost:10081',
        rewrite: (path) => path.replace(/^\/api/, '')
      },
      '/captcha': {
        changeOrigin: true,
        target: 'http://localhost:10200',
        rewrite: (path) => path.replace(/^\/captcha/, '')
      }
    }
  },
  build: {
    assetsDir: 'static',
    chunkSizeWarningLimit: 1000,
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes('node_modules')) {
            return id.toString().split('node_modules/')[1].split('/')[0].toString()
          }
        }
      }
    }
  },
  plugins: [
    //mkcert(),
    vue(),
    vueDevTools(),
    AutoImport({
      resolvers: [ElementPlusResolver()]
    }),
    Components({
      resolvers: [ElementPlusResolver()]
    }),
    // svg 组件化支持
    svgLoader(),
    // 自动按需加载图标
    Icons({
      compiler: 'vue3',
      scale: 1
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
