<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input placeholder="用户名" v-model="listQuery.username" style="width: 200px;" class="filter-item" @keyup.enter.native="handleSearch"/>

      <el-select
        v-model="listQuery.roles"
        multiple
        collapse-tags
        class="filter-item"
        style="width: 185px;"
        placeholder="根据角色查找">
        <el-option
          v-for="item in rolesDrop"
          :key="item.id"
          :label="item.label"
          :value="item.id">
        </el-option>
      </el-select>

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      style="width: 100%">

      <el-table-column type="expand" class="demo-table-expand">
        <template slot-scope="props">
          <el-form label-position="left" inline>

            <el-row style="color: #99a9bf;" v-for="role in props.row.roleList" :key="role.id">

              <el-col :span="6">
                <el-form-item label="角色ID">
                  <span>{{ role.id }}</span>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item label="角色名称">
                  <span>{{ role.name }}</span>
                </el-form-item>
              </el-col>

            </el-row>

          </el-form>
        </template>
      </el-table-column>

      <el-table-column align="center" label="ID">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>

      <el-table-column prop="username" label="用戶名" min-width="100" />

      <el-table-column label="创建日期" align="center" min-width="145">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
        </template>
      </el-table-column>

      <el-table-column label="修改日期" align="center" min-width="145">
        <template slot-scope="scope">
          {{ scope.row.updateTime}}
        </template>
      </el-table-column>

      <el-table-column label="操作" min-width="180" align="center">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" size="small" @click.native="handleEdit(scope.$index, scope.row)">配置角色</el-button>
          <el-button type="text" icon="el-icon-delete" size="small" style="color: #ff0000" @click.native="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>

      </el-table-column>

    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">

      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 93%;margin-left: 25px">

        <el-row :gutter="20">

          <el-col :span="12" v-if="dialogStatus==='create'">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="temp.username"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="角色" prop="roles">
          <el-select
            v-model="temp.roles"
            multiple
            collapse-tags
            class="filter-item"
            placeholder="选择角色">
            <el-option
              v-for="item in rolesDrop"
              :key="item.id"
              :label="item.label"
              :value="item.id">
            </el-option>
          </el-select>
            </el-form-item>
          </el-col>

        </el-row>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import waves from '../../../directive/waves'
  import Pagination from '../../../components/Pagination'
  import {deleteById, saveUser, selectUsers, updateUserRole, selectCount} from "../../../api/user";
  import {rolesDrop} from "../../../api/common";
  import {deleteConfirm, successNotify} from "../../../utils";

  export default {
    directives: { waves },
    // 自定义分页组件
    components: { Pagination },

    data() {
      const validateUserName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入角色名称'));
        } else {
          selectCount(value, this.temp.id).then(res => {
            if(res.data > 0) {
              callback(new Error('用户名称已存在！'));
            }else{
              callback();
            }
          }).catch(() => {});
        }
      };

      const validateRoles = (rule, value, callback) =>{
          if(value.length == 0 ){
            callback(new Error('请给该用户分配一个角色！'))
          }else{
            callback();
          }
      };

      return {

        rolesDrop: [],

        listQuery: {
          page: 1,
          limit: 10,
          username : '',
          roles: []
        },

        temp: {
          id: undefined,
          username: '',
          roles: []
        },

        rules: {
          username: [{ validator: validateUserName, trigger: 'blur' }],
          roles: [{validator: validateRoles, trigger: 'blur'}]
        },

        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: 'Edit',
          create: 'Create'
        },

        total : 0,
        list: [],
        listLoading: false
      }
    },

    // 在模板渲染成html前调用
    created() {
      //获取列表数据
      this.getList();

      this.getRolesDrop();
    },
    methods: {
      getRolesDrop(){
        rolesDrop().then(res => {
          this.rolesDrop = res.data;
        }).catch(() => {})
      },

      getList() {
        this.listLoading = true;
        selectUsers(this.listQuery).then(response => {
          this.list = response.data;
          this.total = response.count;
          this.listLoading = false
        }).catch(() => {
          this.listLoading = false
        })
      },

      handleSearch(){
        this.listQuery.page = 1;
        this.getList()
      },

      handleCreate() {
        this.resetTemp();
        this.dialogStatus = 'create';
        this.dialogFormVisible = true;
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },

      resetTemp(){
        this.temp = { id: undefined, username: '', roles : []}
      },

      createData(){
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            saveUser(this.temp).then((response) => {
              this.getList();
              this.dialogFormVisible = false;
              successNotify('创建成功')
            })
          }
        })
      },

      updateData(){
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            updateUserRole(this.temp).then(() => {
              this.getList();
              this.dialogFormVisible = false;
              successNotify('更新成功')
            })
          }
        })
      },

      handleEdit(index, row) {
        this.temp = Object.assign({}, row);
        this.temp.roles = []; /** Cannot read property 'push' of undefined */

        this.temp.roleList.forEach(role =>{
          this.temp.roles.push(role.id)
        });

        this.dialogStatus = 'update';
        this.dialogFormVisible = true;
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },

      handleDelete(index, row) {
        deleteConfirm(() => {
          deleteById(row.id).then(response => {
            successNotify('删除成功');
            this.list.splice(index, 1)
          }).catch(() => {})
        });
      }
    }
  }
</script>
