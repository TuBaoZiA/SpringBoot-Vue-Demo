<template>
  <div class="app-container">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px">

      <el-row :gutter="20">

        <el-col :span="6">
          <el-form-item label="用户名">
            <el-input v-model="listQuery.username"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="6">
          <el-form-item label="手机号">
            <el-input v-model="listQuery.phone"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="6">
          <el-form-item label="住址">
            <el-input v-model="listQuery.address"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="6">
          <el-form-item label="性别">
            <el-select v-model="listQuery.sex" clearable placeholder="请选择性别">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>

      </el-row>

      <el-row :gutter="20">

        <el-col :span="6">
          <el-form-item label="出生日期">
            <el-date-picker
              v-model="listQuery.birthdayData"
              type="datetimerange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd HH:mm:ss"
              unlink-panels
            ></el-date-picker>
          </el-form-item>
        </el-col>

        <el-col :span="8" :offset="4">
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button type="primary" icon="el-icon-edit" @click="handleCreate">新增</el-button>
          <el-button type="primary" :loading="downloadLoading" icon="el-icon-download" @click="handleDownload">导出</el-button>
        </el-col>

      </el-row>

    </el-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      style="width: 100%">

      <el-table-column type="expand" class="demo-table-expand">
        <template slot-scope="props">
          <el-form label-position="left" inline>

            <el-row style="color: #99a9bf;">

              <el-col :span="6">
                <el-form-item label="ID">
                  <span>{{ props.row.id }}</span>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item label="用户名">
                  <span>{{ props.row.username }}</span>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item label="住址">
                  <span>{{ props.row.address }}</span>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item label="邮箱">
                  <span>{{ props.row.email }}</span>
                </el-form-item>
              </el-col>

            </el-row>

            <el-row style="color: #99a9bf;">

              <el-col :span="6">
                <el-form-item label="手机号">
                  <span>{{ props.row.phone }}</span>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item label="出生日期">
                  <span>{{ props.row.birthday}}</span>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item label="性别">
                  <span>{{ props.row.sex | sexFilter }}</span>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item label="url">
                  <span>{{ props.row.url }}</span>
                </el-form-item>
              </el-col>

            </el-row>

            <el-row style="color: #99a9bf;">

              <el-col :span="6">
                <el-form-item label="价格">
                  <span>{{ props.row.price }}</span>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="描述">
                  <span>{{ props.row.desc }}</span>
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

      <el-table-column prop="email" label="邮箱" min-width="150" />

      <el-table-column label="住址" width="150">
        <template slot-scope="scope">
          {{ scope.row.address }}
        </template>
      </el-table-column>

      <el-table-column label="出生日期" align="center" min-width="145">
        <template slot-scope="scope">
          {{ scope.row.birthday}}
        </template>
      </el-table-column>

      <el-table-column align="center" prop="phone" label="手机号"/>

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
            <el-form-item label="用户名" prop="username">
              <el-input v-model="temp.username" ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="手机号">
              <el-input v-model="temp.phone" ></el-input>
            </el-form-item>
          </el-col>

        </el-row>

        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="temp.email" ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="住址">
              <el-input v-model="temp.address" ></el-input>
            </el-form-item>
          </el-col>

        </el-row>

        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="出生日期">
              <el-date-picker v-model="temp.birthday" type="datetime" placeholder="请选择你的出生日期" style="width: 258px"/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="temp.sex" clearable placeholder="请选择性别" style="width: 258px">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

        </el-row>

        <el-form-item label="个人描述">
          <el-input type="textarea"
                    :autosize="{ minRows: 2, maxRows: 4}"  v-model="temp.desc" />
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
  import { getInnoDBList, deleteByIdInnoDB , insertInnoDB, updateInnoDB} from '@/api/testAPI'
  import {deleteConfirm, parseDateTime, successNotify} from "@/utils";
  import Pagination from '@/components/Pagination'

  export default {
    // 自定义分页组件
    components: { Pagination },
    // 过滤器
    filters: {
      sexFilter(state) {
        const stateMap = { 0 : '男', 1 : '女'}
        return stateMap[state]
      }
    },

    data() {
      return {
        options: [
          {value: 0,label: '男'},
          {value: 1,label: '女'}
        ],

        listQuery: {
          page: 1,
          limit: 10,
          username : '',
          phone : '',
          address : '',
          sex : '',
          birthdayData: '',
          birthdayStart: '',
          birthdayEnd: ''
        },

        temp: {
          id: undefined,
          username: '',
          email: '',
          birthday: new Date(),
          phone: '',
          sex: '',
          address: '',
          desc: ''
        },

        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: 'Edit',
          create: 'Create'
        },
        rules: {
          username: [{ required: true, message: 'username is required', trigger: 'change' }]
        },

        total : 0,
        list: [],
        listLoading: false,
        downloadLoading: false
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true;
        getInnoDBList(this.listQuery).then(response => {
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
        this.temp = { id: undefined, username: '', email: '', birthday: new Date(), phone: '', sex: '', address: '', desc: ''}
      },

      reFlushList(message){
        this.getList();
        this.dialogFormVisible = false;
        successNotify(message)
      },

      createData(){
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            insertInnoDB(this.temp).then((response) => {
              this.reFlushList('创建成功');
            })
          }
        })
      },

      updateData(){
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            updateInnoDB(this.temp).then(() => {
              this.reFlushList('更新成功');
            })
          }
        })
      },

      handleDownload() {
        this.downloadLoading = true;
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['id', 'username', 'phone', 'address', 'email', 'sex', 'birthday', 'price', 'level', 'desc'];
          const filterVal = ['id', 'username', 'phone', 'address', 'email', 'sex', 'birthday', 'price', 'level', 'desc'];
          const data = this.formatJson(filterVal, this.list);
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: 'table-data'
          });
          this.downloadLoading = false
        })
      },

      handleEdit(index, row) {
        this.temp = Object.assign({}, row);
        this.dialogStatus = 'update';
        this.dialogFormVisible = true;
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },

      handleDelete(index, row) {
        deleteConfirm(() => {
          deleteByIdInnoDB(row.id).then(response => {
            successNotify('删除成功');
            this.list.splice(index, 1);
          }).catch(() => {})
        })
      },

      formatJson(filterVal, jsonData) {
        return jsonData.map(v => filterVal.map(j => {
          if (j === 'birthday') {
            return parseDateTime(v[j])
          } else {
            return v[j]
          }
        }))
      }
    }
  }
</script>
