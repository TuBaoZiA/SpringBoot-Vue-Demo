<template>
  <div class="app-container">

    <div class="filter-container">
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">新增菜单</el-button>
    </div>

    <tree-table :data="data" :columns="columns" border>

      <el-table-column label="图标">
        <template slot-scope="scope">
          <svg-icon v-if="scope.row.meta.icon !== undefined && scope.row.meta.icon != null " :icon-class="scope.row.meta.icon" />
        </template>
      </el-table-column>

      <el-table-column label="权限码">
        <template slot-scope="scope">
          {{ scope.row.meta.permission }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="270">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" size="small" @click.native="handleCreateChildren(scope.$index, scope.row)">添加子菜单</el-button>
          <el-button type="text" icon="el-icon-edit" size="small" @click.native="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="text" icon="el-icon-delete" size="small" style="color: #ff0000" @click.native="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>

    </tree-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">

      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 93%;margin-left: 25px">

        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="父类目">
              <el-select
                v-model="temp.parentId"
                clearable style="width: 258px"
                placeholder="请选择">
                <el-option
                  v-for="item in menusDrop"
                  :key="item.id"
                  :label="item.label"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="name" prop="name">
              <el-input v-model="temp.name"></el-input>
            </el-form-item>
          </el-col>

        </el-row>

        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="url" prop="path">
              <el-input v-model="temp.path"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="组件名称">
              <el-input v-model="temp.component"></el-input>
            </el-form-item>
          </el-col>

        </el-row>

        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="菜单图标">
              <svg-icon v-if="temp.icon !== undefined && temp.icon != null " :icon-class="temp.icon" />
              <button type="button" class="el-button el-button--text" @click="iconDialog = !iconDialog">选择</button>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="redirect">
              <el-input v-model="temp.redirect"></el-input>
            </el-form-item>
          </el-col>

        </el-row>

        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="排序号">
              <el-input v-model="temp.order"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="菜单权限">
              <el-select
                v-model="temp.meta.permissionId"
                clearable style="width: 258px"
                placeholder="请选择">
                <el-option
                  v-for="item in permissionsDrop"
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

    <icon-dialog @on-choose="chooseIcon" @on-close="iconDialog=false" :icon-dialog="iconDialog" />
  </div>
</template>

<script>

  import treeTable from '@/components/TreeTable'
  import {deleteById, saveMenu, selectCount, selectMenus, updateMenu} from "@/api/menu";
  import {menusDrop, permissionsDrop} from "@/api/common";
  import {deleteConfirm, successNotify} from "@/utils";
  import iconDialog from '@/views/svg-icons/icon-dialog'

  export default {
    name: 'TreeTableDemo',
    components: { treeTable, iconDialog },
    data() {
      const validateName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入菜单名称'));
        } else {
          selectCount(value, this.temp.id).then(res => {
            if(res.data > 0) {
              callback(new Error('菜单名称已存在！'));
            }else{
              callback();
            }
          }).catch(() => {});
        }
      };

      return {
        columns: [
          {text: '菜单名称', value: 'name'},
          {text: '排序号', value: 'order'},
          {text: '组件', value: 'component'}
        ],
        data:[],
        permissionsDrop: [],
        menusDrop: [],

        dialogFormVisible: false,
        iconDialog: false,
        dialogStatus: '',
        textMap: {
          update: 'Edit',
          create: 'Create'
        },

        temp:{
          id:undefined,
          parentId: undefined,
          name: '',
          path: '',
          component: '',
          redirect: '',
          meta:{
            permissionId: undefined,
            title: ''
          },
          icon: '',
          order: '',
        },

        rules: {
          name: [{ required: true, validator: validateName, trigger: 'blur' }],
          path: [{ required: true, message: '请输入菜单URL', trigger: 'blur' }],
        },
      }
    },
    created() {
      //获取列表数据
      this.getList();
      this.getPermissionsDrop();
    },
    methods:{
      chooseIcon(item){
        this.iconDialog = false;
        this.temp.icon=item;
      },
      getList() {
        selectMenus().then(response => {
          this.data = response.data;
        }).catch(() => {})
      },

      getPermissionsDrop(){
        permissionsDrop().then(res => {
          this.permissionsDrop = res.data;
        }).catch(() => {})
      },

      getMenusDrop(id, keepParent){
        menusDrop(id === undefined ? this.temp.id : id, keepParent).then(res => {
          this.menusDrop = res.data;
        }).catch(() => {})
      },

      resetTemp(){
        this.temp = { id: undefined,parentId: undefined,name: '',path: '',component: '',redirect: '', order: '', meta:{
            icon:'', permissionId: undefined, title: ''
          }}
      },

      resetDialog(create){
        if(create){
          this.resetTemp();
        }
        this.dialogStatus = create ? 'create' : 'update';
        this.dialogFormVisible = true;
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },

      handleEdit(index, row) {
        this.temp = Object.assign({}, row);
        this.temp.icon = this.temp.meta.icon;
        this.temp.children = null;
        this.temp.parent = null;
        this.getMenusDrop();
        this.resetDialog(false)
      },

      updateData(){
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.temp.meta.title = this.temp.name;
            this.temp.meta.icon = this.temp.icon;
            updateMenu(this.temp).then((response) => {
              this.getList();
              this.dialogFormVisible = false;
              successNotify('更新成功！')
            });
          }
        })
      },

      handleCreateChildren(index, row){
        this.getMenusDrop(row.id, true);
        this.resetDialog(true);
        this.temp.parentId = row.id;
      },

      handleCreate(){
        this.getMenusDrop();
        this.resetDialog(true)
      },

      createData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.temp.meta.title = this.temp.name;
            this.temp.meta.icon = this.temp.icon;
            saveMenu(this.temp).then((response) => {
              this.getList();
              this.dialogFormVisible = false;
              successNotify('创建成功');
            })
          }
        })
      },

      handleDelete(index, row) {
        deleteConfirm(() => {
          deleteById(row.id).then(response => {
            this.getList();
            successNotify('删除成功');
          }).catch(() => {})
        });
      }
    }
  }
</script>
