<template>
  <div class="logo">
    <transition name="fade">
      <span v-if="isCollapse" class="logo_title is-bold is-text" key="0">
        <template>
          <img src="favicon.ico" height="20" width="20" />
        </template>
      </span>
    </transition>
    <transition-group name="fade">
      <template v-if="!isCollapse">
        <span class="logo_title is-bold is-img" key="1">
          <img src="favicon.ico" height="20" width="20" />
        </span>
        <span class="logo_subtitle" key="2">Admin</span>
      </template>
    </transition-group>
  </div>
</template>

<script>
  export default {
    name: 'logo',
    props: ['isCollapse'],
  }
</script>

<style scoped="scoped" lang="scss">
  .fade-leave-active {
    transition: opacity 0.2s;
  }
  .fade-enter-active {
    transition: opacity 2.5s;
  }
  .fade-enter,
  .fade-leave-to {
    opacity: 0;
  }
  .logo {
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    // width: 230px;
    height: 50px;
    line-height: 50px;
    background: #fff;
    color: #666;
    text-align: center;
    font-size: 20px;
    font-weight: 600;
    overflow: hidden;
    box-sizing: border-box;
  }
  .logo_title {
    padding: 0 5px 0 0;
    color: #42b983;
    font-size: 20px;
    &.is-bold {
      font-weight: 700;
    }
  }
  .is-text {
    position: absolute;
    top: 0;
    // left: 20px;
  }
  .is-img {
    position: absolute;
    top: 2px;
    left: 18px;
  }
  .logo_subtitle {
    padding-top: 5px;
    margin-left: -15px;
  }
</style>
