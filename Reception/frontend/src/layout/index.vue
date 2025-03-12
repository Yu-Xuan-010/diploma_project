<template>
  <div class="app-wrapper">
    <div class="main-container">
      <app-header />
      <app-main />
    </div>
  </div>
</template>

<script>
import AppHeader from './components/AppHeader'
import AppMain from './components/AppMain'

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Layout',
  components: {
    AppHeader,
    AppMain
  }
}
</script>

<style scoped>
.app-wrapper {
  height: 100%;
  width: 100%;
}

.main-container {
  min-height: 100%;
  transition: margin-left .28s;
  position: relative;
}
</style>
