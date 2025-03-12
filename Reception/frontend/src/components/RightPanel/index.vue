<template>
  <div ref="rightPanel" :class="{show: show}" class="rightPanel-container">
    <div class="rightPanel-background" />
    <div class="rightPanel">
      <div class="handle-button" :style="{'top': buttonTop+'px','background-color': theme}" @click="show=!show">
        <i :class="show?'el-icon-close':'el-icon-setting'" />
      </div>
      <div class="rightPanel-items">
        <slot />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RightPanel',
  props: {
    buttonTop: {
      default: 250,
      type: Number
    },
    theme: {
      default: '#409EFF',
      type: String
    }
  },
  data() {
    return {
      show: false
    }
  },
  watch: {
    show(value) {
      if (value && !this.hasEventListener) {
        this.addEventListenerOnScroll()
      }
      if (value) {
        this.addClass(document.body, 'showRightPanel')
      } else {
        this.removeClass(document.body, 'showRightPanel')
      }
    }
  },
  mounted() {
    this.insertToBody()
  },
  beforeDestroy() {
    const elx = this.$refs.rightPanel
    elx.remove()
  },
  methods: {
    addEventListenerOnScroll() {
      window.addEventListener('scroll', this.handleScroll, true)
      this.hasEventListener = true
    },
    removeEventListenerOnScroll() {
      window.removeEventListener('scroll', this.handleScroll, true)
      this.hasEventListener = false
    },
    handleScroll(e) {
      const scrollTop = e.target.scrollTop
      this.$emit('scroll', scrollTop)
    },
    insertToBody() {
      const elx = this.$refs.rightPanel
      const body = document.querySelector('body')
      body.insertBefore(elx, body.firstChild)
    },
    addClass(element, className) {
      if (!element || !className) return
      let classString = element.className
      const nameIndex = classString.indexOf(className)
      if (nameIndex === -1) {
        classString += '' + className
      }
      element.className = classString
    },
    removeClass(element, className) {
      if (!element || !className) return
      let classString = element.className
      const nameIndex = classString.indexOf(className)
      if (nameIndex !== -1) {
        classString = classString.substr(0, nameIndex) + classString.substr(nameIndex + className.length)
      }
      element.className = classString
    }
  }
}
</script>

<style>
.showRightPanel {
  overflow: hidden;
  position: relative;
  width: calc(100% - 15px);
}
</style>

<style lang="scss" scoped>
.rightPanel-background {
  position: fixed;
  top: 0;
  left: 0;
  opacity: 0;
  transition: opacity .3s cubic-bezier(.7, .3, .1, 1);
  background: rgba(0, 0, 0, .2);
  z-index: -1;
}

.rightPanel {
  width: 100%;
  max-width: 260px;
  height: 100vh;
  position: fixed;
  top: 0;
  right: 0;
  box-shadow: 0px 0px 15px 0px rgba(0, 0, 0, .05);
  transition: all .25s cubic-bezier(.7, .3, .1, 1);
  transform: translate(100%);
  background: #fff;
  z-index: 40000;
}

.show {
  transition: all .3s cubic-bezier(.7, .3, .1, 1);

  .rightPanel-background {
    z-index: 20000;
    opacity: 1;
    width: 100%;
    height: 100%;
  }

  .rightPanel {
    transform: translate(0);
  }
}

.handle-button {
  width: 48px;
  height: 48px;
  position: absolute;
  left: -48px;
  text-align: center;
  font-size: 24px;
  border-radius: 6px 0 0 6px !important;
  z-index: 0;
  pointer-events: auto;
  cursor: pointer;
  color: #fff;
  line-height: 48px;
  i {
    font-size: 24px;
    line-height: 48px;
  }
}
</style> 