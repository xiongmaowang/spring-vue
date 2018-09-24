<template>
  <div class="about">
    <h1 @click="getData">This is an about page</h1>
    <div id="animated-number-demo">
      <input v-model.number="number" type="number" step="20">
      <p>{{ animatedNumber }}</p>
      <p>sb {{ sb }}</p>
    </div>
    <div :style="sbStyle">sbsb</div>
  </div>

</template>
<script>
  import fetch from '@/build/fetch.js'
  export default {
    data () {
      return {
        number: 0,
        tweenedNumber: 0,
        sbStyle:{width:'50px'},
        sb : null
      };
    },
    methods:{
      getData:function () {
        var This = this;
        fetch.post("/welcome",{})
          .then(function (response) {
            console.log(response);
            console.log("res");
          })
          .catch(function (error) {
            This.sb = error.response.data.status
            console.log(error)
            console.log("err")
          })
      }
    },
    computed: {
      animatedNumber: function() {
        return this.tweenedNumber.toFixed(0);
      }
    },
    watch: {
    },
    mounted:function () {
    }
  }
</script>
