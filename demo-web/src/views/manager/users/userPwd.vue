<template>
  <div class="pwd">
    <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="510px">

      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="80px" >
        <el-form-item label="原始密码" prop="sourcePass">
          <el-input type="password" v-model="ruleForm.sourcePass" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="pass">
          <el-input type="password" v-model="ruleForm.pass" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="ruleForm.checkPass" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="close">取消</el-button>
        <el-button type="primary" @click="updatePwd(id)">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import {updatePwd} from "@/api/user";

  export default {
    name: 'userPwd',
    props: ['dialogFormVisible'],
    computed: {
      ...mapGetters([
        'id'
      ])
    },
    data(){
      const validateSourcePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入原始密码'));
        } else {
          if (this.ruleForm.pass !== '') {
            this.$refs.ruleForm.validateField('pass');
          }
          callback();
        }
      };
      const validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.ruleForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      const validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        ruleForm: {
          sourcePass: '',
          pass: '',
          checkPass: ''
        },
        rules: {
          pass: [{ validator: validatePass, trigger: 'blur' }],
          sourcePass: [{ validator: validateSourcePass, trigger: 'blur' }],
          checkPass: [{ validator: validatePass2, trigger: 'blur' }]
        }
      }
    },
    methods: {
      close(){
        this.$emit("on-close");
      },
      updatePwd(id){
        this.$refs['ruleForm'].validate((valid) => {
          if (valid) {
            updatePwd(id, this.ruleForm.pass, this.ruleForm.sourcePass).then(response => {
              this.$notify({
                title: '成功',
                message: '修改成功',
                type: 'success',
                duration: 2000
              });
              this.$emit("on-logout");
            }).catch(() => {});
          } else {
            return false;
          }
        });
      },
    }
  }
</script>
