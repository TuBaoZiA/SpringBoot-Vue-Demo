<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input placeholder="角色名" v-model="listQuery.roleName" style="width: 200px;" class="filter-item" @keyup.enter.native="handleSearch"/>

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

            <el-row style="color: #99a9bf;" v-for="permission in props.row.permissions" :key="permission.id">

              <el-col :span="6">
                <el-form-item label="权限Id">
                  <span>{{ permission.id }}</span>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item label="权限code">
                  <span>{{ permission.code }}</span>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item label="角色描述">
                  <span>{{ permission.desc }}</span>
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

      <el-table-column prop="name" label="角色名" min-width="100" />

      <el-table-column label="操作" min-width="180" align="center">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" size="small" @click.native="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="text" icon="el-icon-delete" size="small" style="color: #ff0000" @click.native="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>

      </el-table-column>

    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">

      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 93%;margin-left: 25px">

        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="角色名" prop="name">
              <el-input v-model="temp.name"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
          <el-select
            v-model="temp.permissionIds"
            multiple
            collapse-tags
            class="filter-item"
            placeholder="分配权限">
            <el-option
              v-for="item in permissionsDrop"
              :key="item.id"
              :label="item.label"
              :value="item.id">
            </el-option>
          </el-select>
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
  import waves from '@/directive/waves'
  import Pagination from '@/components/Pagination'
  import {permissionsDrop } from "@/api/common";
  import {deleteById, saveRole, selectCount, selectRoles, updateRole} from "@/api/role";
  import {deleteConfirm, successNotify} from "@/utils";

  export default {
    directives: { waves },
    // 自定义分页组件
    components: { Pagination },

    data() {
      const validateName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入角色名称'));
        } else {
          selectCount(value, this.temp.id).then(res => {
            if(res.data > 0) {
              callback(new Error('角色名称已存在！'));
            }else{
              callback();
            }
          }).catch(() => {});
        }
      };

      return {

        permissionsDrop: [],

        listQuery: {
          page: 1,
          limit: 10,
          roleName : ''
        },

        temp: {
          id: undefined,
          name: '',
          permissionIds: []
        },

        rules: {
          name: [{ validator: validateName, trigger: 'blur' }]
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

      this.getPermissionsDrop();
    },
    methods: {
      getPermissionsDrop(){
        permissionsDrop().then(res => {
          this.permissionsDrop = res.data;
        }).catch(() => {})
      },

      getList() {
        this.listLoading = true;
        selectRoles(this.listQuery).then(response => {
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
        this.temp = { id: undefined, name: '', permissionIds : []}
      },

      createData(){
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            saveRole(this.temp).then((response) => {
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
            updateRole(this.temp).then(() => {
              this.getList();
              this.dialogFormVisible = false;
              successNotify('更新成功')
            })
          }
        })
      },

      handleEdit(index, row) {
        this.temp = Object.assign({}, row);
        this.temp.permissionIds = []; /** Cannot read property 'push' of undefined */

        this.temp.permissions.forEach(permission =>{
          this.temp.permissionIds.push(permission.id)
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
