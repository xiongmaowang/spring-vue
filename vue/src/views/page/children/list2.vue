<template>
  <div class="about">
    <h1>This is an about page</h1>
    <router-link :to="{ name: 'page/list2', query: { userId: '123', userName:'熊猫王2'}}">User2</router-link>
    <router-link :to="{ name: 'page/list2', query: { userId: '122', userName:'熊猫王1' }}">User1</router-link>
    <input>
    <h2>{{paramsUser}} paramsId:{{paramsId}}</h2>
    <el-table
      :data="tableData"
      style="width: 100%">
      <el-table-column
        prop="date"
        label="日期"
        width="180">
      </el-table-column>
      <el-table-column
        prop="name"
        label="姓名"
        width="180">
      </el-table-column>
      <el-table-column
        prop="address"
        label="地址">
      </el-table-column>
    </el-table>
    <el-pagination
      small
      layout="prev, pager, next"
      :total="50">
    </el-pagination>
  </div>

</template>
<script>
  export default {
    data () {
      return {
        radio: '1',
        paramsUser:this.$route.query.userName,
        paramsId:this.$route.query.userId,
        tableData: [{
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1517 弄'
        }, {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1519 弄'
        }, {
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1516 弄'
        }]
      };
    },
    mounted:function () {
      console.log(this.$route)
    },
    beforeRouteUpdate:function (to, from, next) {
      this.paramsUser = to.query.userName
      this.paramsId = to.query.userId
      console.log(to)
      console.log(from)
      next();
    }
  }
</script>
