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

      <el-table-column prop="jobName" label="任务名称" min-width="100px"/>

      <el-table-column prop="jobGroup" label="任务组" />

      <el-table-column prop="jobDescription" label="任务描述"/>

      <el-table-column prop="jobStatus" label="任务状态" />

      <el-table-column prop="cronExpression" label="任务表达式" />

      <el-table-column prop="createTime" label="创建时间" min-width="90" />

      <el-table-column label="操作" min-width="130" align="center">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" size="small" @click.native="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="text" icon="el-icon-delete" size="small" style="color: #ff0000" @click.native="handleDelete(scope.$index, scope.row)">删除</el-button>
          <el-button v-if="scope.row.jobStatus === 'NORMAL'" style="color: #fff000" type="text" size="small" @click.native="handlePause(scope.$index, scope.row)">暂停</el-button>
          <el-button v-else type="text" icon="el-icon-refresh" size="small" @click.native="handleResume(scope.$index, scope.row)">恢复</el-button>
        </template>

      </el-table-column>

    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">

      <el-form ref="dataForm" :model="temp" label-position="left" label-width="120px" style="width: 93%;margin-left: 25px">

          <el-form-item label="任务Bean名称" prop="jobName">
            <el-input v-model="temp.jobName" :readonly="dialogStatus==='update'"></el-input>
          </el-form-item>

          <el-form-item label="任务组" prop="jobGroup">
            <el-input v-model="temp.jobGroup" :readonly="dialogStatus==='update'"></el-input>
          </el-form-item>

            <el-form-item label="执行表达式" prop="cron">
              <el-input v-model="temp.cronExpression"></el-input>
            </el-form-item>

            <el-form-item label="任务描述" prop="desc">
              <el-input v-model="temp.jobDescription"></el-input>
            </el-form-item>

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
  import {deleteConfirm, successNotify} from "../../../utils";
  import {deleteJob, pauseJob, resumeJob, saveJob, selectJob, updateJob} from "../../../api/job";

  export default {
    directives: { waves },
    // 自定义分页组件
    components: { Pagination },

    data() {
      return {

        temp: {
          id: undefined,
          jobName: '',
          jobGroup: '',
          jobDescription: '',
          cronExpression: ''
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
        selectJob().then(response => {
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
        this.temp = { id: undefined, jobName: '', jobGroup: '', jobDescription: '', cronExpression: ''}
      },

      createData(){
        saveJob(this.temp).then((response) => {
          this.getList();
          this.dialogFormVisible = false;
          successNotify('创建成功')
        })
      },

      updateData(){
        updateJob(this.temp).then(() => {
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
          deleteJob(row.jobName, row.jobGroup).then(response => {
            successNotify('删除成功');
            this.list.splice(index, 1)
          }).catch(() => {})
        });
      },

      handlePause(index, row){
        pauseJob(row.jobName, row.jobGroup).then(response => {
          this.getList();
          successNotify('暂停成功')
        }).catch(() => {})
      },

      handleResume(index, row){
        resumeJob(row.jobName, row.jobGroup).then(response => {
          this.getList();
          successNotify('任务已恢复')
        }).catch(() => {})
      }
    }
  }
</script>
