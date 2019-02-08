<template>
  <div class="app-container">

    <div class="filter-container">
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      style="width: 100%">

      <el-table-column align="center" label="ID">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>

      <el-table-column prop="resource" label="资源"/>

      <el-table-column prop="code" label="权限码" align="center" />

      <el-table-column prop="handle" label="handle" align="center" />

      <el-table-column prop="desc" label="描述" />

      <el-table-column label="操作" min-width="180" align="center">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" size="small" @click.native="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="text" icon="el-icon-delete" size="small" style="color: #ff0000" @click.native="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>

      </el-table-column>

    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">

      <el-form ref="dataForm" :model="temp" label-position="left" label-width="70px" style="width: 93%;margin-left: 25px">

        <el-row :gutter="20">

        <el-col :span="12">
          <el-form-item label="资源" prop="resource">
            <el-input v-model="temp.resource"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="权限码" prop="code">
            <el-input v-model="temp.code"></el-input>
          </el-form-item>
        </el-col>

      </el-row>

        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="操作" prop="handle">
              <el-input v-model="temp.handle"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="描述" prop="desc">
              <el-input v-model="temp.desc"></el-input>
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
  import {selectPermission, deleteById, savePermission, updatePermission} from "../../../api/permission";
  import {deleteConfirm, successNotify} from "../../../utils";

  export default {
    directives: { waves },
    // 自定义分页组件
    components: { Pagination },

    data() {
      return {

        listQuery: {
          page: 1,
          limit: 10
        },

        temp: {
          id: undefined,
          resource: '',
          code: '',
          handle: '',
          desc: ''
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
    },
    methods: {
      getList() {
        this.listLoading = true;
        selectPermission(this.listQuery).then(response => {
          this.list = response.data;
          this.total = response.count;
          this.listLoading = false
        }).catch(() => {
          this.listLoading = false
        })
      },

      handleCreate() {
        this.resetTemp();
        this.dialogStatus = 'create';
        this.dialogFormVisible = true;
      },

      resetTemp(){
        this.temp = { id: undefined, resource: '', code: '', handle: '', desc: ''}
      },

      createData(){
        savePermission(this.temp).then((response) => {
          this.getList();
          this.dialogFormVisible = false;
          successNotify('创建成功')

        })
      },

      updateData(){
          updatePermission(this.temp).then(() => {
            this.getList();
            this.dialogFormVisible = false;
            successNotify('更新成功')
          })
      },

      handleEdit(index, row) {
        this.temp = Object.assign({}, row);
        this.dialogStatus = 'update';
        this.dialogFormVisible = true;
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
