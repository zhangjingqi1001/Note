<template>
  <div class="school">
    <h2 >学校名称:{{ name }}</h2>
    <h2>学校地址：{{ address }}</h2>
    <button @click="sendSchoolName">把学校名给App</button>
  </div>
</template>

<script>
export default {
  //组件名称
  name: "School",
  props:['getSchoolName'],
  data() {
    return {
      name: "齐工大",
      address: "济南",
    };
  },
  methods:{
    sendSchoolName(){
      this.getSchoolName(this.name)
    }
  }
};
</script>

<style scoped>
.school {
  background: skyblue;
  padding: 5px;
  margin-top: 30px;
}
</style>
