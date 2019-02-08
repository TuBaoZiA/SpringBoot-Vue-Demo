<template>
  <div class="container">

  <el-menu class="navbar" mode="horizontal">

    <hamburger :toggle-click="toggleSideBar" :is-active="sidebar.opened" class="hamburger-container"/>

    <breadcrumb />

    <screenfull class="screenFull right-menu-item"/>

    <el-dropdown class="avatar-container" trigger="click">
      <div class="avatar-wrapper">
        <img :src="'http://bucket-demo-01.oss-cn-hangzhou.aliyuncs.com/'+ avatar" class="user-avatar">
      </div>
      <el-dropdown-menu slot="dropdown" class="user-dropdown">
        <router-link class="inlineBlock" to="/">
          <el-dropdown-item>
            主页
          </el-dropdown-item>
        </router-link>
        <a @click="dialogFormVisible = true">
          <el-dropdown-item>
            修改密码
          </el-dropdown-item>
        </a>
        <a @click="dialogInfoForm = true ">
          <el-dropdown-item>
            个人信息
          </el-dropdown-item>
        </a>
        <el-dropdown-item divided>
          <span style="display:block;" @click="logout">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </el-menu>

  <user-pwd :dialog-form-visible="dialogFormVisible" @on-close="dialogFormVisible=false" @on-logout="logout"/>

  <user-info :dialog-info-form="dialogInfoForm" @on-close="dialogInfoForm=false"/>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '../../../components/Breadcrumb'
import Hamburger from '../../../components/Hamburger'
import Screenfull from '../../../components/Screenfull'
import userPwd from '../../../views/manager/users/userPwd'
import userInfo from '../../../views/manager/users/userInfo'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    Screenfull,
    userPwd,
    userInfo
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'id',
      'name'
    ])
  },

  data(){
    return {
      dialogFormVisible: false,
      dialogInfoForm: false
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('ToggleSideBar').then();
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .screenFull {
    position: absolute;
    right: 90px;
    top: 16px;
    color: red;
  }
  .avatar-container {
    height: 50px;
    display: inline-block;
    position: absolute;
    right: 20px;
    .avatar-wrapper {
      cursor: pointer;
      margin-top: 5px;
      position: relative;
      line-height: initial;
      .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
      }
      .el-icon-caret-bottom {
        position: absolute;
        right: -20px;
        top: 25px;
        font-size: 12px;
      }
    }
  }
}
</style>

