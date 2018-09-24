<template>
  <div>
    <h3>Login with Username and Password</h3>
    <form id="login">
      <table>
        <tr><td>User:</td><td><input type='text' v-model='username' value=''></td></tr>
        <tr><td>Password:</td><td><input type='password' v-model='password'/></td></tr>
        <tr><td colspan='2'><input name="submit" type="button" value="Login" @click="login"/></td></tr>
      </table>
    </form>
    {{forward}}
    {{$route.query.f}}
  </div>
</template>

<script>
  import fetch from '@/build/fetch.js'
  export default {
    props:{
      forward: String
    },
    data() {
      return {
        username:'',
        password:''
      };
    },
    methods: {
      login:function () {
        fetch.post("/api/auth/login",{username:this.username,password:this.password})
          .then(function (response) {
          console.log("response111")
          console.log(response);
          }).catch(function (error) {
            console.log("error11")
            console.log(error);
         })
      }
    },
  }
</script>
