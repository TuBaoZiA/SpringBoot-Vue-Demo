<template>
  <div class="login_wrapper">
    <div class="login">
      <el-form :model="loginForm">
        <el-form-item style="height: 55px;">
          <h2 class="title">后台管理系统</h2>
        </el-form-item>
        <el-form-item prop="username">

          <el-input v-model="loginForm.username" name="username" placeholder="账号"></el-input>
        </el-form-item>

        <el-form-item prop="password">

          <el-input
            :type="pwdType"
            v-model="loginForm.password"
            name="password"
            placeholder="密码"
            @keyup.enter.native="handleLogin"></el-input>
            <span class="show-pwd" @click="showPwd">
              <svg-icon icon-class="eye" />
            </span>
        </el-form-item>

        <el-form-item>
          <el-button :loading="loading" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
            登陆
          </el-button>
        </el-form-item>

      </el-form>

    </div>
  </div>
</template>

<script>
  export default {
    name: 'Login',
    data() {
      return {
        loginForm: {   //表单对象
          username: 'admin',
          password: 'admin'
        },
        loading: false,
        pwdType: 'password',
        redirect: undefined
      }
    },
    watch: {
      $route: {
        handler: function(route) {
          this.redirect = route.query && route.query.redirect
        },
        immediate: true
      }
    },
    methods: {
      showPwd() {
        if (this.pwdType === 'password') {
          this.pwdType = ''
        } else {
          this.pwdType = 'password'
        }
      },
      handleLogin() {
        this.loading = true
        this.$store.dispatch('Login', this.loginForm).then(() => {
          this.loading = false
          this.$router.push({ path: this.redirect || '/' })
        }).catch(() => {
          this.loading = false
        })
      }
    }
  }
</script>

<style lang="scss">
  $input_width:300px;

  .login_wrapper {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    .login {
      width: 460px;
      height: 296px;
      margin-top: -150px;
      border: 1px solid #eaeaea;
      box-shadow: 0 0 25px #cac6c6;
      .title {
        text-align: center;
        color: #505458;
      }
      .el-form-item__content {
        width: $input_width;
      }
      .el-button {
        width: $input_width;
      }
      .el-form {
        margin: 10px 80px auto 80px;
        .error {
          display: block;
          text-align: center;
          color: red;
        }
      }
    }

    /*.svg-container {
      padding: 6px 5px 6px 15px;
      color: #889aa4;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
    }*/

    .show-pwd {
      position: absolute;
      right: 10px;
      top: 1px;
      font-size: 16px;
      color: #889aa4;
      cursor: pointer;
      user-select: none;
    }
  }
</style>
