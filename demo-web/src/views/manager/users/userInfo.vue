<template>
  <div class="info">
    <el-dialog title="修改个人信息" :visible.sync="dialogInfoForm" width="510px">

      <el-form :model="temp" status-icon :rules="rules" ref="ruleForm1" label-width="80px" >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="temp.username"></el-input>
        </el-form-item>
      </el-form>

      <my-upload field="file"
                 url="/api/admin/common/upload/avatar"
                 @crop-upload-success="cropUploadSuccess"
                 v-model="imageCropperShow"
                 :width="200"
                 :height="200"
                 img-format="png"
                 :size="2.1"
                :headers="{'X-Token':this.token}"></my-upload>
      <img :src="'http://bucket-demo-01.oss-cn-hangzhou.aliyuncs.com/'+ temp.avatar" @click="imageCropperShow = !imageCropperShow" class="newImage">

      <div slot="footer" class="dialog-footer">
        <el-button @click="close">取消</el-button>
        <el-button type="primary" @click="updateUserInfo()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import {updateUserInfo} from "@/api/user"
  import myUpload from 'vue-image-crop-upload'
  import {resetUser} from "@/utils";

  export default {
    name: 'userPwd',
    props: ['dialogInfoForm'],
    components:{myUpload},
    computed: {
      ...mapGetters([
        'avatar',
        'id',
        'name',
        'token'
      ])
    },
    created(){
      this.temp.username = this.name;
      this.temp.avatar = this.avatar;
      this.temp.id = this.id;
    },
    data(){
      const validateUsername = (rule, value, callback) => {
        if (this.temp.username === '') {
          callback(new Error('请输入用户名'));
        } else {
          callback();
        }
      };
      return {
        imageCropperShow: false,
        temp:{
          id: undefined,
          username: '',
          avatar: ''
        },
        rules: {
          username: [{ validator: validateUsername, trigger: 'blur'}]
        }
      }
    },
    methods: {
      close(){
        this.$emit("on-close");
      },
      cropUploadSuccess(jsonData){
        this.temp.avatar = jsonData.data;
      },
      updateUserInfo(){
        this.$refs['ruleForm1'].validate((valid) => {
          if (valid) {
            updateUserInfo(this.temp).then(response => {
              this.$notify({
                title: '成功',
                message: '修改成功',
                type: 'success',
                duration: 2000
              });

              this.$store.dispatch('UpdateAvatar', this.temp.avatar).then(() => {}).catch(() => {});

              this.close();
            }).catch(() => {});

            resetUser();

          }
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .newImage{
    width: 200px;
    background-size: 100%;
    border-radius: 50%;
    position: relative;
    left: 150px;
  }
</style>

