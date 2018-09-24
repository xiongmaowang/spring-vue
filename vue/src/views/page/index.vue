<template>
  <el-container style="height: 100%;">
    <el-aside :width="menuWidth">
        <el-menu :collapse-transition=false default-active="1-2"  class="el-menu-vertical-demo"     :router="router"
          @open="handleOpen"   @close="handleClose"          :collapse="isCollapse">
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span slot="title">导航一</span>
            </template>
            <el-menu-item-group>
              <span slot="title">分组一</span>
              <el-menu-item index="1-1">选项1</el-menu-item>
              <el-menu-item index="1-2">选项2</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
              <el-menu-item index="1-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="1-4">
              <span slot="title">选项4</span>
              <el-menu-item index="1-4-1">选项1</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-menu-item index="2">
            <i class="el-icon-menu"></i>
            <span slot="title">导航二</span>
          </el-menu-item>
          <el-menu-item index="3" disabled>
            <i class="el-icon-document"></i>
            <span slot="title">导航三</span>
          </el-menu-item>
          <el-menu-item index="4">
            <i class="el-icon-setting"></i>
            <span slot="title">导航四</span>
          </el-menu-item>
        </el-menu>
    </el-aside>
    <el-container>
      <el-header height = "60px">
        <el-button style="font-size: 27px;padding-left: 10px;padding-top: 10px" type="text" size="medium" autofocus icon="el-icon-menu" @click="isCollapse=!isCollapse"></el-button>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>
<!--<script src='@/public/TweenMax.min.js'></script>-->
<style scoped>
  .el-header{
    padding: 0;
  }
</style>
<script>
  import TweenMax from "gsap/TweenMax";
  export default {
    data() {
      return {
        isCollapse: false,
        menuWidth:"201px",
        router: true,
      };
    },
    methods: {
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      }
    },
    watch:{
      "isCollapse" : function (val) {
        var width = val?"66px":"201px";
        TweenMax.to(this.$data, 0.4, { menuWidth: width });
      }
    },
    mounted:function () {
    }
  }
</script>