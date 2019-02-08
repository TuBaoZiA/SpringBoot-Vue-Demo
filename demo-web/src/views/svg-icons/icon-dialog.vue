<template>
  <div class="icons-container">
    <el-dialog title="选择图标" :visible.sync="iconDialog"  :before-close="handleClose" width="510px">
      <div class="icon-item" v-for="item of iconsMap" :key="item" @click="handleClick(item)">
        <svg-icon :icon-class="item" class-name="disabled" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import icons from './requireIcons'

  export default {
    name: 'Icons',
    props: ['iconDialog'],
    data() {
      return {
        iconsMap: icons
      }
    },
    methods: {
      handleClick(text) {
        this.$emit("on-choose", text);
      },
      handleClose(){
        this.$emit('on-close');
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .icons-container {
    margin: 20px;
    .icon-item {
      height: 50px;
      text-align: center;
      width: 50px;
      display: inline-block;
      font-size: 20px;
      color: #24292e;
      cursor: pointer;
    }
    span {
      display: block;
      font-size: 24px;
      margin-top: 10px;
    }
    .disabled{
      pointer-events: none;
    }
  }
</style>
