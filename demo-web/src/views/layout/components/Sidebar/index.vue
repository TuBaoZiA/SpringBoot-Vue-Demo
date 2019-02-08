<template>
  <el-scrollbar wrap-class="scrollbar-wrapper">
    <logo :isCollapse="isCollapse"></logo>
    <el-menu
      :show-timeout="200"
      :default-active="$route.path"
      :collapse="isCollapse"
      mode="vertical"
      background-color="#fff"
      text-color="#666"
      active-text-color="#409eff"
    >
      <sidebar-item v-for="route in menus" :key="route.path" :item="route" :base-path="route.path"/>
    </el-menu>
  </el-scrollbar>
</template>

<script>
  import { mapGetters } from 'vuex'
  import SidebarItem from './SidebarItem'
  import logo from './logo'

  export default {
    components: { SidebarItem, logo },
    computed: {
      ...mapGetters([
        'menus',
        'sidebar'
      ]),
      isCollapse() {
        return !this.sidebar.opened
      }
    }
  }
</script>
